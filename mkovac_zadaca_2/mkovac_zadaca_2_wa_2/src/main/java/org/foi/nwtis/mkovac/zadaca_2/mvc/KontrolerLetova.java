/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.mkovac.zadaca_2.mvc;

import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.rest.RestKlijentLetova;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.MvcAplikacijaSlusac;
import org.foi.nwtis.rest.podaci.LetAviona;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("letovi")
@RequestScoped
public class KontrolerLetova {

  @Inject
  private Models model;

  private ServletContext context;
  private Konfiguracija konf;
  private static int brojRedova;

  public KontrolerLetova() {
    context = MvcAplikacijaSlusac.getServletContext();
    konf = (Konfiguracija) context.getAttribute("konfig");
    brojRedova = Integer.parseInt(konf.dajPostavku("stranica.brojRedova"));
  }

  @GET
  @Path("aerodrom")
  @View("letoviAerodrom.jsp")
  public void getLetoviAerodrom(@QueryParam("stranica") String stranica,
      @QueryParam("icao") String icao, @QueryParam("dan") String dan) {


    String greska = null;

    int brojStranice = 1;
    int pocetak = 1;
    int broj = brojRedova;

    model.put("icao", icao);

    if (stranica != null && !stranica.isEmpty()) {
      try {
        brojStranice = Integer.parseInt(stranica);

        if (brojStranice < 1)
          brojStranice = 1;
      } catch (NumberFormatException e) {
        brojStranice = 1;
      }
    }

    pocetak = (brojStranice - 1) * broj + 1; // brojStranice * broj + 1

    System.out.println("Poc: " + pocetak);
    System.out.println("Kraj: " + broj);

    try {
      RestKlijentLetova rca = new RestKlijentLetova();
      List<LetAviona> letoviAviona =
          rca.dajLetoveAerodrom(icao, dan, pocetak, broj);
      model.put("letoviAviona", letoviAviona);

      model.put("greska", greska);
      model.put("brojRedova", brojRedova);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @POST
  @View("letovi.jsp")
  public void spremiLet(@FormParam("jsonLetAviona") String json_letAviona,
      @FormParam("url") String url) {

    System.out.println("JSON LET AVIONA: " + json_letAviona);

    model.put("url", url);
    String odgovor = null;
    try {
      RestKlijentLetova rca = new RestKlijentLetova();
      odgovor = rca.spremiLet(json_letAviona);
      model.put("odgovor", odgovor);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
