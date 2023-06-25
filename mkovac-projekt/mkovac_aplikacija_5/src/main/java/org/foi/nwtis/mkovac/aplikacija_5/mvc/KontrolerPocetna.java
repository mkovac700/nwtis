package org.foi.nwtis.mkovac.aplikacija_5.mvc;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.podaci.Info;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Controller
@Path("pocetna")
@RequestScoped
public class KontrolerPocetna {
  @Inject
  private Models model;

  private Info info;

  public KontrolerPocetna() {
    ServletContext context = WsSlusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");

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
  @View("5.1.jsp")
  public void pocetak() {
    model.put("info", info);

  }
}
