package org.foi.nwtis.mkovac.zadaca_3.web;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_3.slusaci.Slusac;
import org.foi.nwtis.mkovac.zadaca_3.zrna.SakupljacJmsPoruka;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Controller
@Path("letovi")
@RequestScoped
public class KontrolerLetova {

  @EJB
  SakupljacJmsPoruka sakupljacJmsPoruka;

  @Inject
  private Models model;

  private String[] info;

  public KontrolerLetova() {
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
  @Path("poruke")
  @View("poruke.jsp")
  public void dajPoruke() {
    String greska = null;

    var poruke = sakupljacJmsPoruka.dajSvePoruke();

    model.put("poruke", poruke);
    model.put("greska", greska);
    model.put("info", info);
  }

}
