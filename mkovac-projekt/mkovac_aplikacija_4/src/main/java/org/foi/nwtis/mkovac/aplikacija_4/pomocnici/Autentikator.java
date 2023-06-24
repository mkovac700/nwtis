package org.foi.nwtis.mkovac.aplikacija_4.pomocnici;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.KorisniciFacade;
import jakarta.persistence.NoResultException;

/**
 * Klasa za obavljanje autentikacije korisnika
 * 
 * @author Marijan Kovač
 *
 */
public class Autentikator {

  /**
   * Obavlja autentikaciju korisnika na temelju korisničkog imena i lozinke
   * 
   * @param korisniciFacade objekt klase KorisniciFacade za upravljanje tablicom Korisnik u bazi
   *        podataka
   * @param korisnik Korisničko ime
   * @param lozinka Korisnička lozinka
   * @throws PogresnaAutentikacija Ukoliko autentikacija nije bila uspješna
   */
  public static void autenticiraj(KorisniciFacade korisniciFacade, String korisnik, String lozinka)
      throws PogresnaAutentikacija {

    Korisnici k = null;

    try {
      k = korisniciFacade.findOne(korisnik);
    } catch (NoResultException e) {
      Logger.getGlobal().log(Level.INFO, "Korisnik ne postoji: " + e.getMessage());
      throw new PogresnaAutentikacija("Korisnik ne postoji!");
    } catch (Exception ex) {
      Logger.getGlobal().log(Level.SEVERE, "Greška u dohvaćanju korisnika: " + ex.getMessage());
      throw new PogresnaAutentikacija("Greška u dohvaćanju podataka o korisniku!");
    }

    if (!k.getLozinka().equals(lozinka)) {
      Logger.getGlobal().log(Level.INFO, "Neispravna lozinka!");
      throw new PogresnaAutentikacija("Neispravna lozinka!");
    }
  }


}
