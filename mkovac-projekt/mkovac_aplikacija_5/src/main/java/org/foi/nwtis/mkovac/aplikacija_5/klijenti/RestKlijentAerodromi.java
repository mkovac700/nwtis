package org.foi.nwtis.mkovac.aplikacija_5.klijenti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
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
    Aerodrom a = null;
    try {
      a = rk.dajAerodrom(icao);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rk.close();
    return a;
  }

  /**
   * Dohvaća podatke o svim aerodromima u zadanom rasponu. Zadani raspon je 1, 20.
   * 
   * @param traziNaziv Naziv koji se pretražuje
   * @param traziDrzavu Država koja se pretražuje
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Vraća kolekciju (listu) podataka
   */
  public List<Aerodrom> dajAerodromi(String traziNaziv, String traziDrzavu, int odBroja, int broj) {
    RestKlijent rk = new RestKlijent();
    Aerodrom[] json_Aerodromi = null;
    try {
      json_Aerodromi = rk.dajAerodromi(traziNaziv, traziDrzavu, odBroja, broj);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<Aerodrom> aerodromi;
    if (json_Aerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(json_Aerodromi);
    }
    rk.close();
    return aerodromi;
  }

  /**
   * Dohvaća podatke o udaljenosti dva aerodroma
   * 
   * @param icaoOd Polazni aerodrom
   * @param icaoDo Dolazni aerodrom
   * @return Vraća kolekciju (listu) podataka
   */
  public List<Udaljenost> dajUdaljenosti2Aerodroma(String icaoOd, String icaoDo) {
    RestKlijent rk = new RestKlijent();
    Udaljenost[] json_udaljenost2Aerodroma = null;
    try {
      json_udaljenost2Aerodroma = rk.dajUdaljenosti2Aerodroma(icaoOd, icaoDo);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<Udaljenost> udaljenost2Aerodroma;
    if (json_udaljenost2Aerodroma == null) {
      udaljenost2Aerodroma = new ArrayList<>();
    } else {
      udaljenost2Aerodroma = Arrays.asList(json_udaljenost2Aerodroma);
    }
    rk.close();

    return udaljenost2Aerodroma;
  }

  /**
   * Dohvaća podatak o izračunatoj udaljenosti dva aerodroma
   * 
   * @param icaoOd Polazni aerodrom
   * @param icaoDo Dolazni aerodrom
   * @return Vraća jedan podatak
   */
  public UdaljenostAerodrom dajIzracunUdaljenosti2Aerodroma(String icaoOd, String icaoDo) {
    RestKlijent rk = new RestKlijent();
    UdaljenostAerodrom ua = null;
    try {
      ua = rk.dajIzracunUdaljenosti2Aerodroma(icaoOd, icaoDo);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    rk.close();
    return ua;
  }

  /**
   * Dohvaća podatak o udaljenostima unutar države odredišnog aerodroma koje su manje od udaljenosti
   * od polazišnog do odredišnog aerodroma
   * 
   * @param icaoOd Polazni aerodrom
   * @param icaoDo Dolazni aerodrom
   * @return Vraća kolekciju (listu) podataka
   */
  public List<UdaljenostAerodrom> dajIzracun1Udaljenosti2Aerodroma(String icaoOd, String icaoDo) {
    RestKlijent rk = new RestKlijent();
    UdaljenostAerodrom[] json_udaljenost2Aerodroma = null;
    try {
      json_udaljenost2Aerodroma = rk.dajIzracun1Udaljenosti2Aerodroma(icaoOd, icaoDo);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<UdaljenostAerodrom> udaljenost2Aerodroma;
    if (json_udaljenost2Aerodroma == null) {
      udaljenost2Aerodroma = new ArrayList<>();
    } else {
      udaljenost2Aerodroma = Arrays.asList(json_udaljenost2Aerodroma);
    }
    rk.close();

    return udaljenost2Aerodroma;
  }

  /**
   * Dohvaća podatak o udaljenostima do aerodroma unutar zadane države koja je manja od zadane
   * udaljenosti između polaznog aerodroma i svih aerodroma unutar države
   * 
   * @param icaoOd Polazni aerodrom
   * @param drzava Odredišna država
   * @param km Broj kilometara
   * @return Vraća kolekciju (listu) podataka
   */
  public List<UdaljenostAerodrom> dajIzracun2Udaljenosti2Aerodroma(String icaoOd, String drzava,
      String km) {
    RestKlijent rk = new RestKlijent();
    UdaljenostAerodrom[] json_udaljenost2Aerodroma = null;
    try {
      json_udaljenost2Aerodroma = rk.dajIzracun2Udaljenosti2Aerodroma(icaoOd, drzava, km);
    } catch (ClientErrorException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    List<UdaljenostAerodrom> udaljenost2Aerodroma;
    if (json_udaljenost2Aerodroma == null) {
      udaljenost2Aerodroma = new ArrayList<>();
    } else {
      udaljenost2Aerodroma = Arrays.asList(json_udaljenost2Aerodroma);
    }
    rk.close();

    return udaljenost2Aerodroma;
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
     * Dohvaća podatke o svim aerodromima u zadanom rasponu. Zadani raspon je 1, 20.
     * 
     * @param traziNaziv Naziv koji se pretražuje
     * @param traziDrzavu Država koja se pretražuje
     * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
     * @param broj Koliko podataka se želi dohvatiti
     * @return Vraća kolekciju (listu) podataka
     */
    public Aerodrom[] dajAerodromi(String traziNaziv, String traziDrzavu, int odBroja, int broj)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.queryParam("odBroja", odBroja).queryParam("broj", broj)
          .queryParam("traziNaziv", traziNaziv).queryParam("traziDrzavu", traziDrzavu);

      System.out.println("putanja: " + resource.toString());

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      String odgovor = request.get(String.class);

      System.out.println("odgovor: " + odgovor);

      if (odgovor.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom[] aerodromi = null;
      try {
        aerodromi = gson.fromJson(odgovor, Aerodrom[].class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return aerodromi;
    }

    /**
     * Dohvaća podatke o udaljenosti dva aerodroma
     * 
     * @param icaoOd Polazni aerodrom
     * @param icaoDo Dolazni aerodrom
     * @return Vraća kolekciju (listu) podataka
     */
    public Udaljenost[] dajUdaljenosti2Aerodroma(String icaoOd, String icaoDo)
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
     * Dohvaća podatak o izračunatoj udaljenosti dva aerodroma
     * 
     * @param icaoOd Polazni aerodrom
     * @param icaoDo Dolazni aerodrom
     * @return Vraća jedan podatak
     */
    public UdaljenostAerodrom dajIzracunUdaljenosti2Aerodroma(String icaoOd, String icaoDo)
        throws ClientErrorException {

      WebTarget resource = webTarget;

      if ((icaoOd == null || icaoDo == null) || (icaoOd.isEmpty() || icaoDo.isEmpty()))
        return null;

      resource = resource
          .path(java.text.MessageFormat.format("{0}/izracunaj/{1}", new Object[] {icaoOd, icaoDo}));

      System.out.println("Putanja: " + resource.toString());

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      String odgovor = request.get(String.class);

      System.out.println("odgovor: " + odgovor);

      if (odgovor.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom udaljenost2Aerodroma = null;
      try {
        udaljenost2Aerodroma = gson.fromJson(odgovor, UdaljenostAerodrom.class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return udaljenost2Aerodroma;

    }

    /**
     * Dohvaća podatak o udaljenostima unutar države odredišnog aerodroma koje su manje od
     * udaljenosti od polazišnog do odredišnog aerodroma
     * 
     * @param icaoOd Polazni aerodrom
     * @param icaoDo Dolazni aerodrom
     * @return Vraća kolekciju (listu) podataka
     */
    public UdaljenostAerodrom[] dajIzracun1Udaljenosti2Aerodroma(String icaoOd, String icaoDo)
        throws ClientErrorException {

      WebTarget resource = webTarget;

      if ((icaoOd == null || icaoDo == null) || (icaoOd.isEmpty() || icaoDo.isEmpty()))
        return null;

      resource = resource.path(
          java.text.MessageFormat.format("{0}/udaljenost1/{1}", new Object[] {icaoOd, icaoDo}));

      System.out.println("Putanja: " + resource.toString());

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      String odgovor = request.get(String.class);

      System.out.println("odgovor: " + odgovor);

      if (odgovor.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenost2Aerodroma = null;
      try {
        udaljenost2Aerodroma = gson.fromJson(odgovor, UdaljenostAerodrom[].class);
      } catch (JsonSyntaxException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

      return udaljenost2Aerodroma;
    }

    /**
     * Dohvaća podatak o udaljenostima do aerodroma unutar zadane države koja je manja od zadane
     * udaljenosti između polaznog aerodroma i svih aerodroma unutar države
     * 
     * @param icaoOd Polazni aerodrom
     * @param drzava Odredišna država
     * @param km Broj kilometara
     * @return Vraća kolekciju (listu) podataka
     */
    public UdaljenostAerodrom[] dajIzracun2Udaljenosti2Aerodroma(String icaoOd, String drzava,
        String km) throws ClientErrorException {

      WebTarget resource = webTarget;

      if ((icaoOd == null || icaoOd == null) || (drzava.isEmpty() || drzava.isEmpty())
          || (km.isEmpty() || km.isEmpty()))
        return null;

      resource =
          resource.path(java.text.MessageFormat.format("{0}/udaljenost2", new Object[] {icaoOd}));

      resource = resource.queryParam("drzava", drzava).queryParam("km", km);

      System.out.println("Putanja: " + resource.toString());

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      String odgovor = request.get(String.class);

      System.out.println("odgovor: " + odgovor);

      if (odgovor.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenost2Aerodroma = null;
      try {
        udaljenost2Aerodroma = gson.fromJson(odgovor, UdaljenostAerodrom[].class);
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
