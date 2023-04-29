package org.foi.nwtis.mkovac.zadaca_2.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class RestServisSlusac implements ServletContextListener {
  private static ServletContext servletContext;

  public static ServletContext getServletContext() {
    return servletContext;
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    servletContext = sce.getServletContext();
    String nazivDatoteke =
        servletContext.getRealPath(servletContext.getInitParameter("konfiguracija"));
    Konfiguracija konfig = null;
    try {
      konfig = ucitajPostavke(nazivDatoteke);
    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
      return;
    }
    servletContext.setAttribute("konfig", konfig);
    Logger.getGlobal().log(Level.INFO, "Postavke uspješno učitane!");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // TODO Auto-generated method stub
    ServletContextListener.super.contextDestroyed(sce);
  }

  /**
   * Učitava postavke iz datoteke u objekt Konfiguracija
   * 
   * @param nazivDatoteke Datoteka s konfiguracijskim postavkama
   * @return Vraća objekt tipa Konfiguracija
   * @throws NeispravnaKonfiguracija Baca iznimku ako učitavanje postavki nije uspjelo
   */
  private Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);

  }
}
