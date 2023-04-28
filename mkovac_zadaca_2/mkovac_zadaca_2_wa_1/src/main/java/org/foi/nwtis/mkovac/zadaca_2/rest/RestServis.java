package org.foi.nwtis.mkovac.zadaca_2.rest;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.RestServisSlusac;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("api") // putanja do servisa
public class RestServis extends Application {
  ServletContext context = RestServisSlusac.getServletContext();
  Konfiguracija konf = (Konfiguracija) context.getAttribute("config");
}
