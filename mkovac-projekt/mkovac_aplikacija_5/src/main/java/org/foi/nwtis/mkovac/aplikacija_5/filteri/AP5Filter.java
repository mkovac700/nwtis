package org.foi.nwtis.mkovac.aplikacija_5.filteri;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_5.klijenti.RestKlijentDnevnik;
import org.foi.nwtis.mkovac.aplikacija_5.slusaci.WsSlusac;
import org.foi.nwtis.podaci.Dnevnik;
import com.google.gson.Gson;
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
import jakarta.servlet.http.HttpSession;

/**
 * Klasa za filtriranje i evidentiranje HTTP zahtjeva
 * 
 * @author Marijan Kovač
 *
 */
@WebFilter(filterName = "AP5", urlPatterns = "/*")
public class AP5Filter implements Filter {

  private String filterName;

  private HttpServletRequest request;
  private HttpServletResponse response;

  private String putanja;
  private String metoda;
  private String parametri;

  @Inject
  private HttpSession session;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterName = filterConfig.getFilterName();
  }

  /**
   * Osluškuje dolazak zahtjeva i zatim provjerava trenutno prijavljenog korisnika. Ukoliko korisnik
   * nije prijavljen, dozvoljava pristup krajnjim točkama /korisnici, /korisnici/prijava i
   * korisnici/registracija, dok za sve ostale zahtjeve vrši preusmjeravanje na stranicu prijave.
   * Nakon prijave dozvoljava pristup ostalim zahtjevima. Svaki zahtjev evidentira se u bazi
   * podataka.
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

    String korisnik = (String) session.getAttribute("korisnik");

    String pocetna = WsSlusac.getServletContext().getContextPath();

    String prijava = pocetna + "/mvc/korisnici/prijava";

    if (korisnik != null && !korisnik.isEmpty()) { // ako je korisnik prijavljen, nastavi
      chain.doFilter(request, response);
    } else { // ako nije prijavljen
      // // System.out.println("korisnik:" + korisnik);
      // // System.out.println("putanja:" + putanja);
      // // System.out.println("pocetna:" + pocetna);

      if (putanja.endsWith("korisnici") || putanja.contains("korisnici/registracija")
          || putanja.contains("korisnici/prijava") || putanja.equals(pocetna + "/")) {
        chain.doFilter(request, response);
      } else {
        this.response.sendRedirect(prijava);
      }
    }

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

    var gson = new Gson();
    var jsonDnevnik = gson.toJson(dnevnik);

    RestKlijentDnevnik rkd = new RestKlijentDnevnik();

    try {
      rkd.spremiDnevnik(jsonDnevnik);
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    }
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
