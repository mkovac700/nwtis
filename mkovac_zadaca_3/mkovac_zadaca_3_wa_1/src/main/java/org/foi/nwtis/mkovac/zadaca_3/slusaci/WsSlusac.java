package org.foi.nwtis.mkovac.zadaca_3.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.mkovac.zadaca_3.dretve.SakupljacLetovaAviona;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.Getter;

/**
 * Slušač za Web servis
 * 
 * @author Marijan Kovač
 *
 */
@WebListener
public class WsSlusac implements ServletContextListener {

  @Getter
  private static ServletContext servletContext;

  private Konfiguracija konfig;

  private SakupljacLetovaAviona sakupljacLetovaAviona;

  /**
   * Osluškuje pokretanje aplikacije, vrši učitavanje konfiguracijskih postavki te pokreće dretvu
   * SakupljacLetovaAviona.
   * 
   * @param sce Događaj servlet konteksta
   * 
   */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    servletContext = sce.getServletContext();

    ucitajPostavke();

    pokreniSakupljacLetovaAviona();
  }

  private void pokreniSakupljacLetovaAviona() {
    // TODO Auto-generated method stub
    sakupljacLetovaAviona = new SakupljacLetovaAviona(konfig);
    sakupljacLetovaAviona.start();
  }

  /**
   * Osluškuje gašenje aplikacije te gasi servlet kontekst.
   * 
   * @param sce Događaj servlet konteksta
   * 
   */
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // TODO Auto-generated method stub
    ServletContextListener.super.contextDestroyed(sce);

    // TODO zaustavi dretvu
  }

  /**
   * Učitava postavke iz datoteke u objekt Konfiguracija te dodaje objekt atributu Servlet konteksta
   */
  private void ucitajPostavke() {
    // TODO napraviti throw new i preseliti gore samo try catch dio, tako da se ne poziva pokretanje
    // dretve ako je ucitavanje postavki bilo neuspjesno
    String nazivDatoteke =
        servletContext.getRealPath(servletContext.getInitParameter("konfiguracija"));

    try {
      konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
      return;
    }
    servletContext.setAttribute("konfig", konfig);
    Logger.getGlobal().log(Level.INFO, "Postavke uspješno učitane!");

  }
}
