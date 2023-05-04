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
import org.foi.nwtis.podaci.Info;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
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
    Info info = rca.getInfo();
    model.put("info", info);
  }

  @GET
  @Path("svi")
  @View("aerodromi.jsp")
  public void getAerodromi(@QueryParam("odBroja") String odBroja) {

    String greska = null;

    int pocetak = 1;
    int broj = brojRedova;

    if (odBroja != null && !odBroja.isEmpty()) {
      try {
        pocetak = Integer.parseInt(odBroja);
        if (pocetak < 1)
          pocetak = 1;
      } catch (NumberFormatException e) {
        greska = "Neispravni parametri!";
      }
    }

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
  @Path("icao")
  @View("aerodrom.jsp")
  public void getAerodrom(@QueryParam("icao") String icao) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var aerodrom = rca.getAerodrom(icao);
      model.put("aerodrom", aerodrom);
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
