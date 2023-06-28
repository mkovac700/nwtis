package org.foi.nwtis.mkovac.aplikacija_5.mvc;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.klijenti.RestKlijentAerodromi;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsAerodromi.endpoint.SOAPException_Exception;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsMeteo.endpoint.Meteo;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsMeteo.endpoint.MeteoPodaci;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Info;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.WebServiceRef;

@Controller
@Path("aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/mkovac_aplikacija_4/aerodromi?wsdl")
  private Aerodromi serviceAerodromi;

  @WebServiceRef(wsdlLocation = "http://localhost:8080/mkovac_aplikacija_4/meteo?wsdl")
  private Meteo serviceMeteo;

  @Inject
  private Models model;

  @Inject
  private HttpSession session;

  private Info info;

  private int brojRedova = 15;

  public KontrolerAerodroma() {
    ServletContext context = WsSlusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");

    String autorIme = konf.dajPostavku("autor.ime");
    String autorPrezime = konf.dajPostavku("autor.prezime");
    String autorPredmet = konf.dajPostavku("autor.predmet");
    String aplikacijaGodina = konf.dajPostavku("aplikacija.godina");
    String aplikacijaVerzija = konf.dajPostavku("aplikacija.verzija");

    try {
      brojRedova = Integer.parseInt(konf.dajPostavku("stranica.brojRedova"));
    } catch (NumberFormatException e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Neispravan broj redova. Postavljeno na zadani broj redova (15)." + e.getMessage());
    }

    info = new Info(autorIme, autorPrezime, autorPredmet, aplikacijaGodina, aplikacijaVerzija);
  }

  @GET
  @View("5.5.jsp")
  public void aerodromi() {
    model.put("info", info);
  }

  @GET
  @Path("svi")
  @View("5.5.1.jsp")
  public void getAerodromi() {
    model.put("info", info);

    try {
      RestKlijentAerodromi rka = new RestKlijentAerodromi();

      List<Aerodrom> aerodromi = rka.dajAerodromi(null, null, 1, brojRedova);

      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  @POST
  @Path("svi")
  @View("5.5.1.jsp")
  public void postAerodromi(@FormParam("stranica") String stranica,
      @FormParam("traziNaziv") String traziNaziv, @FormParam("traziDrzavu") String traziDrzavu) {


    model.put("info", info);

    model.put("traziNaziv", traziNaziv);
    model.put("traziDrzavu", traziDrzavu);

    System.out.println("Naziv i drzava: " + traziNaziv + " " + traziDrzavu);

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
      RestKlijentAerodromi rka = new RestKlijentAerodromi();

      List<Aerodrom> aerodromi = rka.dajAerodromi(traziNaziv, traziDrzavu, pocetak, broj);

      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  @POST
  @Path("svi/preuzimanje")
  public Response postAerodromiPreuzimanje(@FormParam("icao") String icao) {
    String odgovor = null;

    String korisnik = (String) session.getAttribute("korisnik");
    String lozinka = (String) session.getAttribute("lozinka");

    var port = serviceAerodromi.getWsAerodromiPort();
    try {
      var rezultat = port.dodajAerodromZaLetove(korisnik, lozinka, icao);
      if (rezultat)
        odgovor = "Aerodrom uspješno dodan za preuzimanje letova!";
      else
        odgovor = "Greška u dodavanju aerodroma za preuzimanje letova!";

      Logger.getGlobal().log(Level.INFO, odgovor);
    } catch (SOAPException_Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      odgovor = e.getMessage();
    } catch (Exception ex) {
      Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
      odgovor = ex.getMessage();
    }

    return Response.ok(odgovor).build();
  }

  @GET
  @Path("{icao}")
  @View("5.5.2.jsp")
  public void getAerodrom(@PathParam("icao") String icao) {
    model.put("icao", icao);
    model.put("info", info);

    try {
      RestKlijentAerodromi rka = new RestKlijentAerodromi();
      var aerodrom = rka.dajAerodrom(icao);
      model.put("aerodrom", aerodrom);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }

    var port = serviceMeteo.getWsMeteoPort();
    try {
      MeteoPodaci meteo = port.dajMeteo(icao);
      model.put("meteo", meteo);
    } catch (org.foi.nwtis.mkovac_aplikacija_4.ws.WsMeteo.endpoint.SOAPException_Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  @GET
  @Path("preuzimanje")
  @View("5.5.3.jsp")
  public void getPreuzimanje() {
    model.put("info", info);

    String korisnik = (String) session.getAttribute("korisnik");
    String lozinka = (String) session.getAttribute("lozinka");

    var port = serviceAerodromi.getWsAerodromiPort();

    var aerodromi = port.dajSveAerodromeZaLetove(korisnik, lozinka);

    model.put("aerodromi", aerodromi);

  }

  @POST
  @Path("preuzimanje/status")
  public Response postPreuzimanjeStatus(@FormParam("icao") String icao,
      @FormParam("akcija") boolean akcija) {

    String odgovor = null;

    String korisnik = (String) session.getAttribute("korisnik");
    String lozinka = (String) session.getAttribute("lozinka");

    var port = serviceAerodromi.getWsAerodromiPort();
    try {
      if (akcija) {
        var rezultat = port.aktivirajAerodromZaLetove(korisnik, lozinka, icao);
        if (rezultat)
          odgovor = "Aerodrom uspješno aktiviran za preuzimanje letova!";
        else
          odgovor = "Greška u aktiviranju aerodroma za preuzimanje letova!";
      } else {
        var rezultat = port.pauzirajAerodromZaLetove(korisnik, lozinka, icao);
        if (rezultat)
          odgovor = "Aerodrom uspješno pauziran za preuzimanje letova!";
        else
          odgovor = "Greška u pauziranju aerodroma za preuzimanje letova!";
      }

      Logger.getGlobal().log(Level.INFO, odgovor);

    } catch (SOAPException_Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      odgovor = e.getMessage();
    } catch (Exception ex) {
      Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
      odgovor = ex.getMessage();
    }

    return Response.ok(odgovor).build();

  }

  @GET
  @Path("udaljenosti2aerodroma")
  @View("5.5.4.jsp")
  public void getUdaljenosti2Aerodroma() {
    model.put("info", info);
  }

  @POST
  @Path("udaljenosti2aerodroma")
  @View("5.5.4.jsp")
  public void postUdaljenosti2Aerodroma(@FormParam("icaoOd") String icaoOd,
      @FormParam("icaoDo") String icaoDo) {
    model.put("info", info);

    model.put("icaoOd", icaoOd);
    model.put("icaoDo", icaoDo);

    try {
      RestKlijentAerodromi rka = new RestKlijentAerodromi();
      List<Udaljenost> udaljenost2Aerodroma = rka.dajUdaljenosti2Aerodroma(icaoOd, icaoDo);
      model.put("udaljenost2Aerodroma", udaljenost2Aerodroma);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
  }

  @GET
  @Path("udaljenost2aerodroma/izracun")
  @View("5.5.5.jsp")
  public void getIzracunUdaljenosti2Aerodroma() {
    model.put("info", info);
  }

  @POST
  @Path("udaljenost2aerodroma/izracun")
  @View("5.5.5.jsp")
  public void postIzracunUdaljenosti2Aerodroma(@FormParam("icaoOd") String icaoOd,
      @FormParam("icaoDo") String icaoDo) {
    model.put("info", info);

    model.put("icaoOd", icaoOd);
    model.put("icaoDo", icaoDo);

    UdaljenostAerodrom ua = null;

    try {
      RestKlijentAerodromi rka = new RestKlijentAerodromi();
      ua = rka.dajIzracunUdaljenosti2Aerodroma(icaoOd, icaoDo);

    } catch (Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }

    model.put("ua", ua);
  }

  @GET
  @Path("udaljenosti2aerodroma/izracun1")
  @View("5.5.6.jsp")
  public void getIzracun1Udaljenosti2Aerodroma() {
    model.put("info", info);
  }

  @POST
  @Path("udaljenosti2aerodroma/izracun1")
  @View("5.5.6.jsp")
  public void postIzracun1Udaljenosti2Aerodroma(@FormParam("icaoOd") String icaoOd,
      @FormParam("icaoDo") String icaoDo) {
    model.put("info", info);

    model.put("icaoOd", icaoOd);
    model.put("icaoDo", icaoDo);

    List<UdaljenostAerodrom> udaljenosti1 = null;

    try {
      RestKlijentAerodromi rka = new RestKlijentAerodromi();
      udaljenosti1 = rka.dajIzracun1Udaljenosti2Aerodroma(icaoOd, icaoDo);

    } catch (Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }

    model.put("udaljenosti1", udaljenosti1);
  }

  @GET
  @Path("udaljenosti2aerodroma/izracun2")
  @View("5.5.7.jsp")
  public void getIzracun2Udaljenosti2Aerodroma() {
    model.put("info", info);
  }

  @POST
  @Path("udaljenosti2aerodroma/izracun2")
  @View("5.5.7.jsp")
  public void postIzracun2Udaljenosti2Aerodroma(@FormParam("icaoOd") String icaoOd,
      @FormParam("drzava") String drzava, @FormParam("km") String km) {
    model.put("info", info);

    model.put("icaoOd", icaoOd);
    model.put("drzava", drzava);
    model.put("km", km);

    List<UdaljenostAerodrom> udaljenosti2 = null;

    try {
      RestKlijentAerodromi rka = new RestKlijentAerodromi();
      udaljenosti2 = rka.dajIzracun2Udaljenosti2Aerodroma(icaoOd, drzava, km);

    } catch (Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }

    model.put("udaljenosti2", udaljenosti2);
  }

}
