package org.foi.nwtis.mkovac.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
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

/**
 * Klasa SimulatorMeteo zadužena je za simulaciju slanja meteo podataka. Svoj rad započinje čitanjem
 * postavki iz konfiguracijske datoteke, a zatim otvara datoteku s meteo podacima te simulira
 * njihovo slanje. Meteo podaci su redom: senzor, vrijeme, temperatura, vlaga, tlak.
 * 
 * @author Marijan Kovač
 *
 */

public class SimulatorMeteo {

  private String trajanjeSekunde;
  private String korisnickoIme;
  private String korisnickaLozinka;
  private String posluziteljGlavniAdresa;
  private String posluziteljGlavniVrata;
  private String maksCekanje;
  private int brojPokusaja;
  private String datotekaProblema;

  /**
   * Glavna funkcija koja služi za pokretanje programa SimulatorMeteo.
   * 
   * @param args Naziv konfiguracijske datoteke s postavkama za pokretanje programa. Dozvoljeni
   *        formati za datoteku su: .txt | .xml | .bin | .json | .yaml
   */

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

  /**
   * Funkcija provjerava je li ulazni argument ispravan.
   * 
   * @param args Ulazni argument
   * @return Vraća true ako je u redu, inače false.
   */
  private static boolean provjeriArgumente(String[] args) {
    if (args.length != 1)
      return false;

    String regex = "\\w+.(txt|xml|bin|json|yaml)$";
    String s = args[0].trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }

