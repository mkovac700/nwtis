package org.foi.nwtis.mkovac.aplikacija_2.rest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_2.jpa.Airports;
import org.foi.nwtis.mkovac.aplikacija_2.zrna.AirportFacade;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST servis za aerodrome
 * 
 * @author Marijan Kovač
 *
 */
@RequestScoped
@Path("aerodromi")
public class RestAerodromi {

  @Inject
  AirportFacade airportFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response test() {
    return Response.ok().entity("test").build();
  }

  /**
   * Vraća podatak o zadanom aerodromu.
   * 
   * @param icao Oznaka aerodroma
   * @return Vraća tražene podatke ili odgovor (pogrešku) sa statusnim kodom i opisom.
   */
  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom(@PathParam("icao") String icao) {
    Aerodrom aerodrom = null;

    String query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ICAO = ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String[] koordinate = rs.getString(4).split(",");

        aerodrom = new Aerodrom(rs.getString(1), rs.getString(2), rs.getString(3),
            new Lokacija(koordinate[1].trim(), koordinate[0].trim()));
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

    if (aerodrom == null)
      return Response.status(404, "Nema podatka!").build();

    var gson = new Gson();
    var jsonAerodrom = gson.toJson(aerodrom);
    var odgovor = Response.ok().entity(jsonAerodrom).build();
    return odgovor;
  }

  @GET
  @Path("2/{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom2(@PathParam("icao") String icao) {
    Aerodrom aerodrom = null;
    if (icao == null || icao.trim().length() == 0) {
      return Response.status(404, "Nema podatka!").build();
    }
    Airports a = airportFacade.find(icao);
    if (a != null) {
      var koord = a.getCoordinates().split(", ");
      var lokacija = new Lokacija(koord[1], koord[0]);
      aerodrom = new Aerodrom(a.getIcao(), a.getName(), a.getIsoCountry(), lokacija);
    }

    var gson = new Gson();
    var jsonAerodrom = gson.toJson(aerodrom);
    var odgovor = Response.ok().entity(jsonAerodrom).build();
    return odgovor;
  }
}
