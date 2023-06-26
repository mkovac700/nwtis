package org.foi.nwtis.mkovac.aplikacija_5.mvc;

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
import jakarta.ws.rs.Path;

@Controller
@Path("poruke")
@RequestScoped
public class KontrolerPoruka {

  @EJB
  SakupljacJmsPoruka sakupljacJmsPoruka;

  @Inject
  private Models model;

  private Info info;

  public KontrolerPoruka() {
    ServletContext context = WsSlusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");

    String autorIme = konf.dajPostavku("autor.ime");
    String autorPrezime = konf.dajPostavku("autor.prezime");
    String autorPredmet = konf.dajPostavku("autor.predmet");
    String aplikacijaGodina = konf.dajPostavku("aplikacija.godina");
    String aplikacijaVerzija = konf.dajPostavku("aplikacija.verzija");

    info = new Info(autorIme, autorPrezime, autorPredmet, aplikacijaGodina, aplikacijaVerzija);
  }

  @GET
  @View("5.4.jsp")
  public void poruke() {
    model.put("info", info);

    var poruke = sakupljacJmsPoruka.dajSvePoruke();

    model.put("poruke", poruke);

  }

}
