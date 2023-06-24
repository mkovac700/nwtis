package org.foi.nwtis.mkovac.aplikacija_5.mvc;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Controller
@Path("korisnici")
@RequestScoped
public class KontrolerKorisnika {

  @Inject
  private Models model;

  /**
   * Metoda za prikaz početne stranice.
   */
  @GET
  @View("index.jsp")
  public void pocetak() {}
}
