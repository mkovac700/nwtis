package org.foi.nwtis.mkovac.aplikacija_3.klijenti;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_3.slusaci.WsSlusac;
import org.foi.nwtis.podaci.Status;
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
 * REST klijent servisa nadzor
 * 
 * @author Marijan Kovač
 *
 */
public class RestKlijentNadzor {

  private static String adresa_AP2;

  public RestKlijentNadzor() {
    ServletContext context = WsSlusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");
    adresa_AP2 = konf.dajPostavku("adresa.AP2");
  }

  /**
   * Dohvaća status poslužitelja AP1.
   * 
   * @return Status poslužitelja
   */
  public Status dajStatus() {
    RestKlijent rk = new RestKlijent();
    Status status = null;
    try {
      status = rk.dajStatus();
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rk.close();
    return status;
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
      webTarget = client.target(BASE_URI).path("nadzor");
    }

    /**
     * Dohvaća status poslužitelja AP1.
     * 
     * @return Status poslužitelja
     * @throws ClientErrorException Ukoliko povezivanje na AP2 nije bilo uspješno.
     */
    public Status dajStatus() throws ClientErrorException {
      WebTarget resource = webTarget;

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      if (request.get(String.class).isEmpty())
        return null;

      Gson gson = new Gson();
      Status status = null;
      try {
        status = gson.fromJson(request.get(String.class), Status.class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return status;
    }

    /**
     * Zatvara klijent REST servisa
     */
    public void close() {
      client.close();
    }
  }

}
