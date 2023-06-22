package org.foi.nwtis.mkovac.aplikacija_4.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.mkovac.aplikacija_4.pomocnici.Autentikator;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.AirportFacade;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.podaci.Korisnik;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.persistence.NoResultException;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.ws.soap.SOAPFaultException;

/**
 * Web servis korisnici
 * 
 * @author Marijan Kovač
 *
 */
@WebService(serviceName = "korisnici")
public class WsKorisnici {

  @Inject
  AirportFacade airportFacade;

  @Inject
  KorisniciFacade korisniciFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @WebMethod(operationName = "dajKorisnike")
  public List<Korisnik> dajKorisnike(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String traziImeKorisnika, @WebParam String traziPrezimeKorisnika)
      throws SOAPException {

    try {
      Autentikator.autenticiraj(korisniciFacade, korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(e.getMessage());
      throw new SOAPFaultException(soapFault);
    }

    List<Korisnici> korisnici = korisniciFacade.findAll(traziImeKorisnika, traziPrezimeKorisnika);
    List<Korisnik> _korisnici = new ArrayList<>();

    korisnici
        .forEach(k -> _korisnici.add(new Korisnik(korisnik, lozinka, k.getIme(), k.getPrezime())));

    return _korisnici;
  }

  @WebMethod(operationName = "dajKorisnika")
  public Korisnik dajKorisnika(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String traziKorisnika) throws SOAPException {
    try {
      Autentikator.autenticiraj(korisniciFacade, korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(e.getMessage());
      throw new SOAPFaultException(soapFault);
    }

    Korisnici k;
    try {
      k = korisniciFacade.findOne(korisnik);
    } catch (NoResultException e) {
      Logger.getGlobal().log(Level.INFO, "Korisnik ne postoji: " + e.getMessage());
      return null;
    } catch (Exception ex) {
      Logger.getGlobal().log(Level.SEVERE, "Greška u dohvaćanju korisnika: " + ex.getMessage());
      return null;
    }

    Korisnik _k = new Korisnik(k.getKorisnik(), k.getLozinka(), k.getIme(), k.getPrezime());

    return _k;
  }

  @WebMethod(operationName = "dodajKorisnika")
  public boolean dodajKorisnika(@WebParam Korisnik korisnik) {

    if (korisnik != null) {
      Korisnici _korisnik = new Korisnici();
      _korisnik.setIme(korisnik.getIme());
      _korisnik.setPrezime(korisnik.getPrezime());
      _korisnik.setKorisnik(korisnik.getKorisnik());
      _korisnik.setLozinka(korisnik.getLozinka());

      korisniciFacade.create(_korisnik);

      return true;
    }

    return false;

  }
}
