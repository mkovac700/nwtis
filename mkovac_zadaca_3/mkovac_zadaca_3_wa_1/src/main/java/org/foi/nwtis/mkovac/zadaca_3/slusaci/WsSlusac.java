package org.foi.nwtis.mkovac.zadaca_3.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.mkovac.zadaca_3.dretve.SakupljacLetovaAviona;
import org.foi.nwtis.mkovac.zadaca_3.zrna.AirportFacade;
import org.foi.nwtis.mkovac.zadaca_3.zrna.JmsPosiljatelj;
import org.foi.nwtis.mkovac.zadaca_3.zrna.LetoviPolasciFacade;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
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

  @Inject
  LetoviPolasciFacade letoviPolasciFacade;

  @Inject
  AirportFacade airportFacade;

  @EJB
  JmsPosiljatelj jmsPosiljatelj;

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

    try {
      ucitajPostavke();
      Logger.getGlobal().log(Level.INFO, "Postavke uspješno učitane!");
    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
      return;
    }

    pokreniSakupljacLetovaAviona();
  }

  private void pokreniSakupljacLetovaAviona() {
    sakupljacLetovaAviona =
        new SakupljacLetovaAviona(konfig, letoviPolasciFacade, airportFacade, jmsPosiljatelj);
    sakupljacLetovaAviona.start();
    Logger.getGlobal().log(Level.INFO, "SakupljacLetovaAviona - start");
  }

  /**
   * Osluškuje gašenje aplikacije, zaustavlja dretvu SakupljacLetovaAviona te gasi servlet kontekst.
   * 
   * @param sce Događaj servlet konteksta
   * 
   */
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    if (sakupljacLetovaAviona != null && sakupljacLetovaAviona.isAlive())
      sakupljacLetovaAviona.interrupt();

    ServletContextListener.super.contextDestroyed(sce);
  }

  /**
   * Učitava postavke iz datoteke u objekt Konfiguracija te dodaje objekt atributu Servlet konteksta
   */
  private void ucitajPostavke() throws NeispravnaKonfiguracija {
    // TODO napraviti throw new i preseliti gore samo try catch dio, tako da se ne poziva pokretanje
    // dretve ako je ucitavanje postavki bilo neuspjesno
    String nazivDatoteke =
        servletContext.getRealPath(servletContext.getInitParameter("konfiguracija"));

    konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);

    servletContext.setAttribute("konfig", konfig);
  }
}
