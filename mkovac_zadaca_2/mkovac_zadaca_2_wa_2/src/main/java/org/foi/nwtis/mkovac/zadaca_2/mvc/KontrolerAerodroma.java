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
 * Kontroler za MVC aplikaciju Aerodromi. Služi za preusmjeravanje do odgovarajućih stranica, prikaz
 * stranica te učitavanje podataka za prikaz na stranici.
 * 
 * @author Marijan Kovač
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

  /**
   * Metoda za prikaz početne stranice.
   */
  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {

    RestKlijentAerodroma rca = new RestKlijentAerodroma();
    String[] info = rca.dajInfo();
    model.put("info", info);
  }

  /**
   * Metoda za prikaz stranice sa svim aerodromima.
   * 
   * @param stranica Broj trenutne stranice za koju se dohvaćaju podaci.
   */
  @GET
  @Path("svi")
  @View("aerodromi.jsp")
  public void dajAerodromi(@QueryParam("stranica") String stranica) {

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
      info = rca.dajInfo();
      List<Aerodrom> aerodromi = rca.dajAerodromi(pocetak, broj);
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }

  /**
   * Metoda za prikaz stranice s podacima za jedan aerodrom.
   * 
   * @param icao Oznaka aerodroma za koji se dohvaćaju podaci.
   */
  @GET
  @Path("{icao}")
  @View("aerodrom.jsp")
  public void dajAerodrom(@PathParam("icao") String icao) {
    model.put("icao", icao);
    String[] info = null;
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var aerodrom = rca.dajAerodrom(icao);
      model.put("aerodrom", aerodrom);
      info = rca.dajInfo();
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
  }

  /**
   * Metoda za prikaz stranice s podacima o najduljem putu države
   * 
   * @param icao Oznaka aerodroma za koji se dohvaćaju podaci.
   */
  @GET
  @Path("{icao}/najduljiPutDrzave")
  @View("aerodromNajduljiPut.jsp")
  public void dajUdaljenostiAerodromDrzava(@PathParam("icao") String icao) {
    model.put("icao", icao);
    String[] info = null;
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      UdaljenostAerodromDrzava udaljenostAerodromDrzava = rca.dajUdaljenostAerodromDrzava(icao);
      model.put("udaljenostAerodromDrzava", udaljenostAerodromDrzava);
      info = rca.dajInfo();

    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
  }

  /**
   * Metoda za prikaz stranice s udaljenostima do svih aerodroma od odabranog aerodroma.
   * 
   * @param icao Oznaka aerodroma za koji se dohvaćaju podaci.
   * @param stranica Broj trenutne stranice za koju se dohvaćaju podaci.
   */
  @GET
  @Path("{icao}/udaljenosti")
  @View("aerodromUdaljenosti.jsp")
  public void dajAerodromUdaljenosti(@PathParam("icao") String icao,
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
      info = rca.dajInfo();
      List<UdaljenostAerodrom> udaljenostAerodromi =
          rca.dajAerodromiUdaljenost(icao, pocetak, broj);
      model.put("udaljenostAerodromi", udaljenostAerodromi);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }

  /**
   * Metoda za prikaz stranice s udaljenostima između dvaju aerodroma
   * 
   * @param icaoOd Oznaka polazišnog aerodroma za koji se dohvaćaju podaci.
   * @param icaoDo Oznaka odredišnog za koji se dohvaćaju podaci.
   */
  @GET
  @Path("udaljenosti2aerodroma")
  @View("aerodromiUdaljenosti.jsp")
  public void dajAerodromiUdaljenost(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("icaoDo") String icaoDo) {

    String greska = null;

    String[] info = null;

    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      info = rca.dajInfo();
      List<Udaljenost> udaljenost2Aerodroma = rca.dajUdaljenost2Aerodroma(icaoOd, icaoDo);
      model.put("udaljenost2Aerodroma", udaljenost2Aerodroma);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }
}
