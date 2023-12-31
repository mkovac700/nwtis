package org.foi.nwtis.mkovac.aplikacija_5.mvc;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_5.klijenti.RestKlijentNadzor;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.podaci.Info;
import org.foi.nwtis.podaci.Status;
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

/**
 * MVC kontroler za nadzor
 * 
 * @author Marijan Kovač
 *
 */
@Controller
@Path("nadzor")
@RequestScoped
public class KontrolerNadzor {

  @Inject
  private Models model;

  private Info info;

  public KontrolerNadzor() {
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
  @View("5.3.jsp")
  public void nadzor() {
    model.put("info", info);
  }

  @POST
  @View("5.3.jsp")
  public void postNadzor(@FormParam("komanda") String komanda) {
    model.put("info", info);

    RestKlijentNadzor rkn = new RestKlijentNadzor();

    String odgovor = null;

    if (komanda.equals("STATUS")) {
      Status status = rkn.dajStatus();

      if (status != null) {
        String opis = status.getOpis();
        int statusKod = status.getStatus();

        if (statusKod == 200) {
          if (opis.split(" ")[1].equals("1"))
            odgovor = "Poslužitelj je aktivan!";
          else
            odgovor = "Poslužitelj je pauziran!";
        } else
          odgovor = opis;
      }
    }

    if (komanda.equals("KRAJ")) {
      Status status = rkn.dajKomandu(komanda);

      if (status != null) {
        String opis = status.getOpis();

        if (opis.equals("OK"))
          odgovor = "Poslužitelj je zaustavljen!";
        else
          odgovor = opis;
      }
    }

    if (komanda.equals("INIT")) {
      Status status = rkn.dajKomandu(komanda);

      if (status != null) {
        String opis = status.getOpis();

        if (opis.equals("OK"))
          odgovor = "Poslužitelj je inicijaliziran!";
        else
          odgovor = opis;
      }
    }

    if (komanda.equals("PAUZA")) {
      Status status = rkn.dajKomandu(komanda);

      if (status != null) {
        String opis = status.getOpis();

        if (opis.contains("OK")) {
          String brojZahtjeva = opis.split(" ")[1];
          odgovor =
              "Poslužitelj je pauziran! Broj obrađenih zahtjeva za udaljenost: " + brojZahtjeva;
        } else
          odgovor = opis;
      }
    }

    if (komanda.contains("INFO")) {
      String vrsta = komanda.split(" ")[1];
      Status status = rkn.dajInfo(vrsta);


      if (status != null) {
        String opis = status.getOpis();

        if (opis.contains("OK")) {
          if (vrsta.equals("DA"))
            odgovor = "Ispis dozvoljen!";
          else
            odgovor = "Ispis prekinut!";
        } else
          odgovor = opis;
      }


    }

    model.put("odgovor", odgovor);
  }
}
