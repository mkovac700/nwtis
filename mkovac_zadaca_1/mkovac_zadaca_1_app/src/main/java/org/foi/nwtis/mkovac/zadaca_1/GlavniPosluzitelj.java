package org.foi.nwtis.mkovac.zadaca_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Korisnik;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Lokacija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Uredaj;
import org.foi.nwtis.mkovac.zadaca_1.pomocnici.CitanjeKorisnika;
import org.foi.nwtis.mkovac.zadaca_1.pomocnici.CitanjeLokacija;
import org.foi.nwtis.mkovac.zadaca_1.pomocnici.CitanjeUredaja;

/**
 * Klasa GlavniPosluzitelj koja je zadužena za otvaranje veze na određenim mrežnim vratima
 * 
 * @author Marijan Kovač
 *
 */
public class GlavniPosluzitelj {
  protected Konfiguracija konfig;
  protected Map<String, Korisnik> korisnici;
  protected Map<String, Lokacija> lokacije;
  protected Map<String, Uredaj> uredaji;

  private int ispis = 0;
  private int mreznaVrata = 8000; // default
  private int brojCekaca = 10;
  private boolean kraj = false;

  public GlavniPosluzitelj(Konfiguracija konfig) {
    this.konfig = konfig;
    this.ispis = Integer.parseInt(konfig.dajPostavku("ispis"));
    this.mreznaVrata = Integer.parseInt(konfig.dajPostavku("mreznaVrata"));
    this.brojCekaca = Integer.parseInt(konfig.dajPostavku("brojCekaca"));
  }

  public void pokreniPosluzitelja() {
    try {
      ucitajKorisnike();
      ucitajLokacije();
      ucitajUredaje();

      otvoriMreznaVrata();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  private void ucitajUredaje() throws IOException {
    var nazivDatoteke = this.konfig.dajPostavku("datotekaUredaja");
    var citacUredaja = new CitanjeUredaja();
    this.uredaji = citacUredaja.ucitajDatoteku(nazivDatoteke);
    if (this.ispis == 1) {
      for (String id : this.uredaji.keySet()) {
        var uredaj = this.uredaji.get(id);
        Logger.getGlobal().log(Level.INFO, "Uredaj: " + uredaj.naziv() + " (" + uredaj.id() + ")");
      }
    }
  }

  private void ucitajLokacije() throws IOException {
    var nazivDatoteke = this.konfig.dajPostavku("datotekaLokacija");
    var citacLokacija = new CitanjeLokacija();
    this.lokacije = citacLokacija.ucitajDatoteku(nazivDatoteke);
    if (this.ispis == 1) {
      for (String id : this.lokacije.keySet()) {
        var lokacija = this.lokacije.get(id);
        Logger.getGlobal().log(Level.INFO,
            "Lokacija: " + lokacija.naziv() + " (" + lokacija.id() + ")");
      }
    }
  }

  /**
   * Učitava sve korisnike iz CSV datoteke koja je definirana u postavci datotekaKorisnika
   * 
   * @throws IOException baca iznimku ako je problem s učitavanjem
   */
  public void ucitajKorisnike() throws IOException {
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

  public void otvoriMreznaVrata() {
    try (var posluzitelj = new ServerSocket(this.mreznaVrata, this.brojCekaca)) {
      int brojacDretvi = 0;
      while (!this.kraj) {
        var uticnica = posluzitelj.accept(); // program stoji i ceka da dode zahtjev (od
                                             // SimulatorMeteo ili GlavniKlijent)
        var dretva = new MrezniRadnik(uticnica, konfig);

        dretva.korisnici = this.korisnici;
        dretva.lokacije = this.lokacije;
        dretva.uredaji = this.uredaji;

        dretva.setName("mkovac_" + brojacDretvi++);
        dretva.start();

      }


    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
