package org.foi.nwtis.mkovac.zadaca_1;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Korisnik;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Uredaj;
import org.foi.nwtis.mkovac.zadaca_1.pomocnici.CitanjeKorisnika;

/**
 * Klasa GlavniPosluzitelj koja je zadužena za otvaranje veze na određenim mrežnim vratima
 * 
 * @author Marijan Kovač
 *
 */
public class GlavniPosluzitelj {
  protected Konfiguracija konfig;
  protected Map<String, Korisnik> korisnici;
  protected Map<String, Uredaj> uredaji;
  private int ispis = 0;

  public GlavniPosluzitelj(Konfiguracija konfig) {
    this.konfig = konfig;
    this.ispis = Integer.parseInt(konfig.dajPostavku("ispis"));
  }

  public void pokreniPosluzitelja() {
    try {
      ucitajKorisnika();
      // TODO dodati ucitavanje ostalih podataka
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  /**
   * Učitava sve korisnike iz CSV datoteke koja je definirana u postavci datotekaKorisnika
   * 
   * @throws IOException baca iznimku ako je problem s učitavanjem
   */
  private void ucitajKorisnika() throws IOException {
    var nazivDatoteke = this.konfig.dajPostavku("datotekaKorisnika");
    var citacKorisnika = new CitanjeKorisnika();
    this.korisnici = citacKorisnika.ucitajDatoteku(nazivDatoteke);
    if (this.ispis == 1) {
      for (String korime : this.korisnici.keySet()) {
        var korisnik = this.korisnici.get(korime);
        Logger.getGlobal().log(Level.INFO,
            "Korisnik: " + korisnik.prezime() + " " + korisnik.ime());
      }
    }
  }

}
