package org.foi.nwtis.mkovac.aplikacija_5.pomocnici;

import org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.Korisnici;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.Korisnik;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.SOAPException_Exception;
import lombok.Getter;
import lombok.Setter;

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
