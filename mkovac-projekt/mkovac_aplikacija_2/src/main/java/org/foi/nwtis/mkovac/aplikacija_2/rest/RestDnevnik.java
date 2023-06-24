package org.foi.nwtis.mkovac.aplikacija_2.rest;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.mkovac.aplikacija_2.jpa.Dnevnik;
import org.foi.nwtis.mkovac.aplikacija_2.zrna.DnevnikFacade;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST servis za dnevnik
 * 
 * @author Marijan Kovač
 *
 */
@RequestScoped
@Path("dnevnik")
public class RestDnevnik {

  @Inject
  DnevnikFacade dnevnikFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajDnevnik(@QueryParam("vrsta") String vrsta,
      @QueryParam("odBroja") String odBroja, @QueryParam("broj") String broj) {

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

    List<Dnevnik> dnevnici = dnevnikFacade.findAll(vrsta, offset - 1, limit);
    List<org.foi.nwtis.podaci.Dnevnik> _dnevnici = new ArrayList<>();
    if (dnevnici != null && !dnevnici.isEmpty())
      dnevnici.forEach(d -> _dnevnici.add(new org.foi.nwtis.podaci.Dnevnik(d.getZahtjev(),
          d.getMetoda(), d.getVrsta(), d.getVremenskaOznaka())));

    var gson = new Gson();
    var jsonDnevnici = gson.toJson(_dnevnici);
    var odgovor = Response.ok().entity(jsonDnevnici).build();
    return odgovor;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response spremiDnevnik(String zapis) {

    Dnevnik dnevnik = null;
    Gson gson = new Gson();
    try {
      dnevnik = gson.fromJson(zapis, Dnevnik.class);
    } catch (JsonSyntaxException e) {
      return Response.status(404, e.getMessage()).build();
    }

    dnevnikFacade.create(dnevnik);

    return Response.ok().entity("Zapis uspješno dodan!").build();
  }
}
