package org.foi.nwtis.mkovac.aplikacija_4.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.mkovac.aplikacija_4.klijenti.RestKlijentNadzor;
import org.foi.nwtis.podaci.Status;
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

  /**
   * Osluškuje pokretanje aplikacije te vrši učitavanje konfiguracijskih postavki.
   * 
   * @param sce Događaj servlet konteksta
   * 
   */
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

    if (!provjeriStatusPosluzitelja()) {
      Logger.getGlobal().log(Level.SEVERE, "Poslužitelj AP1 nije aktivan, prekidam rad!");
      throw new RuntimeException("Poslužitelj AP1 nije aktivan, prekidam rad!");
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
}
