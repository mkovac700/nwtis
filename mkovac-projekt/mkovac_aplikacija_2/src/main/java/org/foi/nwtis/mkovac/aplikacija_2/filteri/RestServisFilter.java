package org.foi.nwtis.mkovac.aplikacija_2.filteri;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_2.jpa.Dnevnik;
import org.foi.nwtis.mkovac.aplikacija_2.klijenti.AP1Klijent;
import org.foi.nwtis.mkovac.aplikacija_2.zrna.DnevnikFacade;
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

@WebFilter(filterName = "AP2", urlPatterns = "/*")
public class RestServisFilter implements Filter {

  private String filterName;

  private HttpServletRequest request;
  private HttpServletResponse response;

  private String putanja;
  private String metoda;
  private String parametri;
  private boolean AP1_status;

  @Inject
  DnevnikFacade dnevnikFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterName = filterConfig.getFilterName();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    this.AP1_status = provjeriStatusPosluzitelja();

    this.request = (HttpServletRequest) request;
    this.response = (HttpServletResponse) response;
    this.putanja = this.request.getRequestURI();
    this.metoda = this.request.getMethod();

    this.parametri = dajParametreUpita();

    evidentirajZahtjev();

    if (!this.AP1_status) { // server neaktivan
      Logger.getGlobal().log(Level.INFO, "server neaktivan");
      if (this.putanja.contains("/aerodromi") || this.putanja.contains("/dnevnik")) {
        this.response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
      } else if (this.putanja.contains("/nadzor")) {
        chain.doFilter(request, response);
        return;
      } else {
        this.response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
    } else { // server aktivan
      Logger.getGlobal().log(Level.INFO, "server aktivan");
      chain.doFilter(request, response);
      return;
    }
  }

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

  private boolean provjeriStatusPosluzitelja() {

    AP1Klijent ap1Klijent = new AP1Klijent();
    String odgovor = ap1Klijent.posaljiZahtjev("STATUS");

    if (odgovor.equals("OK 1"))
      return true;
    else
      return false;
  }

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
