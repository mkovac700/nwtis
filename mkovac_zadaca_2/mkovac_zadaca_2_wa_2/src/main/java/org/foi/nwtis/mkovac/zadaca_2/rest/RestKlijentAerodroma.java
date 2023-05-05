package org.foi.nwtis.mkovac.zadaca_2.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

public class RestKlijentAerodroma {

  private ServletContext context;
  private Konfiguracija konf;
  private static String wa_1;

  public RestKlijentAerodroma() {
    context = MvcAplikacijaSlusac.getServletContext();
    konf = (Konfiguracija) context.getAttribute("konfig");
    wa_1 = konf.dajPostavku("adresa.wa_1");
  }

  public String[] getInfo() {
    String[] info = new String[5];

    info[0] = konf.dajPostavku("autor.ime");
    info[1] = konf.dajPostavku("autor.prezime");
    info[2] = konf.dajPostavku("autor.predmet");
    info[3] = konf.dajPostavku("aplikacija.godina");
    info[4] = konf.dajPostavku("aplikacija.verzija");

    return info;
  }

  public List<Aerodrom> getAerodromi(int odBroja, int broj) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom[] json_Aerodromi = rc.getAerodromi(odBroja, broj);
    List<Aerodrom> aerodromi;
    if (json_Aerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(json_Aerodromi);
    }
    rc.close();
    return aerodromi;
  }

  public List<Aerodrom> getAerodromi() {
    return this.getAerodromi(1, 20);
  }

  public Aerodrom getAerodrom(String icao) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom k = rc.getAerodrom(icao);
    rc.close();
    return k;
  }

  public List<UdaljenostAerodrom> getAerodromiUdaljenost(String icao,
      int odBroja, int broj) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodrom[] json_AerodromiUdaljenost =
        rc.getAerodromiUdaljenost(icao, odBroja, broj);
    List<UdaljenostAerodrom> udaljenostAerodromi;
    if (json_AerodromiUdaljenost == null) {
      udaljenostAerodromi = new ArrayList<>();
    } else {
      udaljenostAerodromi = Arrays.asList(json_AerodromiUdaljenost);
    }
    rc.close();

    return udaljenostAerodromi;
  }

  public UdaljenostAerodromDrzava getUdaljenostAerodromDrzava(String icao) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodromDrzava uad = rc.getUdaljenostAerodromDrzava(icao);
    rc.close();
    return uad;
  }

  public List<Udaljenost> getUdaljenost2Aerodroma(String icaoOd,
      String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    Udaljenost[] json_udaljenost2Aerodroma =
        rc.getUdaljenost2Aerodroma(icaoOd, icaoDo);
    List<Udaljenost> udaljenost2Aerodroma;
    if (json_udaljenost2Aerodroma == null) {
      udaljenost2Aerodroma = new ArrayList<>();
    } else {
      udaljenost2Aerodroma = Arrays.asList(json_udaljenost2Aerodroma);
    }
    rc.close();

    return udaljenost2Aerodroma;
  }

  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = wa_1;// wa_1 // "http://200.20.0.4:8080/mkovac_zadaca_2_wa_1/api"

    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    public Aerodrom[] getAerodromi(int odBroja, int broj)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource =
          resource.queryParam("odBroja", odBroja).queryParam("broj", broj);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom[] aerodromi = null;
      try {
        aerodromi = gson.fromJson(request.get(String.class), Aerodrom[].class);
      } catch (JsonSyntaxException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      return aerodromi;
    }

    public Aerodrom getAerodrom(String icao) throws ClientErrorException {
      WebTarget resource = webTarget;
      // 1. ako treba dodat jos / u path, onda se samo doda jos
      // /{1}/{2} tako nesto, ubacuje icao text u {0},{1} itd
      // tj. za npr GET {icao}/udaljenosti bi islo "{0}/udaljenosti"
      // a za GET {icaoOd}/{icaoDo} bi islo {0}/{1}
      // 2. za DELETE/{id} bi islo {0} kojem se proslijedi id, resource.request() i onda
      // request.delete
      // 3. za POST bi islo samo resource.request() i onda request.post
      resource = resource
          .path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom aerodrom =
          gson.fromJson(request.get(String.class), Aerodrom.class);
      return aerodrom;
    }


    public UdaljenostAerodrom[] getAerodromiUdaljenost(String icao, int odBroja,
        int broj) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource
          .path(java.text.MessageFormat.format("{0}", new Object[] {icao}))
          .path("udaljenosti").queryParam("odBroja", odBroja)
          .queryParam("broj", broj);

      System.out.println("Putanja: " + resource.toString());
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenostAerodromi = null;
      try {
        udaljenostAerodromi = gson.fromJson(request.get(String.class),
            UdaljenostAerodrom[].class);
      } catch (JsonSyntaxException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      System.out.println("JSON: " + request.get(String.class));

      return udaljenostAerodromi;
    }

    public UdaljenostAerodromDrzava getUdaljenostAerodromDrzava(String icao)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource
          .path(java.text.MessageFormat.format("{0}", new Object[] {icao}))
          .path("najduljiPutDrzave");
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      UdaljenostAerodromDrzava udaljenostAerodromDrzava = null;
      Gson gson = new Gson();
      udaljenostAerodromDrzava = gson.fromJson(request.get(String.class),
          UdaljenostAerodromDrzava.class);

      System.out.println("Putanja: " + resource.toString());
      System.out.println("JSON: " + request.get(String.class));

      return udaljenostAerodromDrzava;
    }

    public Udaljenost[] getUdaljenost2Aerodroma(String icaoOd, String icaoDo) {
      WebTarget resource = webTarget;

      if ((icaoOd == null || icaoDo == null)
          || (icaoOd.isEmpty() || icaoDo.isEmpty()))
        return null;

      resource = resource.path(java.text.MessageFormat.format("{0}/{1}",
          new Object[] {icaoOd, icaoDo}));

      System.out.println("Putanja: " + resource.toString());
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Udaljenost[] udaljenost2Aerodroma = null;
      try {
        udaljenost2Aerodroma =
            gson.fromJson(request.get(String.class), Udaljenost[].class);
      } catch (JsonSyntaxException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      System.out.println("JSON: " + request.get(String.class));

      return udaljenost2Aerodroma;
    }

    public void close() {
      client.close();
    }
  }

}
