package org.foi.nwtis.mkovac.zadaca_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Korisnik;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Lokacija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Ocitanje;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Uredaj;
import org.foi.nwtis.mkovac.zadaca_1.pomocnici.CitanjeKorisnika;
import org.foi.nwtis.mkovac.zadaca_1.pomocnici.CitanjeLokacija;
import org.foi.nwtis.mkovac.zadaca_1.pomocnici.CitanjeUredaja;

/**
 * Klasa GlavniPosluzitelj zadužena je za otvaranje veze na određenim mrežnim vratima. Na
 * poslužitelja se mogu spojiti klijenti ili SimulatorMeteo koji šalju različite zahtjeve za obradu.
 * 
 * @author Marijan Kovač
 *
 */
public class GlavniPosluzitelj {
  protected Konfiguracija konfig;
  protected Map<String, Korisnik> korisnici;
  protected Map<String, Lokacija> lokacije;
  protected Map<String, Uredaj> uredaji;

  private List<Ocitanje> listaOcitanja = new ArrayList<>();

  private int ispis = 0;
  private int mreznaVrata = 8000; // default
  private int brojCekaca = 10;
  private boolean kraj = false;

  /**
   * Konstruktor za GlavniPosluzitelj
   * 
   * @param konfig Objekt tipa Konfiguracija s postavkama
   */
  public GlavniPosluzitelj(Konfiguracija konfig) {
    this.konfig = konfig;
    this.ispis = Integer.parseInt(konfig.dajPostavku("ispis"));
    this.mreznaVrata = Integer.parseInt(konfig.dajPostavku("mreznaVrata"));
    this.brojCekaca = Integer.parseInt(konfig.dajPostavku("brojCekaca"));
  }

  /**
   * Učitava podatke o korisnicima, lokacijama i uređajima iz .csv datoteke u odgovarajuće kolekcije
   * te potom otvara mrežna vrata.
   */
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

  /**
   * Učitava podatke o uređajima iz .csv datoteke u odgovarajuću kolekciju.
   * 
   * @throws IOException Baca iznimku ukoliko čitanje datoteke nije uspjelo.
   */
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

  /**
   * Učitava podatke o lokacijama iz .csv datoteke u odgovarajuću kolekciju.
   * 
   * @throws IOException Baca iznimku ukoliko čitanje datoteke nije uspjelo.
   */
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
   * Učitava podatke o uređajima iz .csv datoteke u odgovarajuću kolekciju.
   * 
   * @throws IOException Baca iznimku ukoliko čitanje datoteke nije uspjelo.
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

  /**
   * Otvara mrežna vrata te čeka na spajanje klijenata. Nakon spajanja klijenta daljnji se rad
   * prebacuje u dretvu MrezniRadnik koja obrađuje dolazne zahtjeve.
   * 
   * @see MrezniRadnik
   */
  public void otvoriMreznaVrata() {
    try (var posluzitelj = new ServerSocket(this.mreznaVrata, this.brojCekaca)) {
      int brojacDretvi = 0;
      List<Thread> dretve = new ArrayList<>();

      while (!this.kraj) {

        var uticnica = posluzitelj.accept();

        var dretva = new MrezniRadnik(uticnica, konfig);

        dretva.korisnici = this.korisnici;
        dretva.lokacije = this.lokacije;
        dretva.uredaji = this.uredaji;
        dretva.kraj = this.kraj;

        dretva.listaOcitanja = this.listaOcitanja;

        dretva.setName("mkovac_" + brojacDretvi++);
        dretva.start();

        dretve.add(dretva);
      }

      posluzitelj.close();

      for (Thread d : dretve) {
        try {
          d.join();
        } catch (InterruptedException e) {
          Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
      }

    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }

  }

}
