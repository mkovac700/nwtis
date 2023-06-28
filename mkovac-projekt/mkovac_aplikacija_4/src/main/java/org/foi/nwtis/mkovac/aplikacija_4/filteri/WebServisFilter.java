package org.foi.nwtis.mkovac.aplikacija_4.filteri;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.Dnevnik;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.DnevnikFacade;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Klasa za filtriranje i evidentiranje HTTP zahtjeva
 * 
 * @author Marijan Kovač
 *
 */
@WebFilter(filterName = "AP4", urlPatterns = "/*")
public class WebServisFilter implements Filter {

  private String filterName;

  private HttpServletRequest request;
  private HttpServletResponse response;

  private String putanja;
  private String metoda;
  private String parametri;

  @Inject
  DnevnikFacade dnevnikFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Obavlja inicijalizaciju postavki i filtera
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterName = filterConfig.getFilterName();
  }

  /**
   * Osluškuje dolazak zahtjeva i zatim evidentira zahtjev u bazi podataka.
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    this.request = (HttpServletRequest) request;
    this.response = (HttpServletResponse) response;
    this.putanja = this.request.getRequestURI();
    this.metoda = this.request.getMethod();

    this.parametri = dajParametreUpita();

    evidentirajZahtjev();

    chain.doFilter(request, response);
  }

  /**
   * Sprema zahtjev u bazu podataka. Sprema se putanja zahtjeva zajedno s parametrima upita, metoda
   * zahtjeva, vrsta filtera te vremenska oznaka.
   */
  private void evidentirajZahtjev() {
    StringBuilder sb = new StringBuilder();

    if (this.parametri.isEmpty())
      sb.append(this.putanja);
    else
      sb.append(this.putanja).append("?").append(this.parametri);

    String zahtjev = sb.toString();

    Logger.getGlobal().log(Level.INFO, "Zahtjev: " + zahtjev);

    Dnevnik dnevnik = new Dnevnik();

    dnevnik.setZahtjev(zahtjev);
    dnevnik.setVrsta(filterName);
    dnevnik.setVremenskaOznaka(Timestamp.from(Instant.now()));
    dnevnik.setMetoda(metoda);
    dnevnikFacade.create(dnevnik);
  }

  /**
   * Izdvaja parametre upita iz zahtjeva te ih spaja u jedinstveni niz znakova.
   * 
   * @return Niz znakova u obliku parametara upita
   */
  private String dajParametreUpita() {

    Map<String, String[]> parameterMap = this.request.getParameterMap();

    StringBuilder sb = new StringBuilder();

    int brojac = 0;
    int broj = parameterMap.size();

    for (Entry<String, String[]> entry : parameterMap.entrySet()) {
      String parameterName = entry.getKey();
      String[] parameterValues = entry.getValue();

      sb.append(parameterName).append("=").append(parameterValues[0]);
      if (++brojac < broj)
        sb.append("&");
    }

    return sb.toString();
  }

}
