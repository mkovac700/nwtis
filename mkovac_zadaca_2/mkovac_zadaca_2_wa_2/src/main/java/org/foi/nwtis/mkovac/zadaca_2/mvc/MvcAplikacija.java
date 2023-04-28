package org.foi.nwtis.mkovac.zadaca_2.mvc;


import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.MvcAplikacijaSlusac;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("mvc")
public class MvcAplikacija extends Application {

  ServletContext context = MvcAplikacijaSlusac.getServletContext();
  Konfiguracija konf = (Konfiguracija) context.getAttribute("config");

}
