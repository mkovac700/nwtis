/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.mkovac.zadaca_2.mvc;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.rest.RestKlijentAerodroma;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.MvcAplikacijaSlusac;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzava;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  @Inject
  private Models model;

  private ServletContext context;
  private Konfiguracija konf;
  private static int brojRedova;

  public KontrolerAerodroma() {
    context = MvcAplikacijaSlusac.getServletContext();
    konf = (Konfiguracija) context.getAttribute("konfig");
    brojRedova = Integer.parseInt(konf.dajPostavku("stranica.brojRedova"));
  }

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {

    RestKlijentAerodroma rca = new RestKlijentAerodroma();
    String[] info = rca.getInfo();
    model.put("info", info);
  }

  @GET
  @Path("svi")
  @View("aerodromi.jsp")
  public void getAerodromi(@QueryParam("stranica") String stranica) {

    String greska = null;

    String[] info = null;

    int brojStranice = 1;
    int pocetak = 1;
    int broj = brojRedova;

    if (stranica != null && !stranica.isEmpty()) {
      try {
        brojStranice = Integer.parseInt(stranica);

        if (brojStranice < 1)
          brojStranice = 1;
      } catch (NumberFormatException e) {
        brojStranice = 1;
      }
    }

    pocetak = (brojStranice - 1) * broj + 1;

    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      info = rca.getInfo();
      List<Aerodrom> aerodromi = rca.getAerodromi(pocetak, broj);
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }

  @GET
  @Path("{icao}")
  @View("aerodrom.jsp")
  public void getAerodrom(@PathParam("icao") String icao) {
    model.put("icao", icao);
    String[] info = null;
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var aerodrom = rca.getAerodrom(icao);
      model.put("aerodrom", aerodrom);
      info = rca.getInfo();
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
  }

  @GET
  @Path("{icao}/najduljiPutDrzave")
  @View("aerodromNajduljiPut.jsp")
  public void getUdaljenostiAerodromDrzava(@PathParam("icao") String icao) {
    model.put("icao", icao);
    String[] info = null;
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      UdaljenostAerodromDrzava udaljenostAerodromDrzava = rca.getUdaljenostAerodromDrzava(icao);
      model.put("udaljenostAerodromDrzava", udaljenostAerodromDrzava);
      info = rca.getInfo();

    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
  }

  @GET
  @Path("{icao}/udaljenosti")
  @View("aerodromUdaljenosti.jsp")
  public void getAerodromUdaljenosti(@PathParam("icao") String icao,
      @QueryParam("stranica") String stranica) {
    String greska = null;

    String[] info = null;

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

    pocetak = (brojStranice - 1) * broj + 1;


    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      info = rca.getInfo();
      List<UdaljenostAerodrom> udaljenostAerodromi =
          rca.getAerodromiUdaljenost(icao, pocetak, broj);
      model.put("udaljenostAerodromi", udaljenostAerodromi);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }

  @GET
  @Path("udaljenosti2aerodroma")
  @View("aerodromiUdaljenosti.jsp")
  public void getAerodromiUdaljenost(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("icaoDo") String icaoDo) {

    String greska = null;

    String[] info = null;

    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      info = rca.getInfo();
      List<Udaljenost> udaljenost2Aerodroma = rca.getUdaljenost2Aerodroma(icaoOd, icaoDo);
      model.put("udaljenost2Aerodroma", udaljenost2Aerodroma);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }
}
