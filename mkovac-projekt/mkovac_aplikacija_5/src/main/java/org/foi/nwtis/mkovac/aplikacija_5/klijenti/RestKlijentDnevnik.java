package org.foi.nwtis.mkovac.aplikacija_5.klijenti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.podaci.Dnevnik;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

  /**
   * Dodaje novi zapis u dnevnik
   * 
   * @param json_Zapis JSON zapis dnevnika
   * @return Rezultat spremanja
   */
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
   * Dohvaća zapise iz dnevnika
   * 
   * @param vrsta Vrsta zahtjeva
   * @param odBroja Od kojeg podataka se želi dohvatiti
   * @param broj Koliko podataka se želi dohvatiti
   * @return Kolekcija (lista) podataka
   */
  public List<Dnevnik> dajDnevnik(String vrsta, int odBroja, int broj) {
    RestKlijent rk = new RestKlijent();
    Dnevnik[] json_Dnevnik = null;
    try {
      json_Dnevnik = rk.dajDnevnik(vrsta, odBroja, broj);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<Dnevnik> dnevnik;
    if (json_Dnevnik == null) {
      dnevnik = new ArrayList<>();
    } else {
      dnevnik = Arrays.asList(json_Dnevnik);
    }
    rk.close();
    return dnevnik;
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
     * Dohvaća zapise iz dnevnika
     * 
     * @param vrsta Vrsta zahtjeva
     * @param odBroja Od kojeg podataka se želi dohvatiti
     * @param broj Koliko podataka se želi dohvatiti
     * @return Kolekcija (lista) podataka
     */
    public Dnevnik[] dajDnevnik(String vrsta, int odBroja, int broj) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.queryParam("vrsta", vrsta).queryParam("odBroja", odBroja)
          .queryParam("broj", broj);

      System.out.println("putanja: " + resource.toString());

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      String odgovor = request.get(String.class);

      System.out.println("odgovor: " + odgovor);

      if (odgovor.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Dnevnik[] dnevnik = null;
      try {
        dnevnik = gson.fromJson(odgovor, Dnevnik[].class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return dnevnik;
    }

    /**
     * Zatvara klijent REST servisa
     */
    public void close() {
      client.close();
    }
  }
}
