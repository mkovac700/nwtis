package org.foi.nwtis.mkovac.zadaca_1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.MeteoSimulacija;

public class SimulatorMeteo {

  public static void main(String[] args) {
    var sm = new SimulatorMeteo();
    if (!SimulatorMeteo.provjeriArgumente(args)) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke!");
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
    return args.length == 1 ? true : false;
  }

  private void pokreniSimulator(Konfiguracija konf) throws IOException {
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
        var vazeciMeteo =
            new MeteoSimulacija(atributi[0], atributi[1], Float.parseFloat(atributi[2]),
                Float.parseFloat(atributi[3]), Float.parseFloat(atributi[4]));

        if (!isPrviPodatak(rbroj)) {
          this.izracunajSpavanje(prethodniMeteo, vazeciMeteo);
        }

        this.posaljiMeteoPodatak(vazeciMeteo);
        prethodniMeteo = vazeciMeteo;
      }
    }
  }

  private void izracunajSpavanje(MeteoSimulacija prethodniMeteo, MeteoSimulacija vazeciMeteo) {
    String prvi = prethodniMeteo.vrijeme();
    String drugi = vazeciMeteo.vrijeme();
    int kraj = 10; // drugi u milisekundama
    int pocetak = 5; // prvi u milisekundama

    int spavanje = kraj - pocetak;
    // TODO napraviti korekciju temeljem podatka o trajanjuSekunde

    try {
      Thread.sleep(spavanje);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

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

  }
}