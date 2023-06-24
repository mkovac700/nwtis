package org.foi.nwtis.mkovac.aplikacija_4.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.LetoviPolasci;
import org.foi.nwtis.mkovac.aplikacija_4.pomocnici.Autentikator;
import org.foi.nwtis.mkovac.aplikacija_4.pomocnici.DatumskiUpravitelj;
import org.foi.nwtis.mkovac.aplikacija_4.pomocnici.RegexUpravitelj;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.LetoviPolasciFacade;
import org.foi.nwtis.rest.podaci.LetAviona;
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
 * Web servis aerodromi
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

    if (!RegexUpravitelj.provjeriFormatDatuma(danOd)
        || !RegexUpravitelj.provjeriFormatDatuma(danDo)) {
      String greska = "Dan nije u formatu dd.mm.gggg";
      Logger.getGlobal().log(Level.SEVERE, greska);
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(greska);
      throw new SOAPFaultException(soapFault);
    }

    long pocetniDan = DatumskiUpravitelj.konvertirajDan(danOd);
    long zavrsniDan = DatumskiUpravitelj.konvertirajDan(danDo);

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

    if (!RegexUpravitelj.provjeriFormatDatuma(dan)) {
      String greska = "Dan nije u formatu dd.mm.gggg";
      Logger.getGlobal().log(Level.SEVERE, greska);
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(greska);
      throw new SOAPFaultException(soapFault);
    }

    long pocetniDan = DatumskiUpravitelj.konvertirajDan(dan);
    long zavrsniDan = DatumskiUpravitelj.dodajDan(pocetniDan);

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
}
