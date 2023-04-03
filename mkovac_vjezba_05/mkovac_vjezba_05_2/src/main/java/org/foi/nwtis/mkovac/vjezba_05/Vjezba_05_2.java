package org.foi.nwtis.mkovac.vjezba_05;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.KonfiguracijaBP;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.PostavkeBazaPodataka;

public class Vjezba_05_2 {

  protected KonfiguracijaBP konfBP;

  public static void main(String[] args) {
    var vjezba = new Vjezba_05_2();
    if (!vjezba.provjeriArgumente(args)) {
      Logger.getGlobal().log(Level.SEVERE, "Mora biti upisan jedan argument!");
      return;
    }
    vjezba.konfBP = new PostavkeBazaPodataka(args[0]);
    try {
      vjezba.konfBP.ucitajKonfiguraciju();
      vjezba.ispisKonfiguracije();
      String icaoForm = args[1];
      String icaoTo = args[2];

      vjezba.ispisiUdaljenosti(icaoForm, icaoTo);
    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  private void ispisiUdaljenosti(String icaoForm, String icaoTo) {
    String url = this.konfBP.getServerDatabase();
    String korime = this.konfBP.getUserUsername();
    String lozinka = this.konfBP.getUserPassword();
    String driver = this.konfBP.getDriverDatabase();
    String baza = this.konfBP.getUserDatabase();

    String query =
        "SELECT ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? AND ICAO_TO = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      Class.forName(driver); // čita driver prije nego s driver maanagerom idemo citati konekciju

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
        System.out.println(drzava + ": " + udaljenost);
      }
      System.out.println("Ukupna udaljenost: " + ukupnoUdaljenost);
    } catch (SQLException | ClassNotFoundException e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();

        if (con != null && !con.isClosed())
          con.close();

      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }
    }

  }

  private void ispisKonfiguracije() {
    Logger.getGlobal().log(Level.INFO, "Baza: " + this.konfBP.getUserDatabase());
    Logger.getGlobal().log(Level.INFO, "Korime: " + this.konfBP.getUserUsername());
    Logger.getGlobal().log(Level.INFO, "Lozinka: " + this.konfBP.getUserPassword());
    Logger.getGlobal().log(Level.INFO, "Putanja: " + this.konfBP.getServerDatabase());
    Logger.getGlobal().log(Level.INFO, "Driver: " + this.konfBP.getDriverDatabase());
  }

  private boolean provjeriArgumente(String[] args) {
    return (args.length == 3) ? true : false;
  }



}
