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
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzava;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("aerodromi")
public class RestAerodromi {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome(@QueryParam("odBroja") String odBroja,
      @QueryParam("broj") String broj) {

    int offset = 1, limit = 20;

    if ((odBroja != null && !odBroja.isEmpty())
        && (broj != null && !broj.isEmpty())) {
      try {
        offset = Integer.parseInt(odBroja);
        limit = Integer.parseInt(broj);
      } catch (NumberFormatException e) {
        return Response.status(404, "Neispravni parametri").build();
      }
    }

    List<Aerodrom> aerodromi = new ArrayList<>();

    String query =
        "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS LIMIT ? OFFSET ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setInt(1, limit);
      stmt.setInt(2, offset - 1);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String[] koordinate = rs.getString(4).split(",");

        Aerodrom a =
            new Aerodrom(rs.getString(1), rs.getString(2), rs.getString(3),
                new Lokacija(koordinate[0].trim(), koordinate[1].trim()));

        aerodromi.add(a);
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

    // TODO mozda najbolje izbacit van nakon testiranja
    if (aerodromi.isEmpty())
      return Response.status(404, "Lista je prazna").build();

    var gson = new Gson();
    var jsonAerodromi = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(jsonAerodromi).build();
    return odgovor;
  }

  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom(@PathParam("icao") String icao) {
    Aerodrom aerodrom = null;

    String query =
        "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ICAO = ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String[] koordinate = rs.getString(4).split(",");

        aerodrom =
            new Aerodrom(rs.getString(1), rs.getString(2), rs.getString(3),
                new Lokacija(koordinate[0].trim(), koordinate[1].trim()));
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

    // TODO mozda najbolje izbacit van nakon testiranja
    if (aerodrom == null)
      return Response.status(404, "Nema podatka za dani upit").build();

    var gson = new Gson();
    var jsonAerodrom = gson.toJson(aerodrom);
    var odgovor = Response.ok().entity(jsonAerodrom).build();
    return odgovor;
  }

  @GET
  @Path("{icaoOd}/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeđuDvaAerodroma(
      @PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo) {
    // vjezba_06_4:

    var udaljenosti = new ArrayList<Udaljenost>();

    String query =
        "SELECT ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? AND ICAO_TO = ?";

    // Connection con = null;
    // ODRADUJE SPAJANJE PREKO glassfish-resources.xml UMJESTO RUCNOG KAK SMO IMALI U 06_4
    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
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
    var jsonUdaljenosti = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonUdaljenosti).build();
    return odgovor;
  }

  @GET
  @Path("{icao}/udaljenosti")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeđuSvihAerodroma(
      @PathParam("icao") String icao, @QueryParam("odBroja") String odBroja,
      @QueryParam("broj") String broj) {

    List<UdaljenostAerodrom> udaljenosti = new ArrayList<>();

    int offset = 1, limit = 20;

    if ((odBroja != null && !odBroja.isEmpty())
        && (broj != null && !broj.isEmpty())) {
      offset = Integer.parseInt(odBroja);
      limit = Integer.parseInt(broj);
    }

    String query =
        "SELECT DISTINCT ICAO_FROM, ICAO_TO, DIST_TOT FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? LIMIT ? OFFSET ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);
      stmt.setInt(2, limit);
      stmt.setInt(3, offset - 1);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String icaoTo = rs.getString("ICAO_TO");
        float udaljenost = rs.getFloat("DIST_TOT");

        var u = new UdaljenostAerodrom(icaoTo, udaljenost);
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

    if (udaljenosti.isEmpty())
      return Response.status(404, "Lista je prazna").build();

    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonUdaljenosti).build();
    return odgovor;
  }

  @GET
  @Path("{icao}/najduljiPutDrzave")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajNajduljiPutDrzave(@PathParam("icao") String icao) {

    UdaljenostAerodromDrzava udaljenost = null;

    String query =
        "SELECT ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? ORDER BY DIST_CTRY DESC LIMIT 1";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String icaoTo = rs.getString("ICAO_TO");
        String drzava = rs.getString("COUNTRY");
        float km = rs.getFloat("DIST_CTRY");

        udaljenost = new UdaljenostAerodromDrzava(icaoTo, drzava, km);

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
    var jsonUdaljenost = gson.toJson(udaljenost);
    var odgovor = Response.ok().entity(jsonUdaljenost).build();
    return odgovor;
  }

}
