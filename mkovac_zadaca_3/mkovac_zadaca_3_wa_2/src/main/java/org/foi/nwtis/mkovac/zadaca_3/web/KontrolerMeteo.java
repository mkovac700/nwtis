package org.foi.nwtis.mkovac.zadaca_3.web;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_3.slusaci.Slusac;
import org.foi.nwtis.mkovac.zadaca_3.ws.WsMeteo.endpoint.Meteo;
import org.foi.nwtis.mkovac.zadaca_3.ws.WsMeteo.endpoint.MeteoPodaci;
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
 * MVC kontroler za meteo
 * 
 * @author Marijan Kovač
 *
 */
@Controller
@Path("meteo")
@RequestScoped
public class KontrolerMeteo {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/mkovac_zadaca_3_wa_1/meteo?wsdl")
  private Meteo serviceMeteo;

  @Inject
  private Models model;

  private String[] info;

  public KontrolerMeteo() {
    ServletContext context = Slusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");

    // try {
    // brojRedova = Integer.parseInt(konf.dajPostavku("stranica.brojRedova"));
    // } catch (NumberFormatException e) {
    // Logger.getGlobal().log(Level.SEVERE,
    // "Neispravan broj redova. Postavljeno na zadani broj redova (15)." + e.getMessage());
    // }

    String autorIme = konf.dajPostavku("autor.ime");
    String autorPrezime = konf.dajPostavku("autor.prezime");
    String autorPredmet = konf.dajPostavku("autor.predmet");
    String aplikacijaGodina = konf.dajPostavku("aplikacija.godina");
    String aplikacijaVerzija = konf.dajPostavku("aplikacija.verzija");

    info = new String[] {autorIme, autorPrezime, autorPredmet, aplikacijaGodina, aplikacijaVerzija};
  }

  @GET
  @Path("{icao}")
  @View("meteoAerodrom.jsp")
  public void dajMeteoAerodrom(@PathParam("icao") String icao) {
    String greska = null;

    var port = serviceMeteo.getWsMeteoPort();
    var meteo = port.dajMeteo(icao);

    model.put("icao", icao);
    model.put("meteo", meteo);
    model.put("greska", greska);
    model.put("info", info);
  }

  @GET
  @View("meteoAdresa.jsp")
  public void dajMeteoAdresa(@QueryParam("adresa") String adresa) {
    String greska = null;

    MeteoPodaci meteo = null;
    if (adresa != null && adresa.trim().length() != 0) {
      var port = serviceMeteo.getWsMeteoPort();
      meteo = port.dajMeteoAdresa(adresa);
    }

    model.put("meteo", meteo);
    model.put("greska", greska);
    model.put("info", info);
  }

}
