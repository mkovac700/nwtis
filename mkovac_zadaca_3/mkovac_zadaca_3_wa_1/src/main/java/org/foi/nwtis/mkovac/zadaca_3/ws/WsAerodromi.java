package org.foi.nwtis.mkovac.zadaca_3.ws;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.mkovac.zadaca_3.jpa.Airports;
import org.foi.nwtis.mkovac.zadaca_3.zrna.AirportFacade;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzavaKlasa;
import org.foi.nwtis.podaci.UdaljenostAerodromKlasa;
import org.foi.nwtis.podaci.UdaljenostKlasa;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

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

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Dohvaća sve aerodrome u zadanom rasponu.
   * 
   * @param odBroja redni broj od kojeg se dohvaćaju podaci
   * @param broj koliko podataka se želi dohvatiti
   * @return Kolekcija aerodroma
   */
  @WebMethod
  public List<Aerodrom> dajSveAerodrome(@WebParam int odBroja, @WebParam int broj) {

    if (odBroja < 1 || broj < 1)
      return null;

    List<Aerodrom> aerodromi = new ArrayList<>();
    List<Airports> airports = airportFacade.findAll(odBroja - 1, broj);

    String[] koord = null;
    Lokacija lokacija = null;
    for (Airports a : airports) {
      koord = a.getCoordinates().split(",");
      lokacija = new Lokacija(koord[1], koord[0]);
      aerodromi.add(new Aerodrom(a.getIcao(), a.getName(), a.getIsoCountry(), lokacija));
    }

    return aerodromi;
  }

  /**
   * Dohvaća jedan aerodrom
   * 
   * @param icao Oznaka aerodroma
   * @return Jedan aerodrom
   */
  @WebMethod
  public Aerodrom dajAerodrom(@WebParam String icao) {
    Aerodrom aerodrom = null;
    if (icao == null || icao.trim().length() == 0) {
      return aerodrom;
    }
    Airports a = airportFacade.find(icao);
    if (a != null) {
      var koord = a.getCoordinates().split(",");
      var lokacija = new Lokacija(koord[1], koord[0]);
      aerodrom = new Aerodrom(a.getIcao(), a.getName(), a.getIsoCountry(), lokacija);
    }
    return aerodrom;
  }

  /**
   * Vraća udaljenosti između dva aerodroma
   * 
   * @param icaoOd Oznaka polaznog aerodroma
   * @param icaoDo Oznaka dolaznog aerodroma
   * @return Kolekcija podataka o udaljenosti
   */
  @WebMethod
  public List<UdaljenostKlasa> dajUdaljenostiAerodroma(@WebParam String icaoOd,
      @WebParam String icaoDo) {
    if ((icaoOd == null || icaoOd.trim().length() == 0)
        || (icaoDo == null || icaoDo.trim().length() == 0))
      return null;

    var udaljenosti = new ArrayList<UdaljenostKlasa>();
    var distances = airportFacade.findDistances(icaoOd, icaoDo);

    for (Object[] o : distances) {
      udaljenosti.add(new UdaljenostKlasa(o[1].toString(), Float.parseFloat(o[0].toString())));
    }

    return udaljenosti;
  }

  /**
   * Vraća udaljenosti do svih aerodroma od zadanog aerodroma
   * 
   * @param icao Oznaka aerodroma
   * @param odBroja redni broj od kojeg se dohvaćaju podaci
   * @param broj koliko podataka se želi dohvatiti
   * @return Kolekcija podataka o udaljenosti
   */
  @WebMethod
  public List<UdaljenostAerodromKlasa> dajSveUdaljenostiAerodroma(@WebParam String icao,
      @WebParam int odBroja, @WebParam int broj) {
    if (icao == null || icao.trim().length() == 0 || odBroja < 1 || broj < 1)
      return null;

    var udaljenosti = new ArrayList<UdaljenostAerodromKlasa>();
    var distances = airportFacade.findDistances(icao, odBroja - 1, broj);

    for (Object[] o : distances) {
      udaljenosti
          .add(new UdaljenostAerodromKlasa(o[1].toString(), Float.parseFloat(o[0].toString())));
    }

    return udaljenosti;
  }

  /**
   * Vraća podatak o najduljem putu unutar neke države od zadanog aerodroma
   * 
   * @param icao Oznaka aerodroma
   * @return Podatci o udaljenosti unutar neke države
   */
  @WebMethod
  public UdaljenostAerodromDrzavaKlasa dajNajduljiPutDrzave(@WebParam String icao) {
    if (icao == null || icao.trim().length() == 0)
      return null;

    UdaljenostAerodromDrzavaKlasa udaljenost = null;
    var distances = airportFacade.findDistances(icao);

    if (distances != null)
      udaljenost = new UdaljenostAerodromDrzavaKlasa(distances[0].toString(),
          distances[1].toString(), Float.parseFloat(distances[2].toString()));

    return udaljenost;
  }
}
