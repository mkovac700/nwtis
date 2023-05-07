package org.foi.nwtis.mkovac.zadaca_2.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.MvcAplikacijaSlusac;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzava;
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
 * REST klijent aerodroma
 * 
 * @author Marijan Kovač
 *
 */
public class RestKlijentAerodroma {

  private ServletContext context;
  private Konfiguracija konf;
  private static String wa_1;

  public RestKlijentAerodroma() {
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
   * Dohvaća podatke o svim aerodromima u zadanom rasponu. Zadani raspon je 1, 20.
   * 
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Vraća kolekciju (listu) podataka
   */
  public List<Aerodrom> dajAerodromi(int odBroja, int broj) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom[] json_Aerodromi = null;
    try {
      json_Aerodromi = rc.dajAerodromi(odBroja, broj);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<Aerodrom> aerodromi;
    if (json_Aerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(json_Aerodromi);
    }
    rc.close();
    return aerodromi;
  }

  /**
   * Dohvaća podatke o svim aerodromima u zadanom rasponu 1, 20.
   * 
   * @return Vraća kolekciju (listu) podataka
   */
  public List<Aerodrom> dajAerodromi() {
    return this.dajAerodromi(1, 20);
  }

  /**
   * Dohvaća podatak o zadanom aerodromu.
   * 
   * @param icao Oznaka aerodroma
   * @return Vraća jedan podatak
   */
  public Aerodrom dajAerodrom(String icao) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom k = null;
    try {
      k = rc.dajAerodrom(icao);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rc.close();
    return k;
  }

  /**
   * Dohvaća podatke o udaljenosti od zadanog aerodroma do svih ostalih aerodroma u zadanom rasponu.
   * Zadani raspon je 1, 20.
   * 
   * @param icao Oznaka aerodroma
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Vraća kolekciju (listu) podataka
   */
  public List<UdaljenostAerodrom> dajAerodromiUdaljenost(String icao, int odBroja, int broj) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodrom[] json_AerodromiUdaljenost = null;
    try {
      json_AerodromiUdaljenost = rc.dajAerodromiUdaljenost(icao, odBroja, broj);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<UdaljenostAerodrom> udaljenostAerodromi;
    if (json_AerodromiUdaljenost == null) {
      udaljenostAerodromi = new ArrayList<>();
    } else {
      udaljenostAerodromi = Arrays.asList(json_AerodromiUdaljenost);
    }
    rc.close();

    return udaljenostAerodromi;
  }

  /**
   * Dohvaća podatak o najduljem putu unutar neke države od zadanog aerodroma.
   * 
   * @param icao Oznaka aerodroma
   * @return Vraća jedan podatak
   */
  public UdaljenostAerodromDrzava dajUdaljenostAerodromDrzava(String icao) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodromDrzava uad = null;
    try {
      uad = rc.dajUdaljenostAerodromDrzava(icao);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rc.close();
    return uad;
  }

  /**
   * Dohvaća podatke o udaljenosti između dva aerodroma.
   * 
   * @param icaoOd Oznaka polaznog aerodroma
   * @param icaoDo Oznaka dolaznog aerodroma
   * @return Vraća kolekciju (listu) podataka
   */
  public List<Udaljenost> dajUdaljenost2Aerodroma(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    Udaljenost[] json_udaljenost2Aerodroma = null;
    try {
      json_udaljenost2Aerodroma = rc.dajUdaljenost2Aerodroma(icaoOd, icaoDo);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<Udaljenost> udaljenost2Aerodroma;
    if (json_udaljenost2Aerodroma == null) {
      udaljenost2Aerodroma = new ArrayList<>();
    } else {
      udaljenost2Aerodroma = Arrays.asList(json_udaljenost2Aerodroma);
    }
    rc.close();

    return udaljenost2Aerodroma;
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
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    /**
     * Dohvaća podatke o svim aerodromima u zadanom rasponu. Zadani raspon je 1, 20.
     * 
     * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
     * @param broj Koliko podataka se želi dohvatiti
     * @return Vraća kolekciju podataka
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public Aerodrom[] dajAerodromi(int odBroja, int broj) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.queryParam("odBroja", odBroja).queryParam("broj", broj);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom[] aerodromi = null;
      try {
        aerodromi = gson.fromJson(request.get(String.class), Aerodrom[].class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return aerodromi;
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
     * Dohvaća podatke o udaljenosti od zadanog aerodroma do svih ostalih aerodroma u zadanom
     * rasponu. Zadani raspon je 1, 20.
     * 
     * @param icao Oznaka aerodroma
     * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
     * @param broj Koliko podataka se želi dohvatiti
     * @return Vraća kolekciju podataka
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public UdaljenostAerodrom[] dajAerodromiUdaljenost(String icao, int odBroja, int broj)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}))
          .path("udaljenosti").queryParam("odBroja", odBroja).queryParam("broj", broj);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenostAerodromi = null;
      try {
        udaljenostAerodromi = gson.fromJson(request.get(String.class), UdaljenostAerodrom[].class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return udaljenostAerodromi;
    }

    /**
     * Dohvaća podatak o najduljem putu unutar neke države od zadanog aerodroma.
     * 
     * @param icao Oznaka aerodroma
     * @return Vraća jedan podatak
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public UdaljenostAerodromDrzava dajUdaljenostAerodromDrzava(String icao)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}))
          .path("najduljiPutDrzave");
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      UdaljenostAerodromDrzava udaljenostAerodromDrzava = null;
      Gson gson = new Gson();
      try {
        udaljenostAerodromDrzava =
            gson.fromJson(request.get(String.class), UdaljenostAerodromDrzava.class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return udaljenostAerodromDrzava;
    }

    /**
     * Dohvaća podatke o udaljenosti između dva aerodroma.
     * 
     * @param icaoOd Oznaka polaznog aerodroma
     * @param icaoDo Oznaka dolaznog aerodroma
     * @return Vraća kolekciju podataka
     * @throws ClientErrorException Ukoliko se dogodi greška u radu REST klijenta
     */
    public Udaljenost[] dajUdaljenost2Aerodroma(String icaoOd, String icaoDo)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      if ((icaoOd == null || icaoDo == null) || (icaoOd.isEmpty() || icaoDo.isEmpty()))
        return null;

      resource =
          resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[] {icaoOd, icaoDo}));

      System.out.println("Putanja: " + resource.toString());
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Udaljenost[] udaljenost2Aerodroma = null;
      try {
        udaljenost2Aerodroma = gson.fromJson(request.get(String.class), Udaljenost[].class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return udaljenost2Aerodroma;
    }

    /**
     * Zatvara klijent REST servisa
     */
    public void close() {
      client.close();
    }
  }

}
