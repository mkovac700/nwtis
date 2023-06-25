package org.foi.nwtis.mkovac.aplikacija_5.klijenti;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
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
   * Šalje komandu prema AP1 i vraća odgovor (status).
   * 
   * @param komanda Komanda koja se šalje (KRAJ | INIT | PAUZA)
   * @return Status poslužitelja
   */
  public Status dajKomandu(String komanda) {
    RestKlijent rk = new RestKlijent();
    Status status = null;
    try {
      status = rk.dajKomandu(komanda);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rk.close();
    return status;
  }

  /**
   * Šalje komandu INFO s vrstom prema AP1 i vraća odgovor (status).
   * 
   * @param vrsta Vrsta komande INFO koja se šalje (DA | NE)
   * @return Status poslužitelja
   */
  public Status dajInfo(String vrsta) {
    RestKlijent rk = new RestKlijent();
    Status status = null;
    try {
      status = rk.dajKomandu(vrsta);
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

      String zahtjev = request.get(String.class);

      if (zahtjev.isEmpty())
        return null;

      Gson gson = new Gson();
      Status status = null;
      try {
        status = gson.fromJson(zahtjev, Status.class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return status;
    }

    /**
     * Šalje komandu prema poslužitelju AP1.
     * 
     * @param komanda Komanda koja se šalje
     * @return Status poslužitelja
     * @throws ClientErrorException Ukoliko povezivanje na AP2 nije bilo uspješno.
     */
    public Status dajKomandu(String komanda) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.path(MessageFormat.format("{0}", new Object[] {komanda}));

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      String zahtjev = request.get(String.class);

      if (zahtjev.isEmpty())
        return null;

      Gson gson = new Gson();
      Status status = null;
      try {
        status = gson.fromJson(zahtjev, Status.class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return status;
    }

    /**
     * Šalje komandu INFO prema poslužitelju AP1.
     * 
     * @param vrsta Vrsta koja se šalje
     * @return Status poslužitelja
     * @throws ClientErrorException Ukoliko povezivanje na AP2 nije bilo uspješno.
     */
    public Status dajInfo(String vrsta) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.path(MessageFormat.format("INFO/{0}", new Object[] {vrsta}));

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      String zahtjev = request.get(String.class);

      if (zahtjev.isEmpty())
        return null;

      Gson gson = new Gson();
      Status status = null;
      try {
        status = gson.fromJson(zahtjev, Status.class);
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
