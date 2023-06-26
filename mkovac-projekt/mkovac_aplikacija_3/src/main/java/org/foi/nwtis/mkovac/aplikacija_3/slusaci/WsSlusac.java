package org.foi.nwtis.mkovac.aplikacija_3.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.mkovac.aplikacija_3.dretve.SakupljacLetovaAviona;
import org.foi.nwtis.mkovac.aplikacija_3.klijenti.RestKlijentNadzor;
import org.foi.nwtis.mkovac.aplikacija_3.zrna.AerodromiLetoviFacade;
import org.foi.nwtis.mkovac.aplikacija_3.zrna.AirportFacade;
import org.foi.nwtis.mkovac.aplikacija_3.zrna.JmsPosiljatelj;
import org.foi.nwtis.mkovac.aplikacija_3.zrna.LetoviPolasciFacade;
import org.foi.nwtis.podaci.Status;
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

  @Inject
  AerodromiLetoviFacade aerodromiLetoviFacade;

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

    if (!provjeriStatusPosluzitelja()) {
      Logger.getGlobal().log(Level.SEVERE, "Poslužitelj AP1 nije aktivan, prekidam rad!");
      throw new RuntimeException("Poslužitelj AP1 nije aktivan, prekidam rad!");
    }

    pokreniSakupljacLetovaAviona();
  }

  /**
   * Provjerava status poslužitelja AP1.
   * 
   * @return True ako je aktivan, inače false.
   */
  private boolean provjeriStatusPosluzitelja() {
    RestKlijentNadzor rkn = new RestKlijentNadzor();
    Status status = rkn.dajStatus();

    boolean s = false;
    if (status != null && status.getOpis().split(" ")[1].equals("1"))
      s = true;

    return s;
  }

  /**
   * Obavlja pokretanje dretve za preuzimanje letova aviona
   */
  private void pokreniSakupljacLetovaAviona() {
    sakupljacLetovaAviona = new SakupljacLetovaAviona(konfig, letoviPolasciFacade, airportFacade,
        aerodromiLetoviFacade, jmsPosiljatelj);
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
    String nazivDatoteke =
        servletContext.getRealPath(servletContext.getInitParameter("konfiguracija"));

    konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);

    servletContext.setAttribute("konfig", konfig);
  }
}
