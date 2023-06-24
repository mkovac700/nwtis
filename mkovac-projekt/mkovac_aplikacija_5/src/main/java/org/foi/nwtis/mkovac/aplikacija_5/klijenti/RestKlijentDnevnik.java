package org.foi.nwtis.mkovac.aplikacija_5.klijenti;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * REST klijent servisa dnevnik
 * 
 * @author Marijan Kovač
 *
 */
public class RestKlijentDnevnik {

  private static String adresa_AP2;

  public RestKlijentDnevnik() {
    ServletContext context = WsSlusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");
    adresa_AP2 = konf.dajPostavku("adresa.AP2");
  }

  public String spremiDnevnik(String json_Zapis) {
    RestKlijent rk = new RestKlijent();
    String odgovor = null;
    try {
      odgovor = rk.spremiDnevnik(json_Zapis);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rk.close();
    return odgovor;
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
      webTarget = client.target(BASE_URI).path("dnevnik");
    }

    /**
     * Šalje zahtjev za spremanje zapisa dnevnika u bazu podataka
     * 
     * @param json_Zapis Podaci o zapisu u JSON formatu
     * @return Vraća poruku o uspjehu akcije
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public String spremiDnevnik(String json_Zapis) throws ClientErrorException {
      WebTarget resource = webTarget;

      Invocation.Builder request = resource.request();
      Response response = request.post(Entity.entity(json_Zapis, MediaType.APPLICATION_JSON));

      String odgovor = null;

      if (response.getStatus() == 200)
        odgovor = response.readEntity(String.class);
      else
        odgovor = response.getStatusInfo().getReasonPhrase();

      return odgovor;
    }

    /**
     * Zatvara klijent REST servisa
     */
    public void close() {
      client.close();
    }
  }
}
