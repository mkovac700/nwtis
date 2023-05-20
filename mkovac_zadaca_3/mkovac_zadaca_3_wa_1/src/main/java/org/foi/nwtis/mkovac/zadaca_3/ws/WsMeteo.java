package org.foi.nwtis.mkovac.zadaca_3.ws;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_3.jpa.Airports;
import org.foi.nwtis.mkovac.zadaca_3.slusaci.WsSlusac;
import org.foi.nwtis.mkovac.zadaca_3.zrna.AirportFacade;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;

@WebService(serviceName = "meteo")
public class WsMeteo {

  @Inject
  AirportFacade airportFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  private String OWM_apiKey;
  private String LIQ_apiKey;

  public WsMeteo() {
    ServletContext context = WsSlusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");

    this.OWM_apiKey = konf.dajPostavku("OpenWeatherMap.apikey");
    this.LIQ_apiKey = konf.dajPostavku("LocationIQ.apikey");
  }

  @WebMethod
  public MeteoPodaci dajMeteoAdresa(@WebParam String adresa) {

    if (adresa == null || adresa.trim().length() == 0)
      return null;

    LIQKlijent liqKlijent = new LIQKlijent(LIQ_apiKey);
    Lokacija lokacija = null;
    try {
      lokacija = liqKlijent.getGeoLocation(adresa);
    } catch (NwtisRestIznimka e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    OWMKlijent owmKlijent = new OWMKlijent(OWM_apiKey);
    MeteoPodaci mp = null;
    try {
      mp = owmKlijent.getRealTimeWeather(lokacija.getLatitude(), lokacija.getLongitude());
    } catch (NwtisRestIznimka e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return mp;
  }

  @WebMethod
  public MeteoPodaci dajMeteo(@WebParam String icao) {
    if (icao == null || icao.trim().length() == 0)
      return null;

    Airports a = airportFacade.find(icao);
    var koord = a.getCoordinates().split(",");
    var lokacija = new Lokacija(koord[1], koord[0]);

    OWMKlijent owmKlijent = new OWMKlijent(OWM_apiKey);
    MeteoPodaci mp = null;
    try {
      mp = owmKlijent.getRealTimeWeather(lokacija.getLatitude(), lokacija.getLongitude());
    } catch (NwtisRestIznimka e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return mp;
  }
}
