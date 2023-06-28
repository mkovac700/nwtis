package org.foi.nwtis.mkovac.aplikacija_5.pomocnici;

import org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.Korisnici;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.Korisnik;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.SOAPException_Exception;
import lombok.Getter;
import lombok.Setter;

/**
 * Klasa za autentikaciju korisnika
 * 
 * @author Marijan Kovač
 *
 */
public class Autentikator {

  private Korisnici service;

  @Getter
  @Setter
  private String korisnik;
  @Getter
  @Setter
  private String lozinka;

  public Autentikator(Korisnici service) {
    this.service = service;
  }

  /**
   * Šalje zahtjev JAX-WS servisu za autentikaciju
   * 
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @return True ako je u redu, inače false
   * @throws SOAPException_Exception Greška prilikom autentikacije ili druga neočekivana pogreška
   */
  public boolean prijaviKorisnika(String korisnik, String lozinka) throws SOAPException_Exception {
    var port = service.getWsKorisniciPort();
    Korisnik k = port.dajKorisnika(korisnik, lozinka, korisnik);

    if (k != null) {
      this.korisnik = k.getKorisnik();
      this.lozinka = k.getLozinka();

      return true;
    }

    return false;
  }

}
