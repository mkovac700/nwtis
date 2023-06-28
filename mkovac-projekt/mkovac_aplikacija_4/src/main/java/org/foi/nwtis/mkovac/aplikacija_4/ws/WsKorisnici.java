package org.foi.nwtis.mkovac.aplikacija_4.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.mkovac.aplikacija_4.pomocnici.Autentikator;
import org.foi.nwtis.mkovac.aplikacija_4.web.WsInfo;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.podaci.Korisnik;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
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
  KorisniciFacade korisniciFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Dohvaća sve korisnike
   * 
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @param traziImeKorisnika Ime korisnika za koje se pretražuje
   * @param traziPrezimeKorisnika Prezime korisnika za koje se pretražuje
   * @return Vraća kolekciju korisnika
   * @throws SOAPException Ako se dogodila pogreška autentikacije korisnika ili neka neočekivana
   *         pogreška
   */
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

    korisnici.forEach(k -> _korisnici
        .add(new Korisnik(k.getKorisnik(), k.getLozinka(), k.getIme(), k.getPrezime())));

    return _korisnici;
  }

  /**
   * Dohvaća korisnika
   * 
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @param traziKorisnika Korisnik koji se traži (korisničko ime)
   * @return Vraća jednog korisnika
   * @throws SOAPException Ako se dogodila pogreška autentikacije korisnika ili neka neočekivana
   *         pogreška
   */
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
      // } catch (NoResultException e) {
      // Logger.getGlobal().log(Level.INFO, "Korisnik ne postoji: " + e.getMessage());
      // return null;
    } catch (Exception ex) {
      Logger.getGlobal().log(Level.SEVERE, "Greška u dohvaćanju korisnika: " + ex.getMessage());
      return null;
    }

    Korisnik _k = new Korisnik(k.getKorisnik(), k.getLozinka(), k.getIme(), k.getPrezime());

    return _k;
  }

  /**
   * Dodaje novog korisnika. Nakon dodavanja šalje obavijest putem WebSocketa
   * 
   * @param korisnik Objekt korisnika koji se dodaje
   * @return True ako je u redu, inače false
   */
  @WebMethod(operationName = "dodajKorisnika")
  public boolean dodajKorisnika(@WebParam Korisnik korisnik) {

    if (korisnik != null) {
      Korisnici _korisnik = new Korisnici();
      _korisnik.setIme(korisnik.getIme());
      _korisnik.setPrezime(korisnik.getPrezime());
      _korisnik.setKorisnik(korisnik.getKorisnik());
      _korisnik.setLozinka(korisnik.getLozinka());

      korisniciFacade.create(_korisnik);

      int brojKorisnika = korisniciFacade.count();

      WsInfo.posaljiObavijest("Trenutni broj korisnika: " + brojKorisnika);

      return true;
    }

    return false;

  }
}
