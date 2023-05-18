package org.foi.nwtis.mkovac.zadaca_3.ws;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_3.slusaci.WsSlusac;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;

@WebService(serviceName = "meteo")
public class WsMeteo {
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
  public void dajMeteoAdresa(@WebParam String adresa) {
    // TODO dodati inject za AirportFacade, kako bi se dohvatio Aerodrom tj. lokacija
    LIQKlijent liqKlijent = new LIQKlijent(LIQ_apiKey);
    try {
      liqKlijent.getGeoLocation(adresa);
    } catch (NwtisRestIznimka e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
