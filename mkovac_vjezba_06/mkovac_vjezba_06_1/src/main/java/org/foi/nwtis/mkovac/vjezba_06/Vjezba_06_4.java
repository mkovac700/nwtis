package org.foi.nwtis.mkovac.vjezba_06;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.PostavkeBazaPodataka;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Vjezba_06_1
 */
@WebServlet(name = "Vjezba_06_4", urlPatterns = {"/Vjezba_06_4"},
    initParams = {@WebInitParam(name = "konfiguracija",
        value = "NWTiS.db.config_1.xml")})
public class Vjezba_06_4 extends HttpServlet {


  private static final long serialVersionUID = -67067521182996033L;
  private PostavkeBazaPodataka konfBP;

  /**
   * @see HttpServlet#service(HttpServletRequest request,
   *      HttpServletResponse response)
   */
  protected void service(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
    super.service(request, response);
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request,
   *      HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {

    String icaoFrom = request.getParameter("icaoFrom");
    String icaoTo = request.getParameter("icaoTo");

    ispisiUdaljenosti(icaoFrom, icaoTo, request, response);


  }

  @Override
  public void init() throws ServletException {
    super.init();

    var nazivDatoteke =
        this.getServletContext().getRealPath("/WEB-INF/")
            + this.getInitParameter("konfiguracija");

    this.konfBP = new PostavkeBazaPodataka(nazivDatoteke);
    try {
      this.konfBP.ucitajKonfiguraciju();
      this.ispisKonfiguracije();
    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  private void ispisKonfiguracije() {
    Logger.getGlobal().log(Level.INFO,
        "Baza: " + this.konfBP.getUserDatabase());
    Logger.getGlobal().log(Level.INFO,
        "Korime: " + this.konfBP.getUserUsername());
    Logger.getGlobal().log(Level.INFO,
        "Lozinka: " + this.konfBP.getUserPassword());
    Logger.getGlobal().log(Level.INFO,
        "Putanja: " + this.konfBP.getServerDatabase());
    Logger.getGlobal().log(Level.INFO,
        "Driver: " + this.konfBP.getDriverDatabase());
  }

  private void ispisiUdaljenosti(String icaoForm, String icaoTo,
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String url = this.konfBP.getServerDatabase();
    String korime = this.konfBP.getUserUsername();
    String lozinka = this.konfBP.getUserPassword();
    String driver = this.konfBP.getDriverDatabase();
    String baza = this.konfBP.getUserDatabase();

    var udaljenosti = new ArrayList<Udaljenost>();

    String query =
        "SELECT ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? AND ICAO_TO = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      Class.forName(driver); // čita driver prije nego s driver
                             // maanagerom idemo citati konekciju

      con = DriverManager.getConnection(url + baza, korime, lozinka);
      stmt = con.prepareStatement(query);
      stmt.setString(1, icaoForm);
      stmt.setString(2, icaoTo);

      ResultSet rs = stmt.executeQuery();
      float ukupnoUdaljenost = 0;
      while (rs.next()) {
        String drzava = rs.getString("COUNTRY");
        float udaljenost = rs.getFloat("DIST_CTRY");
        ukupnoUdaljenost += udaljenost;

        var u = new Udaljenost(drzava, udaljenost);
        udaljenosti.add(u);

      }
      rs.close();

    } catch (SQLException | ClassNotFoundException e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());

      request.setAttribute("greska", e.getMessage());
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();

        if (con != null && !con.isClosed())
          con.close();

      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        request.setAttribute("greska", e.getMessage());
      }
    }

    // salje podatke
    request.setAttribute("podaci", udaljenosti);
    RequestDispatcher rd =
        this.getServletContext().getRequestDispatcher("/ispis2.jsp");
    rd.forward(request, response);
  }
}
