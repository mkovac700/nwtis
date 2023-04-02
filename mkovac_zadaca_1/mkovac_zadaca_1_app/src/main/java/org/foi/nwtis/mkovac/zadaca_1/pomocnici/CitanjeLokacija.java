package org.foi.nwtis.mkovac.zadaca_1.pomocnici;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Lokacija;

/**
 * Klasa CitanjeLokacija pomoćna je klasa zadužena za čitanje podataka o lokacijama iz datoteke
 * *.csv
 * 
 * @author Marijan Kovač
 *
 */
public class CitanjeLokacija {
  /**
   * Učitava datoteku lokacija *.csv te sprema podatke u kolekciju
   * 
   * @param nazivDatoteke Naziv datoteke lokacija *.csv
   * @return Vraća kolekciju u obliku Map s lokacijama
   * @throws IOException Baca iznimku ako otvaranje ili čitanje datoteke nije uspješno
   */
  public Map<String, Lokacija> ucitajDatoteku(String nazivDatoteke) throws IOException {
    var putanja = Path.of(nazivDatoteke);
    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    var lokacije = new HashMap<String, Lokacija>();

    var citac = Files.newBufferedReader(putanja, Charset.forName("UTF-8"));

    while (true) {
      var red = citac.readLine();
      if (red == null)
        break;

      var atributi = red.split(";");
      if (!redImaCetiriAtributa(atributi)) {
        Logger.getGlobal().log(Level.WARNING, red);
      } else {
        var lokacija = new Lokacija(atributi[0], atributi[1], atributi[2], atributi[3]);
        lokacije.put(atributi[1], lokacija);
      }
    }

    return lokacije;
  }

  /**
   * Provjerava ima li redak u datoteci 4 atributa
   * 
   * @param atribut Polje atributa
   * @return Vraća true ako je u redu, inače false
   */
  private boolean redImaCetiriAtributa(String[] atribut) {
    return atribut.length == 4;
  }
}
