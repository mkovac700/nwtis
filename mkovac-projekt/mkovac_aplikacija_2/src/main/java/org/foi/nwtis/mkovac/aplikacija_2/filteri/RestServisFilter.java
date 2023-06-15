package org.foi.nwtis.mkovac.aplikacija_2.filteri;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_2.klijenti.AP1Klijent;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class RestServisFilter implements Filter {

  private HttpServletRequest request;
  private HttpServletResponse response;
  private String putanja;
  private boolean AP1_status;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    this.AP1_status = provjeriStatusPosluzitelja();

    this.request = (HttpServletRequest) request;
    this.response = (HttpServletResponse) response;
    this.putanja = this.request.getRequestURI().substring(this.request.getContextPath().length());

    Logger.getGlobal().log(Level.INFO, "putanja: " + this.putanja);

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
    // TODO Auto-generated method stub

  }

  private boolean provjeriStatusPosluzitelja() {

    AP1Klijent ap1Klijent = new AP1Klijent();
    String odgovor = ap1Klijent.posaljiZahtjev("STATUS");

    if (odgovor.equals("OK 1"))
      return true;
    else
      return false;
  }

}
