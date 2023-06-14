package org.foi.nwtis.mkovac.aplikacija_2.rest;

import java.util.ArrayList;
import java.util.List;
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
import jakarta.ws.rs.QueryParam;
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
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome(@QueryParam("traziNaziv") String traziNaziv,
      @QueryParam("traziDrzavu") String traziDrzavu, @QueryParam("odBroja") String odBroja,
      @QueryParam("broj") String broj) {

    int offset = 1, limit = 20;

    if ((odBroja != null && !odBroja.isEmpty()) && (broj != null && !broj.isEmpty())) {
      try {
        offset = Integer.parseInt(odBroja);
        limit = Integer.parseInt(broj);
        if (offset < 1 || limit < 1)
          return Response.status(400, "Neispravni parametri").build();
      } catch (NumberFormatException e) {
        return Response.status(400, "Neispravni parametri").build();
      }
    }

    List<Aerodrom> aerodromi = new ArrayList<>();
    List<Airports> airports = airportFacade.findAll(traziNaziv, traziDrzavu, offset - 1, limit);

    for (Airports a : airports) {
      var koord = a.getCoordinates().split(", ");
      var lokacija = new Lokacija(koord[1], koord[0]);
      aerodromi.add(new Aerodrom(a.getIcao(), a.getName(), a.getIsoCountry(), lokacija));
    }

    var gson = new Gson();
    var jsonAerodrom = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(jsonAerodrom).build();
    return odgovor;
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
