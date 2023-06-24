package org.foi.nwtis.mkovac.aplikacija_4.ws;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_4.klijenti.RestKlijentAerodromi;
import org.foi.nwtis.mkovac.aplikacija_4.slusaci.WsSlusac;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.ws.soap.SOAPFaultException;

/**
 * Web servis meteo
 * 
 * @author Marijan Kovač
 *
 */
@WebService(serviceName = "meteo")
public class WsMeteo {

  private String OWM_apiKey;

  public WsMeteo() {
    ServletContext context = WsSlusac.getServletContext();
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    OWM_apiKey = konfig.dajPostavku("OpenWeatherMap.apikey");
  }

  @WebMethod(operationName = "dajMeteo")
  public MeteoPodaci dajMeteo(@WebParam String icao) throws SOAPException {
    RestKlijentAerodromi rka = new RestKlijentAerodromi();

    Aerodrom aerodrom = rka.dajAerodrom(icao);

    OWMKlijent owmKlijent = new OWMKlijent(OWM_apiKey);

    MeteoPodaci mp = null;

    try {
      mp = owmKlijent.getRealTimeWeather(aerodrom.getLokacija().getLatitude(),
          aerodrom.getLokacija().getLongitude());
    } catch (NwtisRestIznimka e) {
      String greska = e.getMessage();
      Logger.getGlobal().log(Level.SEVERE, greska);
      SOAPFault soapFault = SOAPFactory.newInstance().createFault();
      soapFault.setFaultString(greska);
      throw new SOAPFaultException(soapFault);
    }

    return mp;
  }

}
