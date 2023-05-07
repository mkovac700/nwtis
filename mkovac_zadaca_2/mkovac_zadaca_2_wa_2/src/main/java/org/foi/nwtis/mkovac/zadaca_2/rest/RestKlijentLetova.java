package org.foi.nwtis.mkovac.zadaca_2.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.MvcAplikacijaSlusac;
import org.foi.nwtis.rest.podaci.LetAviona;
import org.foi.nwtis.rest.podaci.LetAvionaID;
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
 * REST klijent letova
 * 
 * @author Marijan Kovač
 *
 */
public class RestKlijentLetova {

  private ServletContext context;
  private Konfiguracija konf;
  private static String wa_1;

  public RestKlijentLetova() {
    context = MvcAplikacijaSlusac.getServletContext();
    konf = (Konfiguracija) context.getAttribute("konfig");
    wa_1 = konf.dajPostavku("adresa.wa_1");
  }

  /**
   * Dohvaća informacije o autoru i aplikaciji. Infomacije se učitavaju iz konteksta.
   * 
   * @return Vraća polje s podacima
   */
  public String[] dajInfo() {
    String[] info = new String[5];

    info[0] = konf.dajPostavku("autor.ime");
    info[1] = konf.dajPostavku("autor.prezime");
    info[2] = konf.dajPostavku("autor.predmet");
    info[3] = konf.dajPostavku("aplikacija.godina");
    info[4] = konf.dajPostavku("aplikacija.verzija");

    return info;
  }

