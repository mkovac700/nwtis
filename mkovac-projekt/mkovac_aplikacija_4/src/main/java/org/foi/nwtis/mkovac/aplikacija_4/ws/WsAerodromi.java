package org.foi.nwtis.mkovac.aplikacija_4.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.AerodromiLetovi;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.Airports;
import org.foi.nwtis.mkovac.aplikacija_4.pomocnici.Autentikator;
import org.foi.nwtis.mkovac.aplikacija_4.web.WsInfo;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.AerodromiLetoviFacade;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.AirportFacade;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.AerodromLetovi;
import org.foi.nwtis.podaci.Lokacija;
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
@WebService(serviceName = "aerodromi")
public class WsAerodromi {

  @Inject
  AirportFacade airportFacade;

  @Inject
  KorisniciFacade korisniciFacade;

  @Inject
  AerodromiLetoviFacade aerodromiLetoviFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @WebMethod(operationName = "dajSveAerodromeZaLetove")
  public List<AerodromLetovi> dajSveAerodromeZaLetove(@WebParam String korisnik,
      @WebParam String lozinka) {

    List<AerodromiLetovi> aerodromiLetovi = aerodromiLetoviFacade.findAll(); // (byte) 1
    List<AerodromLetovi> aerodromi = new ArrayList<>();

    String[] koord = null;
    Lokacija lokacija = null;
    for (AerodromiLetovi al : aerodromiLetovi) {
      Airports a = al.getAirport();
      koord = a.getCoordinates().split(",");
      lokacija = new Lokacija(koord[1], koord[0]);
      aerodromi.add(
          new AerodromLetovi(new Aerodrom(a.getIcao(), a.getName(), a.getIsoCountry(), lokacija),
              al.getPreuzimanje()));
    }

    return aerodromi;
  }

  @WebMethod(operationName = "dajAerodromeZaLetove")
  public List<Aerodrom> dajAerodromeZaLetove(@WebParam String korisnik, @WebParam String lozinka) {

    List<AerodromiLetovi> aerodromiLetovi = aerodromiLetoviFacade.findAll(true); // (byte) 1
    List<Aerodrom> aerodromi = new ArrayList<>();

    String[] koord = null;
    Lokacija lokacija = null;
    for (AerodromiLetovi al : aerodromiLetovi) {
      Airports a = al.getAirport();
      koord = a.getCoordinates().split(",");
      lokacija = new Lokacija(koord[1], koord[0]);
      aerodromi.add(new Aerodrom(a.getIcao(), a.getName(), a.getIsoCountry(), lokacija));
    }

    return aerodromi;
  }

  @WebMethod(operationName = "dodajAerodromZaLetove")
  public boolean dodajAerodromZaLetove(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao) throws SOAPException {
    try {
      Autentikator.autenticiraj(korisniciFacade, korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(e.getMessage());
      throw new SOAPFaultException(soapFault);
    }

    Airports airport = airportFacade.find(icao);

    if (airport != null) {
      AerodromiLetovi aerodromiLetovi = new AerodromiLetovi();
      aerodromiLetovi.setAirport(airport);
      aerodromiLetovi.setPreuzimanje(true);// (byte) 1

      aerodromiLetoviFacade.create(aerodromiLetovi);

      int brojAerodroma = aerodromiLetoviFacade.count();

      WsInfo.posaljiObavijest("Trenutni broj aerodroma za preuzimanje: " + brojAerodroma);

      return true;
    }

    return false;
  }

  @WebMethod(operationName = "pauzirajAerodromZaLetove")
  public boolean pauzirajAerodromZaLetove(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao) throws SOAPException {

    try {
      Autentikator.autenticiraj(korisniciFacade, korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(e.getMessage());
      throw new SOAPFaultException(soapFault);
    }

    AerodromiLetovi aerodromiLetovi = null;
    try {
      aerodromiLetovi = aerodromiLetoviFacade.findOne(icao);
    } catch (Exception ex) {
      Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
      return false;
    }

    if (aerodromiLetovi != null && aerodromiLetovi.getPreuzimanje()) {
      aerodromiLetovi.setPreuzimanje(false);
      aerodromiLetoviFacade.edit(aerodromiLetovi);
      return true;
    }

    return false;
  }

  @WebMethod(operationName = "aktivirajAerodromZaLetove")
  public boolean aktivirajAerodromZaLetove(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao) throws SOAPException {

    try {
      Autentikator.autenticiraj(korisniciFacade, korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(e.getMessage());
      throw new SOAPFaultException(soapFault);
    }

    AerodromiLetovi aerodromiLetovi = null;
    try {
      aerodromiLetovi = aerodromiLetoviFacade.findOne(icao);
    } catch (Exception ex) {
      Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
      return false;
    }

    if (aerodromiLetovi != null && !aerodromiLetovi.getPreuzimanje()) {
      aerodromiLetovi.setPreuzimanje(true);
      aerodromiLetoviFacade.edit(aerodromiLetovi);
      return true;
    }

    return false;
  }
}
