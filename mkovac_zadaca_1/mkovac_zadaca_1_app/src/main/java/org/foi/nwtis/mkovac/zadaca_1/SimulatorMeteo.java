package org.foi.nwtis.mkovac.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.MeteoSimulacija;

public class SimulatorMeteo {

  private String trajanjeSekunde;
  private String korisnickoIme;
  private String korisnickaLozinka;
  private String posluziteljGlavniAdresa;
  private String posluziteljGlavniVrata;

  public static void main(String[] args) {
    var sm = new SimulatorMeteo();
    if (!SimulatorMeteo.provjeriArgumente(args)) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke ili datoteka nema odgovarajući format!");
      return;
    }

    try {
      var konf = sm.ucitajPostavke(args[0]);
      sm.pokreniSimulator(konf);
    } catch (NeispravnaKonfiguracija e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE, e.getMessage());
    } catch (IOException e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Greška učitavanja meteo podataka" + e.getMessage());
    }

  }

  private static boolean provjeriArgumente(String[] args) {
    // return args.length == 1 ? true : false;
    if (args.length != 1)
      return false;

    String regex = "\\w+.(txt|xml|bin|json|yaml)$";
    String s = args[0].trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }

  private void pokreniSimulator(Konfiguracija konf) throws IOException {
    // preuzimanje postavki
    trajanjeSekunde = konf.dajPostavku("trajanjeSekunde");
    korisnickoIme = konf.dajPostavku("korisnickoIme");
    korisnickaLozinka = konf.dajPostavku("korisnickaLozinka");
    posluziteljGlavniAdresa = konf.dajPostavku("posluziteljGlavniAdresa");
    posluziteljGlavniVrata = konf.dajPostavku("posluziteljGlavniVrata");
    // TODO dodati ostale, myb u globalno i onda ucitavati putem zasebne funkcije!

    // otvaranje datoteke
    var nazivDatoteke = konf.dajPostavku("datotekaMeteo");
    var putanja = Path.of(nazivDatoteke);

    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    var citac = Files.newBufferedReader(putanja, Charset.forName("UTF-8"));

    MeteoSimulacija prethodniMeteo = null; // klasa se moze kreirati na desni klik, pa onda odabrati
                                           // i package u koji se zeli staviti

    int rbroj = 0;

    while (true) {
      var red = citac.readLine();
      if (red == null)
        break;
      rbroj++;
      if (isZaglavlje(rbroj))
        continue;

      var atributi = red.split(";");
      if (!redImaPetAtributa(atributi)) {
        Logger.getGlobal().log(Level.WARNING, red);
      } else {
        MeteoSimulacija vazeciMeteo =
            new MeteoSimulacija(atributi[0], atributi[1], Float.parseFloat(atributi[2]),
                Float.parseFloat(atributi[3]), Float.parseFloat(atributi[4]));

        if (!isPrviPodatak(rbroj)) {
          long spavanjeKorigirano = this.izracunajSpavanje(prethodniMeteo, vazeciMeteo);

          try {
            if (spavanjeKorigirano != 0)
              Thread.sleep(spavanjeKorigirano);

          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }

        this.posaljiMeteoPodatak(vazeciMeteo);
        prethodniMeteo = vazeciMeteo;
      }
    }
  }

  private long izracunajSpavanje(MeteoSimulacija prethodniMeteo, MeteoSimulacija vazeciMeteo) {
    String prvi = prethodniMeteo.vrijeme();
    String drugi = vazeciMeteo.vrijeme();

    long ms1 = 0, ms2 = 0;

    ms1 = konvertirajVrijeme(prvi);
    ms2 = konvertirajVrijeme(drugi);

    /*
     * LocalTime time2 = LocalTime.parse(drugi); ms2 = time2.toSecondOfDay() * 1000L;
     * 
     * LocalTime time1 = LocalTime.parse(prvi); ms1 = time1.toSecondOfDay() * 1000L;
     */

    /*
     * SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); try { Date date2 = sdf.parse(drugi);
     * ms2 = date2.getTime(); Date date1 = sdf.parse(prvi); ms1 = date1.getTime(); } catch
     * (ParseException e) { // TODO Auto-generated catch block e.printStackTrace(); }
     */

    long kraj = ms2; // drugi u milisekundama //10?
    long pocetak = ms1; // prvi u milisekundama //5?

    long spavanje = kraj - pocetak;

    // TODO napraviti korekciju temeljem podatka o trajanjuSekunde
    long trajanje = Long.parseLong(trajanjeSekunde);

    long spavanjeKorigirano = spavanje / (1000 / trajanje);

    return spavanjeKorigirano;

  }

  private long konvertirajVrijeme(String vrijeme) {
    String[] str = vrijeme.split(":");

    int h = Integer.parseInt(str[0]);
    int m = Integer.parseInt(str[1]);
    int s = Integer.parseInt(str[2]);

    return (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000);

  }

  private Konfiguracija ucitajPostavke(String string) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(string);

  }

  private boolean isPrviPodatak(int rbroj) {
    return rbroj == 2;
  }

  private boolean redImaPetAtributa(String[] atributi) {
    return atributi.length == 5;
  }

  private boolean isZaglavlje(int rbroj) {
    return rbroj == 1;
  }

  private void posaljiMeteoPodatak(MeteoSimulacija vazeciMeteo) {
    // TODO isto kao što smo radili u GlavnomKlijentu
    try {
      var mreznaUticnica =
          new Socket(posluziteljGlavniAdresa, Integer.parseInt(posluziteljGlavniVrata));

      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String zahtjev = "KORISNIK " + korisnickoIme + " LOZINKA " + korisnickaLozinka;
      zahtjev += " SENZOR " + vazeciMeteo.id() + " " + vazeciMeteo.vrijeme();
      zahtjev += " " + vazeciMeteo.temperatura();
      if (vazeciMeteo.vlaga() != -999)
        zahtjev += " " + vazeciMeteo.vlaga();
      if (vazeciMeteo.tlak() != -999)
        zahtjev += " " + vazeciMeteo.tlak();

      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();// sa slanja na primanje

      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        poruka.append(red);
      }

      Logger.getGlobal().log(Level.INFO, "Odgovor: " + poruka);
      // TODO ispitati odgovor, ako se vrati kod pogreske, mora se ponoviti postupak!!!

      mreznaUticnica.shutdownInput(); // s primanja na slanje
      mreznaUticnica.close();

    } catch (NumberFormatException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
