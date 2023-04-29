package org.foi.nwtis.mkovac.zadaca_2.rest;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.RestServisSlusac;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("letovi")
@RequestScoped
public class RestLetovi {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  ServletContext context = RestServisSlusac.getServletContext();
  Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response test() {
    String korisnik = konf.dajPostavku("OpenSkyNetwork.korisnik");
    String lozinka = konf.dajPostavku("OpenSkyNetwork.lozinka");

    List<String> temp = new ArrayList<String>();
    temp.add(korisnik);
    temp.add(lozinka);

    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(temp);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }

  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajLetoveAerodromeNaDan(@PathParam("icao") String icao,
      @QueryParam("dan") String dan, @QueryParam("odBroja") String odBroja,
      @QueryParam("broj") String broj) {



    return null;
  }
}
