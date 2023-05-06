package org.foi.nwtis.mkovac.zadaca_2.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.MvcAplikacijaSlusac;
import org.foi.nwtis.rest.podaci.LetAviona;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestKlijentLetova {

  private ServletContext context;
  private Konfiguracija konf;
  private static String wa_1;

  public RestKlijentLetova() {
    context = MvcAplikacijaSlusac.getServletContext();
    konf = (Konfiguracija) context.getAttribute("konfig");
    wa_1 = konf.dajPostavku("adresa.wa_1");
  }

  public List<LetAviona> dajLetoveAerodrom(String icao, String dan, int odBroja,
      int broj) {
    RestKKlijent rc = new RestKKlijent();
    LetAviona[] json_letoviAviona =
        rc.dajLetoveAerodrom(icao, dan, odBroja, broj);
    List<LetAviona> letoviAviona;
    if (json_letoviAviona == null) {
      letoviAviona = new ArrayList<>();
    } else {
      letoviAviona = Arrays.asList(json_letoviAviona);
    }
    rc.close();

    return letoviAviona;
  }

  public String spremiLet(String json_LetAviona) {
    RestKKlijent rc = new RestKKlijent();
    String odgovor = rc.spremiLet(json_LetAviona);
    rc.close();
    return odgovor;
  }

  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = wa_1;// wa_1 // "http://200.20.0.4:8080/mkovac_zadaca_2_wa_1/api"

    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("letovi");
    }

    public LetAviona[] dajLetoveAerodrom(String icao, String dan, int odBroja,
        int broj) {
      WebTarget resource = webTarget;

      if ((icao == null || icao.isEmpty()))
        return null;

      resource = resource
          .path(java.text.MessageFormat.format("{0}", new Object[] {icao}))
          .queryParam("dan", dan).queryParam("odBroja", odBroja)
          .queryParam("broj", broj);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      LetAviona[] letoviAviona = null;
      try {
        letoviAviona =
            gson.fromJson(request.get(String.class), LetAviona[].class);
      } catch (JsonSyntaxException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      System.out.println("JSON: " + request.get(String.class));

      return letoviAviona;
    }

    public String spremiLet(String json_letAviona) {
      WebTarget resource = webTarget;

      Invocation.Builder request = resource.request();
      Response response = request
          .post(Entity.entity(json_letAviona, MediaType.APPLICATION_JSON));

      String odgovor = null;

      if (response.getStatus() == 200)
        odgovor = response.readEntity(String.class);
      else
        odgovor = response.getStatusInfo().getReasonPhrase();

      return odgovor;
    }

    public void close() {
      client.close();
    }
  }

}
