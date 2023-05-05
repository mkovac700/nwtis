/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.mkovac.zadaca_2.mvc;

import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.rest.RestKlijentAerodroma;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.MvcAplikacijaSlusac;
import org.foi.nwtis.podaci.Aerodrom;
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
  public void pocetak() { // /aerodromi/pocetak

    RestKlijentAerodroma rca = new RestKlijentAerodroma();
    String[] info = rca.getInfo();
    model.put("info", info);
  }

  @GET
  @Path("svi")
  @View("aerodromi.jsp")
  public void getAerodromi(@QueryParam("stranica") String stranica) {

    String greska = null;

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

    pocetak = (brojStranice - 1) * broj + 1; // brojStranice * broj + 1

    System.out.println("Poc: " + pocetak);
    System.out.println("Kraj: " + broj);

    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      List<Aerodrom> aerodromi = rca.getAerodromi(pocetak, broj);
      model.put("aerodromi", aerodromi);
      model.put("greska", greska);
      model.put("brojRedova", brojRedova);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("{icao}")
  @View("aerodrom.jsp")
  public void getAerodrom(@PathParam("icao") String icao) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var aerodrom = rca.getAerodrom(icao);
      model.put("aerodrom", aerodrom);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("{icao}/najduljiPutDrzave")
  @View("aerodromNajduljiPut.jsp")
  public void getUdaljenostiAerodromDrzava(@PathParam("icao") String icao) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      UdaljenostAerodromDrzava udaljenostAerodromDrzava =
          rca.getUdaljenostAerodromDrzava(icao);
      model.put("udaljenostAerodromDrzava", udaljenostAerodromDrzava);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("{icao}/udaljenosti")
  @View("aerodromUdaljenosti.jsp")
  public void getAerodromUdaljenosti(@PathParam("icao") String icao,
      @QueryParam("stranica") String stranica) {
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
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      List<UdaljenostAerodrom> udaljenostAerodromi =
          rca.getAerodromiUdaljenost(icao, pocetak, broj);
      model.put("udaljenostAerodromi", udaljenostAerodromi);

      model.put("greska", greska);
      model.put("brojRedova", brojRedova);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }



  @GET
  @Path("udaljenosti2aerodroma")
  @View("aerodromiUdaljenosti.jsp")
  public void getAerodromiUdaljenost(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("icaoDo") String icaoDo) {}

}
