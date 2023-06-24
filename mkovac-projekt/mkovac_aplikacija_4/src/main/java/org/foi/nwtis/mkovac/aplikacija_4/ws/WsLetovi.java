package org.foi.nwtis.mkovac.aplikacija_4.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.LetoviPolasci;
import org.foi.nwtis.mkovac.aplikacija_4.pomocnici.Autentikator;
import org.foi.nwtis.mkovac.aplikacija_4.pomocnici.UpraviteljDatuma;
import org.foi.nwtis.mkovac.aplikacija_4.slusaci.WsSlusac;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.LetoviPolasciFacade;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.klijenti.OSKlijentBP;
import org.foi.nwtis.rest.podaci.LetAviona;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.ws.soap.SOAPFaultException;

/**
 * Web servis letovi
 * 
 * @author Marijan Kovač
 *
 */
@WebService(serviceName = "letovi")
public class WsLetovi {

  @Inject
  KorisniciFacade korisniciFacade;

  @Inject
  LetoviPolasciFacade letoviPolasciFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  private String osKorisnik;
  private String osLozinka;
  private String bpKorisnik;
  private String bpLozinka;
  private String klijent;

  public WsLetovi() {
    ServletContext context = WsSlusac.getServletContext();
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    this.osKorisnik = konfig.dajPostavku("OpenSkyNetwork.korisnik");
    this.osLozinka = konfig.dajPostavku("OpenSkyNetwork.lozinka");
    this.bpKorisnik = konfig.dajPostavku("OSKlijentBP.korisnik");
    this.bpLozinka = konfig.dajPostavku("OSKlijentBP.lozinka");
    this.klijent = konfig.dajPostavku("preuzimanje.klijent");
  }