  /**
   * Funkcija obavlja pokretanje simulacije. Vrši učitavanje konfiguracijskih postavki, čita meteo
   * podatke iz datoteke te periodički šalje podatke na server. Periodičko se slanje simulira
   * izračunavanjem spavanja na temelju razlike vremena između dva podatka.
   * 
   * @param konf Objekt s postavkama učitanim iz konfiguracijske datoteke
   * @throws IOException Ukoliko postoji problem sa metodom Thread.sleep()
   * @see izracunajSpavanje
   */
  private void pokreniSimulator(Konfiguracija konf) throws IOException {
    trajanjeSekunde = konf.dajPostavku("trajanjeSekunde");
    korisnickoIme = konf.dajPostavku("korisnickoIme");
    korisnickaLozinka = konf.dajPostavku("korisnickaLozinka");
    posluziteljGlavniAdresa = konf.dajPostavku("posluziteljGlavniAdresa");
    posluziteljGlavniVrata = konf.dajPostavku("posluziteljGlavniVrata");
    maksCekanje = konf.dajPostavku("maksCekanje");
    brojPokusaja = Integer.parseInt(konf.dajPostavku("brojPokusaja"));
    datotekaProblema = konf.dajPostavku("datotekaProblema");

    var nazivDatoteke = konf.dajPostavku("datotekaMeteo");
    var putanja = Path.of(nazivDatoteke);

    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    var citac = Files.newBufferedReader(putanja, Charset.forName("UTF-8"));

    MeteoSimulacija prethodniMeteo = null;

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
            if (spavanjeKorigirano < 0) {
              Logger.getGlobal().log(Level.WARNING, "Zapis iz prošlosti, preskačem slanje!");
              continue;
            }

            if (spavanjeKorigirano != 0)
              Thread.sleep(spavanjeKorigirano);

          } catch (InterruptedException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
          }
        }

        this.posaljiMeteoPodatak(vazeciMeteo);
        prethodniMeteo = vazeciMeteo;
      }
    }
  }

  /**
   * Izračunava spavanje u milisekundama na temelju razlike prethodnog i trenutnog meteo podatka.
   * Vrijeme se dodatno korigira ovisno o postavki za trajanje sekunde kako bi se ubrzala ili
   * usporila simulacija slanja podataka.
   * 
   * @param prethodniMeteo Objekt tipa Record s prethodno poslanim meteo podatcima
   * @param vazeciMeteo Objekt tipa Record s trenutnim meteo podatcima
   * @return Vraća preračunato vrijeme u milisekundama (ms)
   */
  private long izracunajSpavanje(MeteoSimulacija prethodniMeteo, MeteoSimulacija vazeciMeteo) {
    String prvi = prethodniMeteo.vrijeme();
    String drugi = vazeciMeteo.vrijeme();

    long ms1 = 0, ms2 = 0;

    ms1 = konvertirajVrijeme(prvi);
    ms2 = konvertirajVrijeme(drugi);

    long kraj = ms2;
    long pocetak = ms1;

    long spavanje = kraj - pocetak;

    long trajanje = Long.parseLong(trajanjeSekunde);

    long spavanjeKorigirano = spavanje / (1000 / trajanje);

    return spavanjeKorigirano;

  }

  /**
   * Konvertira vrijeme iz formatiranog zapisa u obliku HH:mm:ss u vrijeme u milisekundama (ms)
   * 
   * @param vrijeme Formatirani zapis vremena u obliku HH:mm:ss
   * @return Vraća vrijeme u milisekundama
   */
  private long konvertirajVrijeme(String vrijeme) {
    String[] str = vrijeme.split(":");

    int h = Integer.parseInt(str[0]);
    int m = Integer.parseInt(str[1]);
    int s = Integer.parseInt(str[2]);

    return (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000);

  }

  /**
   * Učitava postavke iz datoteke u objekt Konfiguracija
   * 
   * @param datoteka Datoteka s konfiguracijskim postavkama
   * @return Vraća objekt tipa Konfiguracija
   * @throws NeispravnaKonfiguracija Baca iznimku ako učitavanje postavki nije uspjelo
   */
  private Konfiguracija ucitajPostavke(String datoteka) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);

  }

  /**
   * Provjerava je li podatak u datoteci meteo podataka prvi podatak kako se ne bi pokušalo
   * dohvatiti nepostojeći prethodni podatak
   * 
   * @param rbroj Redni broj retka u datoteci
   * @return Vraća true ako je u redu, inače false
   */
  private boolean isPrviPodatak(int rbroj) {
    return rbroj == 2;
  }

  /**
   * Provjerava ima li redak u datoteci s meteo podatcima potrebnih pet atributa
   * 
   * @param atributi Polje s atributima učitanim iz datoteke meteo podataka
   * @return Vraća true ako je u redu, inače false
   */
  private boolean redImaPetAtributa(String[] atributi) {
    return atributi.length == 5;
  }

  /**
   * Provjerava je li podatak u datoteci meteo podataka zaglavlje
   * 
   * @param rbroj Redni broj retka u datoteci
   * @return Vraća true ako je u redu, inače false
   */
  private boolean isZaglavlje(int rbroj) {
    return rbroj == 1;
  }

  /**
   * Šalje važeći meteo podatak na GlavniPosluzitelj putem mrežne utičnice na način da šalje poruku
   * u zadanom formatu. Ukoliko se od GlavniPosluzitelj dobije pogreška, slanje se ponavlja onoliko
   * puta koliko je to zadano postavkom. Ukoliko slanje ne uspije nakon zadanog broja pokušaja,
   * pogreška se upisuje u datoteku problema te se nastavlja sa slanjem drugih podataka.
   * 
   * @param vazeciMeteo Objekt tipa Record s važećim meteo podatcima za slanje
   * @see upisiDatotekaProblema
   */
  private void posaljiMeteoPodatak(MeteoSimulacija vazeciMeteo) {
    boolean ponovno = false;
    int pokusaj = 0;
    String problem = "";

    do {

      try {
        var mreznaUticnica =
            new Socket(posluziteljGlavniAdresa, Integer.parseInt(posluziteljGlavniVrata));

        mreznaUticnica.setSoTimeout(Integer.parseInt(maksCekanje));

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
        mreznaUticnica.shutdownOutput();

        var poruka = new StringBuilder();
        while (true) {
          var red = citac.readLine();
          if (red == null)
            break;
          poruka.append(red);
        }

        Logger.getGlobal().log(Level.INFO, "Odgovor: " + poruka);

        mreznaUticnica.shutdownInput();

        if (poruka.toString().contains("ERROR")) {
          problem = poruka.toString();
          pokusaj++;
          ponovno = true;
        } else
          ponovno = false;



        mreznaUticnica.close();

      } catch (NumberFormatException | IOException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

    } while (ponovno && pokusaj < brojPokusaja);

    if (pokusaj == brojPokusaja) {

      try {
        upisiDatotekaProblema(datotekaProblema, vazeciMeteo, problem);
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE,
            "Greška u upisivanju u datotekaProblema!" + e.getMessage());
      }
    }
  }

  /**
   * Upisuje neuspjeli pokušaj slanja meteo podataka na GlavniPosluzitelj s opisom problema u
   * datoteku problema
   * 
   * @param nazivDatoteke Naziv datoteke problema
   * @param vazeciMeteo Objekt tipa Record s meteo podacima
   * @param problem Opis problema primljen od GlavniPosluzitelj
   * @throws IOException Baca iznimku ako je greška u otvaranju ili pisanju u datoteku problema
   */
  private void upisiDatotekaProblema(String nazivDatoteke, MeteoSimulacija vazeciMeteo,
      String problem) throws IOException {
    var putanja = Path.of(nazivDatoteke);

    if (!Files.exists(putanja)) {
      Files.createFile(putanja);
    }

    if (Files.isDirectory(putanja) || !Files.isWritable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    if (putanja.toFile().length() != 0) {
      BufferedWriter bw = new BufferedWriter(new FileWriter(putanja.toFile(), true));

      bw.write(vazeciMeteo.id() + ";" + vazeciMeteo.vrijeme() + ";" + vazeciMeteo.temperatura()
          + ";" + vazeciMeteo.vlaga() + ";" + vazeciMeteo.tlak() + ";" + problem);

      bw.newLine();

      bw.close();
    } else {
      BufferedWriter bw = new BufferedWriter(new FileWriter(putanja.toFile(), true));

      bw.write("id;vrijeme;temp;vlaga;tlak;opis problema");

      bw.newLine();

      bw.close();
    }

  }
}
