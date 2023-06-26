package org.foi.nwtis.mkovac.aplikacija_5.mvc;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.mkovac.aplikacija_5.zrna.SakupljacJmsPoruka;
import org.foi.nwtis.podaci.Info;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Controller
@Path("poruke")
@RequestScoped
public class KontrolerPoruka {

  @EJB
  SakupljacJmsPoruka sakupljacJmsPoruka;

  @Inject
  private Models model;

  private Info info;

  private int brojRedova = 15;

  public KontrolerPoruka() {
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
  @View("5.4.jsp")
  public void poruke(@QueryParam("stranica") String stranica) {
    model.put("info", info);

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

    var poruke = sakupljacJmsPoruka.dajSvePoruke(pocetak, broj);

    model.put("poruke", poruke);

  }

  @POST
  @View("5.4.jsp")
  public void postPoruke() {
    model.put("info", info);

    sakupljacJmsPoruka.obrisiSvePoruke();
  }

}