  /**
   * Dohvaća podatke o letovima sa zadanog aerodroma na određeni dan u zadanom rasponu. Zadani
   * raspon je 1, 20.
   * 
   * @param icao Oznaka aerodroma
   * @param dan Dan u formatu dd.MM.yyyy
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Vraća kolekciju (listu) podataka
   */
  public List<LetAviona> dajLetoveAerodrom(String icao, String dan, int odBroja, int broj) {
    RestKKlijent rc = new RestKKlijent();
    LetAviona[] json_letoviAviona = null;
    try {
      json_letoviAviona = rc.dajLetoveAerodrom(icao, dan, odBroja, broj);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<LetAviona> letoviAviona;
    if (json_letoviAviona == null) {
      letoviAviona = new ArrayList<>();
    } else {
      letoviAviona = Arrays.asList(json_letoviAviona);
    }
    rc.close();

    return letoviAviona;
  }

  /**
   * Dohvaća podatke o letovima sa polazišnog aerodroma do odredišnog aerodroma na određeni dan u
   * zadanom rasponu. Zadani raspon je 1, 20.
   * 
   * @param icaoOd Oznaka polazišnog aerodroma
   * @param icaoDo Oznaka odredišnog aerodroma
   * @param dan Dan u formatu dd.MM.yyyy
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Vraća kolekciju (listu) podataka
   */
  public List<LetAviona> dajLetoveAerodrom(String icaoOd, String icaoDo, String dan, int odBroja,
      int broj) {
    RestKKlijent rc = new RestKKlijent();
    LetAviona[] json_letoviAviona = null;
    try {
      json_letoviAviona = rc.dajLetoveAerodrom(icaoOd, icaoDo, dan, odBroja, broj);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<LetAviona> letoviAviona;
    if (json_letoviAviona == null) {
      letoviAviona = new ArrayList<>();
    } else {
      letoviAviona = Arrays.asList(json_letoviAviona);
    }
    rc.close();

    return letoviAviona;
  }

  /**
   * Dohvaća podatke o spremljenim letovima iz baze podataka
   * 
   * @return Vraća kolekciju (listu) podataka
   */
  public List<LetAvionaID> dajSpremljeneLetove() {
    RestKKlijent rc = new RestKKlijent();
    LetAvionaID[] json_letoviAviona = null;
    try {
      json_letoviAviona = rc.dajSpremljeneLetove();
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<LetAvionaID> letoviAviona;
    if (json_letoviAviona == null) {
      letoviAviona = new ArrayList<>();
    } else {
      letoviAviona = Arrays.asList(json_letoviAviona);
    }
    rc.close();

    return letoviAviona;
  }

  /**
   * Obavlja spremanje odabranog leta u bazu podataka
   * 
   * @param json_LetAviona Podaci o odabranom letu u JSON formatu
   * @return Vraća poruku o uspjehu akcije
   */
  public String spremiLet(String json_LetAviona) {
    RestKKlijent rc = new RestKKlijent();
    String odgovor = null;
    try {
      odgovor = rc.spremiLet(json_LetAviona);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rc.close();
    return odgovor;
  }

  /**
   * Obavlja brisanje odabranog leta iz baze podataka
   * 
   * @param id ID leta koji se želi obrisati
   * @return Vraća poruku o uspjehu akcije
   */
  public String obrisiLet(int id) {
    RestKKlijent rc = new RestKKlijent();
    String odgovor = null;
    try {
      odgovor = rc.obrisiLet(id);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rc.close();
    return odgovor;
  }

  /**
   * Interna klasa za rad s klijentom REST servisa. Odrađuje slanje odgovarajućih zahtjeva prema
   * REST servisu.
   * 
   * @author Marijan Kovač
   *
   */
  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = wa_1;

    /**
     * Kreira instancu klijenta REST servisa i vezu prema REST servisu
     */
    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("letovi");
    }

    /**
     * Dohvaća podatke o letovima sa zadanog aerodroma na određeni dan u zadanom rasponu. Zadani
     * raspon je 1, 20.
     * 
     * @param icao Oznaka aerodroma
     * @param dan Dan u formatu dd.MM.yyyy
     * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
     * @param broj Koliko podataka se želi dohvatiti
     * @return Vraća kolekciju podataka
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public LetAviona[] dajLetoveAerodrom(String icao, String dan, int odBroja, int broj)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      if ((icao == null || icao.isEmpty()))
        return null;

      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}))
          .queryParam("dan", dan).queryParam("odBroja", odBroja).queryParam("broj", broj);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      LetAviona[] letoviAviona = null;
      try {
        letoviAviona = gson.fromJson(request.get(String.class), LetAviona[].class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return letoviAviona;
    }

    /**
     * Dohvaća podatke o letovima sa polazišnog aerodroma do odredišnog aerodroma na određeni dan u
     * zadanom rasponu. Zadani raspon je 1, 20.
     * 
     * @param icaoOd Oznaka polazišnog aerodroma
     * @param icaoDo Oznaka odredišnog aerodroma
     * @param dan Dan u formatu dd.MM.yyyy
     * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
     * @param broj Koliko podataka se želi dohvatiti
     * @return Vraća kolekciju podataka
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public LetAviona[] dajLetoveAerodrom(String icaoOd, String icaoDo, String dan, int odBroja,
        int broj) throws ClientErrorException {

      WebTarget resource = webTarget;

      if ((icaoOd == null || icaoOd.isEmpty()) || (icaoDo == null || icaoDo.isEmpty()))
        return null;

      resource =
          resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[] {icaoOd, icaoDo}))
              .queryParam("dan", dan).queryParam("odBroja", odBroja).queryParam("broj", broj);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      LetAviona[] letoviAviona = null;
      try {
        letoviAviona = gson.fromJson(request.get(String.class), LetAviona[].class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return letoviAviona;

    }

    /**
     * Dohvaća podatke o spremljenim letovima iz baze podataka
     * 
     * @return Vraća kolekciju podataka
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public LetAvionaID[] dajSpremljeneLetove() throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.path("spremljeni");

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      LetAvionaID[] letoviAviona = null;
      try {
        letoviAviona = gson.fromJson(request.get(String.class), LetAvionaID[].class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return letoviAviona;
    }

    /**
     * Šalje zahtjev za spremanje odabranog leta u bazu podataka
     * 
     * @param json_letAviona Podaci o odabranom letu u JSON formatu
     * @return Vraća poruku o uspjehu akcije
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public String spremiLet(String json_letAviona) throws ClientErrorException {
      WebTarget resource = webTarget;

      Invocation.Builder request = resource.request();
      Response response = request.post(Entity.entity(json_letAviona, MediaType.APPLICATION_JSON));

      String odgovor = null;

      if (response.getStatus() == 200)
        odgovor = response.readEntity(String.class);
      else
        odgovor = response.getStatusInfo().getReasonPhrase();

      return odgovor;
    }

    /**
     * Šalje zahtjev za brisanje odabranog leta iz baze podataka
     * 
     * @param id ID leta koji se želi obrisati
     * @return Vraća poruku o uspjehu akcije
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public String obrisiLet(int id) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.path(Integer.toString(id));

      Invocation.Builder request = resource.request();
      Response response = request.delete();

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