  @WebMethod(operationName = "dajPolaskeInterval")
  public List<LetAviona> dajPolaskeInterval(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao, @WebParam String danOd, @WebParam String danDo, @WebParam int odBroja,
      @WebParam int broj) throws SOAPException {

    try {
      Autentikator.autenticiraj(korisniciFacade, korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(e.getMessage());
      throw new SOAPFaultException(soapFault);
    }

    if (!UpraviteljDatuma.provjeriFormatDatuma(danOd)
        || !UpraviteljDatuma.provjeriFormatDatuma(danDo)) {
      String greska = "Dan nije u formatu dd.mm.gggg";
      Logger.getGlobal().log(Level.SEVERE, greska);
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(greska);
      throw new SOAPFaultException(soapFault);
    }

    long pocetniDan = UpraviteljDatuma.konvertirajDan(danOd);
    long zavrsniDan = UpraviteljDatuma.konvertirajDan(danDo);

    if (odBroja < 1 || broj < 1)
      return null;

    List<LetoviPolasci> letoviPolasci =
        letoviPolasciFacade.findAll(icao, pocetniDan, zavrsniDan, odBroja - 1, broj);
    List<LetAviona> letoviAviona = new ArrayList<>();

    if (letoviPolasci != null) {
      for (LetoviPolasci lp : letoviPolasci) {
        LetAviona letAviona = new LetAviona();

        letAviona.setArrivalAirportCandidatesCount(lp.getArrivalAirportCandidatesCount());
        letAviona.setCallsign(lp.getCallsign());
        letAviona.setDepartureAirportCandidatesCount(lp.getDepartureAirportCandidatesCount());
        letAviona.setEstArrivalAirport(lp.getEstArrivalAirport());
        letAviona.setEstArrivalAirportHorizDistance(lp.getEstArrivalAirportHorizDistance());
        letAviona.setEstArrivalAirportVertDistance(lp.getEstArrivalAirportVertDistance());
        letAviona.setEstDepartureAirportHorizDistance(lp.getEstDepartureAirportHorizDistance());
        letAviona.setEstDepartureAirportVertDistance(lp.getEstDepartureAirportVertDistance());
        letAviona.setFirstSeen(lp.getFirstSeen());
        letAviona.setIcao24(lp.getIcao24());
        letAviona.setLastSeen(lp.getLastSeen());

        letoviAviona.add(letAviona);
      }
    }

    return letoviAviona;
  }

  @WebMethod(operationName = "dajPolaskeNaDan")
  public List<LetAviona> dajPolaskeNaDan(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao, @WebParam String dan, @WebParam int odBroja, @WebParam int broj)
      throws SOAPException {

    try {
      Autentikator.autenticiraj(korisniciFacade, korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(e.getMessage());
      throw new SOAPFaultException(soapFault);
    }

    if (!UpraviteljDatuma.provjeriFormatDatuma(dan)) {
      String greska = "Dan nije u formatu dd.mm.gggg";
      Logger.getGlobal().log(Level.SEVERE, greska);
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(greska);
      throw new SOAPFaultException(soapFault);
    }

    long pocetniDan = UpraviteljDatuma.konvertirajDan(dan);
    long zavrsniDan = UpraviteljDatuma.dodajDan(pocetniDan);

    if (odBroja < 1 || broj < 1)
      return null;

    List<LetoviPolasci> letoviPolasci =
        letoviPolasciFacade.findAll(icao, pocetniDan, zavrsniDan, odBroja - 1, broj);
    List<LetAviona> letoviAviona = new ArrayList<>();

    if (letoviPolasci != null) {
      for (LetoviPolasci lp : letoviPolasci) {
        LetAviona letAviona = new LetAviona();

        letAviona.setArrivalAirportCandidatesCount(lp.getArrivalAirportCandidatesCount());
        letAviona.setCallsign(lp.getCallsign());
        letAviona.setDepartureAirportCandidatesCount(lp.getDepartureAirportCandidatesCount());
        letAviona.setEstArrivalAirport(lp.getEstArrivalAirport());
        letAviona.setEstArrivalAirportHorizDistance(lp.getEstArrivalAirportHorizDistance());
        letAviona.setEstArrivalAirportVertDistance(lp.getEstArrivalAirportVertDistance());
        letAviona.setEstDepartureAirportHorizDistance(lp.getEstDepartureAirportHorizDistance());
        letAviona.setEstDepartureAirportVertDistance(lp.getEstDepartureAirportVertDistance());
        letAviona.setFirstSeen(lp.getFirstSeen());
        letAviona.setIcao24(lp.getIcao24());
        letAviona.setLastSeen(lp.getLastSeen());

        letoviAviona.add(letAviona);
      }
    }

    return letoviAviona;
  }

  @WebMethod(operationName = "dajPolaskeNaDanOS")
  public List<LetAviona> dajPolaskeNaDanOS(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao, @WebParam String dan) throws SOAPException {

    try {
      Autentikator.autenticiraj(korisniciFacade, korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(e.getMessage());
      throw new SOAPFaultException(soapFault);
    }

    if (!UpraviteljDatuma.provjeriFormatDatuma(dan)) {
      String greska = "Dan nije u formatu dd.mm.gggg";
      Logger.getGlobal().log(Level.SEVERE, greska);
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(greska);
      throw new SOAPFaultException(soapFault);
    }

    long pocetniDan = UpraviteljDatuma.konvertirajDan(dan);
    long zavrsniDan = UpraviteljDatuma.dodajDan(pocetniDan);

    OSKlijent osKlijent = null;
    OSKlijentBP osKlijentBP = null;

    List<LetAviona> letoviAviona = null;

    if (klijent.equals("OSKlijent")) {
      osKlijent = new OSKlijent(osKorisnik, osLozinka);

      try {
        letoviAviona = osKlijent.getDepartures(icao, pocetniDan, zavrsniDan);
      } catch (NwtisRestIznimka e) {
        String greska = e.getMessage();
        Logger.getGlobal().log(Level.SEVERE, greska);
        SOAPFault soapFault = SOAPFactory.newInstance().createFault();
        soapFault.setFaultString(greska);
        throw new SOAPFaultException(soapFault);
      }

    } else if (klijent.equals("OSKlijentBP")) {
      osKlijentBP = new OSKlijentBP(bpKorisnik, bpLozinka);

      try {
        letoviAviona = osKlijentBP.getDepartures(icao, pocetniDan, zavrsniDan);
      } catch (NwtisRestIznimka e) {
        String greska = e.getMessage();
        Logger.getGlobal().log(Level.SEVERE, greska);
        SOAPFault soapFault = SOAPFactory.newInstance().createFault();
        soapFault.setFaultString(greska);
        throw new SOAPFaultException(soapFault);
      }
    } else {
      String greska = "Nepoznat klijent preuzimanja podataka o polascima";
      Logger.getGlobal().log(Level.SEVERE, greska);
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(greska);
      throw new SOAPFaultException(soapFault);
    }

    return letoviAviona;
  }
}
