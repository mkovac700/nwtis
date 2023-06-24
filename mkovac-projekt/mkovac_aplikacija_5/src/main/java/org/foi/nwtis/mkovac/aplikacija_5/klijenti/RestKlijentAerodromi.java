package org.foi.nwtis.mkovac.aplikacija_5.klijenti;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.podaci.Aerodrom;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 * REST klijent servisa aerodromi
 * 
 * @author Marijan Kovač
 *
 */
public class RestKlijentAerodromi {

  private static String adresa_AP2;

  public RestKlijentAerodromi() {
    ServletContext context = WsSlusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");
    adresa_AP2 = konf.dajPostavku("adresa.AP2");
  }

  /**
   * Dohvaća podatak o zadanom aerodromu.
   * 
   * @param icao Oznaka aerodroma
   * @return Vraća jedan podatak
   */
  public Aerodrom dajAerodrom(String icao) {
    RestKlijent rk = new RestKlijent();
    Aerodrom k = null;
    try {
      k = rk.dajAerodrom(icao);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rk.close();
    return k;
  }

  /**
   * Interna klasa za rad s klijentom REST servisa. Odrađuje slanje odgovarajućih zahtjeva prema
   * REST servisu.
   * 
   * @author Marijan Kovač
   *
   */
  static class RestKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = adresa_AP2;

    /**
     * Kreira instancu klijenta REST servisa i vezu prema REST servisu
     */
    public RestKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    /**
     * Dohvaća podatak o zadanom aerodromu.
     * 
     * @param icao Oznaka aerodroma
     * @return Vraća jedan podatak
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public Aerodrom dajAerodrom(String icao) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom aerodrom = null;
      try {
        aerodrom = gson.fromJson(request.get(String.class), Aerodrom.class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }
      return aerodrom;
    }

    /**
     * Zatvara klijent REST servisa
     */
    public void close() {
      client.close();
    }
  }

}
