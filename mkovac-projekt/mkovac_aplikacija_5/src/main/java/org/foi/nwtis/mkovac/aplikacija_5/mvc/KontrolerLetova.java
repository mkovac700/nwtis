package org.foi.nwtis.mkovac.aplikacija_5.mvc;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsLetovi.endpoint.LetAviona;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsLetovi.endpoint.Letovi;
import org.foi.nwtis.mkovac_aplikacija_4.ws.WsLetovi.endpoint.SOAPException_Exception;
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
@Path("letovi")
@RequestScoped
public class KontrolerLetova {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/mkovac_aplikacija_4/letovi?wsdl")
  private Letovi serviceLetovi;

  @Inject
  private Models model;

  private Info info;

  private int brojRedova = 15;

  public KontrolerLetova() {
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
  @View("5.6.jsp")
  public void letovi() {
    model.put("info", info);
  }

  @GET
  @Path("spremljeni2")
  @View("5.6.2.jsp")
  public void spremljeniLetovi2() {
    model.put("info", info);
  }

  @POST
  @Path("spremljeni2")
  @View("5.6.2.jsp")
  public void spremljeniLetovi2(@FormParam("icao") String icao, @FormParam("dan") String dan,
      @FormParam("stranica") String stranica) {
    model.put("info", info);

    model.put("icao", icao);
    model.put("dan", dan);
    model.put("stranica", stranica);

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

    var port = serviceLetovi.getWsLetoviPort();

    List<LetAviona> letoviAviona = null;

    try {
      letoviAviona = port.dajPolaskeNaDan("pperic", "pero123", icao, dan, pocetak, broj);

    } catch (SOAPException_Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    } catch (Exception ex) {
      Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
    }

    model.put("letoviAviona", letoviAviona);
  }

}
