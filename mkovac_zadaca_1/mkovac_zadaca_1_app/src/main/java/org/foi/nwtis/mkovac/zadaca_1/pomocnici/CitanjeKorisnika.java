package org.foi.nwtis.mkovac.zadaca_1.pomocnici;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Korisnik;

/**
 * Klasa CitanjeKorisnika pomoćna je klasa zadužena za čitanje podataka o korisnicima iz datoteke
 * *.csv
 * 
 * @author Marijan Kovač
 *
 */
public class CitanjeKorisnika {
  /**
   * Učitava datoteku korisnika *.csv te sprema podatke u kolekciju
   * 
   * @param nazivDatoteke Naziv datoteke korisnika *.csv
   * @return Vraća kolekciju u obliku Map s korisnicima
   * @throws IOException Baca iznimku ako otvaranje ili čitanje datoteke nije uspješno
   */
  public Map<String, Korisnik> ucitajDatoteku(String nazivDatoteke) throws IOException {
    var putanja = Path.of(nazivDatoteke);
    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    var korisnici = new HashMap<String, Korisnik>();

    var citac = Files.newBufferedReader(putanja, Charset.forName("UTF-8"));

    while (true) {
      var red = citac.readLine();
      if (red == null)
        break;

      var atributi = red.split(";");
      if (!redImaPetAtributa(atributi)) {
        Logger.getGlobal().log(Level.WARNING, red);
      } else {
        boolean administrator = isAdministrator(atributi[4]);
        // boolean administrator = false;
        var korisnik =
            new Korisnik(atributi[0], atributi[1], atributi[2], atributi[3], administrator);
        korisnici.put(atributi[2], korisnik);
      }
    }

    return korisnici;
  }

  /**
   * Određuje je li korisnik administrator
   * 
   * @param atribut Podatak u datoteci korisnika iz stupca admin
   * @return Vraća true ako je u redu, inače false
   */
  private boolean isAdministrator(String atribut) {
    return atribut.compareTo("1") == 0 ? true : false;
  }

  /**
   * Provjerava ima li redak u datoteci 5 atributa
   * 
   * @param atribut Polje atributa
   * @return Vraća true ako je u redu, inače false
   */
  private boolean redImaPetAtributa(String[] atribut) {
    return atribut.length == 5;
  }
}
