package org.foi.nwtis.mkovac.aplikacija_5.mvc;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Controller
@Path("pocetna")
@RequestScoped
public class KontrolerPocetna {
  @Inject
  private Models model;

  /**
   * Metoda za prikaz početne stranice.
   */
  @GET
  @View("5.1.jsp")
  public void pocetak() {}
}
