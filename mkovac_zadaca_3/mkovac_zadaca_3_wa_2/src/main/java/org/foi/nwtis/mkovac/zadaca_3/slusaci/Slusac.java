package org.foi.nwtis.mkovac.zadaca_3.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.Getter;

/**
 * Slušač za MVC aplikaciju
 * 
 * @author Marijan Kovač
 *
 */
@WebListener
public class Slusac implements ServletContextListener {
  @Getter
  private static ServletContext servletContext;

  private Konfiguracija konfig;

  /**
   * Osluškuje pokretanje aplikacije te vrši učitavanje konfiguracijskih postavki.
   * 
   * @param sce Događaj servlet konteksta
   * 
   */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    servletContext = sce.getServletContext();

    try {
      ucitajPostavke();
      Logger.getGlobal().log(Level.INFO, "Postavke uspješno učitane!");
    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
      return;
    }
  }

  /**
   * Osluškuje gašenje aplikacije te gasi servlet kontekst.
   * 
   * @param sce Događaj servlet konteksta
   * 
   */
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContextListener.super.contextDestroyed(sce);
  }

  /**
   * Učitava postavke iz datoteke u objekt Konfiguracija te dodaje objekt atributu Servlet konteksta
   */
  private void ucitajPostavke() throws NeispravnaKonfiguracija {
    String nazivDatoteke =
        servletContext.getRealPath(servletContext.getInitParameter("konfiguracija"));

    konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);

    servletContext.setAttribute("konfig", konfig);
  }
}
