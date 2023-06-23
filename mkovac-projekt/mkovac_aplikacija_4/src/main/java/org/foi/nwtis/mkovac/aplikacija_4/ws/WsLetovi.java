package org.foi.nwtis.mkovac.aplikacija_4.ws;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.mkovac.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.LetoviPolasci;
import org.foi.nwtis.mkovac.aplikacija_4.pomocnici.Autentikator;
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

  private final String regexDatum =
      "(0[1-9]|[1-2][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19[7-9][0-9]|20[0-2][0-9]|203[0-7])";

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

    if (!provjeriFormatDatuma(danOd) || !provjeriFormatDatuma(danDo)) {
      String greska = "Dan nije u formatu dd.mm.gggg";
      Logger.getGlobal().log(Level.SEVERE, greska);
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(greska);
      throw new SOAPFaultException(soapFault);
    }

    long pocetniDan = konvertirajDan(danOd);
    long zavrsniDan = konvertirajDan(danDo);

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

  /**
   * Konvertira datum u formatu dd.MM.yyyy u vrijeme u sekundama proteklo od 1.1.1970. (eng. epoch
   * time)
   * 
   * @param dan Datum u formatu dd.MM.yyyy
   * @return Vrijeme proteklo u sekundama od 1.1.1970.
   */
  private long konvertirajDan(String dan) {
    long epochTime;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate date = LocalDate.parse(dan, dtf);
    ZonedDateTime dateTime = date.atStartOfDay(ZoneId.systemDefault());

    epochTime = dateTime.toInstant().getEpochSecond();

    return epochTime;
  }

  /**
   * Provjerava ispravnost formata datuma
   * 
   * @param datum Zadani datum
   * @return true ako je u redu, inače false
   */
  private boolean provjeriFormatDatuma(String datum) {
    return provjeriIzraz(datum, regexDatum);
  }

  /**
   * Provjerava ispravnost danog izraza koristeći regularne izraze
   * 
   * @param izraz Izraz koji se provjerava
   * @param regex Regularni izraz s kojim se provjerava
   * @return true ako je u redu, inače false
   */
  private boolean provjeriIzraz(String izraz, String regex) {
    String s = izraz.trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }
}
