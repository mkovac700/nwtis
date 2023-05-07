/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.mkovac.zadaca_2.mvc;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.rest.RestKlijentLetova;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.MvcAplikacijaSlusac;
import org.foi.nwtis.rest.podaci.LetAviona;
import org.foi.nwtis.rest.podaci.LetAvionaID;
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
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

/**
 * Kontroler za MVC aplikaciju Letovi. Služi za preusmjeravanje do odgovarajućih stranica, prikaz
 * stranica te učitavanje podataka za prikaz na stranici.
 * 
 * @author Marijan Kovač
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

  /**
   * Metoda za prikaz stranice s polascima sa zadanog aerodroma na određeni dan.
   * 
   * @param stranica Broj trenutne stranice za koju se dohvaćaju podaci.
   * @param icao Oznaka aerodroma za koji se dohvaćaju podaci.
   * @param dan Dan za koji se dohvaćaju podaci u formatu dd.MM.yyyy
   */
  @GET
  @Path("aerodrom")
  @View("letoviAerodrom.jsp")
  public void dajLetoviAerodrom(@QueryParam("stranica") String stranica,
      @QueryParam("icao") String icao, @QueryParam("dan") String dan) {

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
      RestKlijentLetova rca = new RestKlijentLetova();
      info = rca.dajInfo();
      List<LetAviona> letoviAviona = rca.dajLetoveAerodrom(icao, dan, pocetak, broj);
      model.put("letoviAviona", letoviAviona);

    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }

  /**
   * Metoda za prikaz stranice s polascima sa polazišnog aerodroma do odredišnog aerodroma na
   * određeni dan.
   * 
   * @param stranica Broj trenutne stranice za koju se dohvaćaju podaci.
   * @param icaoOd Oznaka polazišnog aerodroma za koji se dohvaćaju podaci.
   * @param icaoDo Oznaka odredišnog aerodroma za koji se dohvaćaju podaci.
   * @param dan Dan za koji se dohvaćaju podaci u formatu dd.MM.yyyy
   */
  @GET
  @Path("aerodromi")
  @View("letoviAerodromi.jsp")
  public void dajLetoviAerodrom(@QueryParam("stranica") String stranica,
      @QueryParam("icaoOd") String icaoOd, @QueryParam("icaoDo") String icaoDo,
      @QueryParam("dan") String dan) {

    String greska = null;

    String[] info = null;

    int brojStranice = 1;
    int pocetak = 1;
    int broj = brojRedova;

    model.put("icaoOd", icaoOd);
    model.put("icaoDo", icaoDo);

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
      RestKlijentLetova rca = new RestKlijentLetova();
      info = rca.dajInfo();
      List<LetAviona> letoviAviona = rca.dajLetoveAerodrom(icaoOd, icaoDo, dan, pocetak, broj);
      model.put("letoviAviona", letoviAviona);


    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
    model.put("greska", greska);
  }

  /**
   * Metoda za prikaz stranice sa spremljenim letovima.
   */
  @GET
  @Path("spremljeni")
  @View("letoviSpremljeni.jsp")
  public void dajLetoviSpremljeni() {

    String greska = null;

    String[] info = null;

    try {
      RestKlijentLetova rca = new RestKlijentLetova();
      info = rca.dajInfo();
      List<LetAvionaID> letoviAviona = rca.dajSpremljeneLetove();
      model.put("letoviAviona", letoviAviona);
      model.put("greska", greska);

    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
  }

  /**
   * Metoda za slanje zahtjeva za spremanje odabranog leta u bazu podataka. Prikazuje stranicu s
   * informacijama o uspjehu akcije.
   * 
   * @param json_letAviona Podaci o odabranom letu u JSON formatu
   * @param url Trenutni URL stranice za preusmjeravanje (povratak) na prethodnu stranicu
   */
  @POST
  @View("spremiLet.jsp")
  public void spremiLet(@FormParam("jsonLetAviona") String json_letAviona,
      @FormParam("url") String url) {

    String[] info = null;

    model.put("url", url);
    String odgovor = null;
    try {
      RestKlijentLetova rca = new RestKlijentLetova();
      info = rca.dajInfo();
      odgovor = rca.spremiLet(json_letAviona);
      model.put("odgovor", odgovor);

    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
  }

  /**
   * Metoda za slanje zahtjeva za brisanje odabranog leta iz baze podataka. Prikazuje stranicu s
   * informacijama o uspjehu akcije.
   * 
   * @param id ID leta koji se želi obrisati
   * @param url Trenutni URL stranice za preusmjeravanje (povratak) na prethodnu stranicu
   */
  @POST
  @Path("{id}")
  @View("obrisiLet.jsp")
  public void obrisiLet(@PathParam("id") int id, @FormParam("url") String url) {

    String[] info = null;
    model.put("url", url);
    String odgovor = null;
    try {
      RestKlijentLetova rca = new RestKlijentLetova();
      info = rca.dajInfo();
      odgovor = rca.obrisiLet(id);
      model.put("odgovor", odgovor);


    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("info", info);
  }
}
