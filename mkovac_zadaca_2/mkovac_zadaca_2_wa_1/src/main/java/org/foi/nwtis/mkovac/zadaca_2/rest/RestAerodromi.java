package org.foi.nwtis.mkovac.zadaca_2.rest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.Udaljenost;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("aerodromi")
public class RestAerodromi {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome() {
    List<Aerodrom> aerodromi = new ArrayList<>();
    Aerodrom ad = new Aerodrom("LDZA", "Airport Zagreb", "HR", new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LDVA", "Airport Varaždin", "HR", new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDF", "Airport Frankfurt", "DE", new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDB", "Airport Berlin", "DE", new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LOWW", "Airport Vienna", "AT", new Lokacija("0", "0"));
    aerodromi.add(ad);

    var gson = new Gson();
    var jsonAerodromi = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(jsonAerodromi).build();
    return odgovor;
  }

  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  // TODO DORADITI!
  public Response dajAerodrom(@PathParam("icao") String icao) {
    List<Aerodrom> aerodromi = new ArrayList<>();
    Aerodrom ad = new Aerodrom("LDZA", "Airport Zagreb", "HR", new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LDVA", "Airport Varaždin", "HR", new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDF", "Airport Frankfurt", "DE", new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDB", "Airport Berlin", "DE", new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LOWW", "Airport Vienna", "AT", new Lokacija("0", "0"));
    aerodromi.add(ad);

    Aerodrom aerodrom = null;

    for (Aerodrom a : aerodromi) {
      if (a.getIcao().compareTo(icao) == 0) {
        aerodrom = a;
        break;
      }
    }

    if (aerodrom == null) {
      return Response.status(404).build();
    } else {
      var gson = new Gson();
      var jsonAerodrmi = gson.toJson(aerodrom);
      var odgovor = Response.ok().entity(jsonAerodrmi).build();

      return odgovor;
    }
  }

  @Path("{icaoOd}/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiAerodroma(@PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo) {
    // vjezba_06_4:

    var udaljenosti = new ArrayList<Udaljenost>();

    String query =
        "SELECT ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? AND ICAO_TO = ?";

    // Connection con = null;
    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) { // ODRADUJE SPAJANJE PREKO
                                         // glassfish-resources.xml
                                         // UMJESTO RUCNOG KAK SMO
                                         // IMALI U 06_4
      stmt = con.prepareStatement(query);
      stmt.setString(1, icaoFrom);
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

    } catch (SQLException e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();

      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }
    }

    var gson = new Gson();
    var jsonAerodromi = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonAerodromi).build();
    return odgovor;
  }



}