package org.foi.nwtis.mkovac.aplikacija_5.mvc;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.mkovac.aplikacija_5.zrna.Sesija;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.Korisnici;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.Korisnik;
import org.foi.nwtis.podaci.Info;
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
import jakarta.xml.ws.WebServiceRef;

@Controller
@Path("korisnici")
@RequestScoped
public class KontrolerKorisnika {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/mkovac_aplikacija_4/korisnici?wsdl")
  private Korisnici service;

  @Inject
  Sesija sesija;

  @Inject
  private Models model;

  private int brojRedova = 15;
  private Info info;

  public KontrolerKorisnika() {
    ServletContext context = WsSlusac.getServletContext();
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

    info = new Info(autorIme, autorPrezime, autorPredmet, aplikacijaGodina, aplikacijaVerzija);
  }

  /**
   * Metoda za prikaz početne stranice.
   */
  @GET
  @View("5.2.jsp")
  public void pocetak() {
    model.put("info", info);
  }

  @GET
  @Path("registracija")
  @View("5.2.1.jsp")
  public void registracija() {
    model.put("info", info);
  }

  @POST
  @Path("registracija")
  @View("5.2.1.jsp")
  public void postRegistracija(@FormParam("ime") String ime, @FormParam("prezime") String prezime,
      @FormParam("korime") String korime, @FormParam("lozinka") String lozinka) {
    model.put("info", info);

    Korisnik korisnik = new Korisnik();
    korisnik.setIme(ime);
    korisnik.setPrezime(prezime);
    korisnik.setKorisnik(korime);
    korisnik.setLozinka(lozinka);

    String odgovor = null;
    try {
      var port = service.getWsKorisniciPort();
      var rezultat = port.dodajKorisnika(korisnik);
      if (rezultat)
        odgovor = "Korisnik uspješno dodan!";
      else
        odgovor = "Greška prilikom registracije korisnika!";
    } catch (Exception e) {
      odgovor = e.getMessage();
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
    model.put("odgovor", odgovor);
  }

  @GET
  @Path("prijava")
  @View("5.2.2.jsp")
  public void prijava() {
    model.put("info", info);
  }

  @POST
  @Path("prijava")
  @View("5.2.2.jsp")
  public void postPrijava(@FormParam("korime") String korime,
      @FormParam("lozinka") String lozinka) {
    model.put("info", info);

    String odgovor = null;
    try {
      boolean rezultat = sesija.prijaviKorisnika(korime, lozinka);
      if (rezultat)
        odgovor = "Prijavljeni ste kao " + korime + "!";
      else
        odgovor = "Neuspješna prijava!";
    } catch (Exception e) {
      String msg = e.getMessage();
      if (msg.contains("Greška u dohvaćanju podataka o korisniku"))
        odgovor = "Korisnik ne postoji!";
      else if (msg.contains("Neispravna lozinka"))
        odgovor = "Neispravna lozinka!";
      else
        odgovor = "Neuspješna prijava!";
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }

    model.put("odgovor", odgovor);
  }

  @GET
  @Path("pregled")
  @View("5.2.3.jsp")
  public void pregled() {
    model.put("info", info);

    String korisnickoIme = sesija.getKorisnik();
    String lozinka = sesija.getLozinka();


    String odgovor = null;
    try {
      var port = service.getWsKorisniciPort();
      List<Korisnik> korisnici = port.dajKorisnike(korisnickoIme, lozinka, null, null);
      model.put("korisnici", korisnici);
    } catch (Exception e) {
      String msg = e.getMessage();
      odgovor = msg;
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }

    model.put("odgovor", odgovor);
  }

  @POST
  @Path("pregled")
  @View("5.2.3.jsp")
  public void postPregled(@FormParam("ime") String ime, @FormParam("prezime") String prezime) {
    model.put("info", info);

    String korisnickoIme = sesija.getKorisnik();
    String lozinka = sesija.getLozinka();

    String odgovor = null;
    try {
      var port = service.getWsKorisniciPort();
      List<Korisnik> korisnici = port.dajKorisnike(korisnickoIme, lozinka, ime, prezime);
      model.put("korisnici", korisnici);
    } catch (Exception e) {
      String msg = e.getMessage();
      odgovor = msg;
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }

    model.put("odgovor", odgovor);
  }
}
