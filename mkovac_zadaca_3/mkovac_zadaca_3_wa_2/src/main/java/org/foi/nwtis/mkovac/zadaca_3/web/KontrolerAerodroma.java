package org.foi.nwtis.mkovac.zadaca_3.web;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_3.slusaci.Slusac;
import org.foi.nwtis.mkovac.zadaca_3.ws.WsAerodromi.endpoint.Aerodromi;
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
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/mkovac_zadaca_3_wa_1/aerodromi?wsdl")
  private Aerodromi service;

  @Inject
  private Models model;

  private int brojRedova = 15;
  private String[] info;

  public KontrolerAerodroma() {
    ServletContext context = Slusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");

    try {
      brojRedova = Integer.parseInt(konf.dajPostavku("stranica.brojRedova"));
    } catch (NumberFormatException e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Neispravan broj redova. Postavljeno na zadani broj redova (15)." + e.getMessage());
    }

    String autorIme = konf.dajPostavku("autor.ime");
    String autorPrezime = konf.dajPostavku("autor.prezime");
    String autorPredmet = konf.dajPostavku("autor.predmet");
    String aplikacijaGodina = konf.dajPostavku("aplikacija.godina");
    String aplikacijaVerzija = konf.dajPostavku("aplikacija.verzija");

    info = new String[] {autorIme, autorPrezime, autorPredmet, aplikacijaGodina, aplikacijaVerzija};
  }

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {
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

    pocetak = (brojStranice - 1) * broj + 1;

    try {
      var port = service.getWsAerodromiPort();
      var aerodromi = port.dajSveAerodrome(pocetak, broj);
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      greska = e.getMessage();
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }

  @GET
  @Path("{icao}")
  @View("aerodrom.jsp")
  public void getAerodrom(@PathParam("icao") String icao) {
    String greska = null;
    model.put("icao", icao);
    try {
      var port = service.getWsAerodromiPort();
      var aerodrom = port.dajAerodrom(icao);
      model.put("aerodrom", aerodrom);
    } catch (Exception e) {
      greska = e.getMessage();
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }

  @GET
  @Path("{icao}/najduljiPutDrzave")
  @View("aerodromNajduljiPut.jsp")
  public void dajUdaljenostiAerodromDrzava(@PathParam("icao") String icao) {
    String greska = null;
    model.put("icao", icao);
    try {
      var port = service.getWsAerodromiPort();
      var udaljenostAerodromDrzava = port.dajNajduljiPutDrzave(icao);
      model.put("udaljenostAerodromDrzava", udaljenostAerodromDrzava);

    } catch (Exception e) {
      greska = e.getMessage();
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }

  @GET
  @Path("{icao}/udaljenosti")
  @View("aerodromUdaljenosti.jsp")
  public void dajAerodromUdaljenosti(@PathParam("icao") String icao,
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

    pocetak = (brojStranice - 1) * broj + 1;

    try {
      var port = service.getWsAerodromiPort();
      var udaljenostAerodromi = port.dajSveUdaljenostiAerodroma(icao, pocetak, broj);
      model.put("udaljenostAerodromi", udaljenostAerodromi);
    } catch (Exception e) {
      greska = e.getMessage();
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

    try {
      var port = service.getWsAerodromiPort();
      var udaljenost2Aerodroma = port.dajUdaljenostiAerodroma(icaoOd, icaoDo);
      model.put("udaljenost2Aerodroma", udaljenost2Aerodroma);

    } catch (Exception e) {
      greska = e.getMessage();
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }
}
