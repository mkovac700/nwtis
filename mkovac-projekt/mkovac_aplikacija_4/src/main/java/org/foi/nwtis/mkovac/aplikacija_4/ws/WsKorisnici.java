package org.foi.nwtis.mkovac.aplikacija_4.ws;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.Airports;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.AirportFacade;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

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

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @WebMethod
  public List<Aerodrom> test(@WebParam int odBroja, @WebParam int broj) {
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
}
