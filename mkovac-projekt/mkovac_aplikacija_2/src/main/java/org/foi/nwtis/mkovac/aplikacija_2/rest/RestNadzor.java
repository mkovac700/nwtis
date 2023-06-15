package org.foi.nwtis.mkovac.aplikacija_2.rest;

import org.foi.nwtis.mkovac.aplikacija_2.klijenti.AP1Klijent;
import org.foi.nwtis.podaci.Status;
import com.google.gson.Gson;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST servis za nadzor
 * 
 * @author Marijan Kovač
 *
 */
@RequestScoped
@Path("nadzor")
public class RestNadzor {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajStatus() {
    Response response = null;

    Status status = null;

    AP1Klijent ap1Klijent = new AP1Klijent();

    String odgovor = ap1Klijent.posaljiZahtjev("STATUS");

    if (odgovor.contains("OK")) {
      status = new Status(200, odgovor);
      var gson = new Gson();
      var jsonStatus = gson.toJson(status);
      response = Response.ok().entity(jsonStatus).build();
    } else {
      response = Response.status(400, odgovor).build();
    }

    return response;
  }

  @GET
  @Path("{komanda}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response saljiKomandu(@PathParam("komanda") String komanda) {
    Response response;

    Status status = null;

    AP1Klijent ap1Klijent = new AP1Klijent();

    String odgovor = ap1Klijent.posaljiZahtjev(komanda);

    if (odgovor.contains("OK")) {
      status = new Status(200, odgovor);
      var gson = new Gson();
      var jsonStatus = gson.toJson(status);
      response = Response.ok().entity(jsonStatus).build();
    } else {
      response = Response.status(400, odgovor).build();
    }

    return response;
  }

  @GET
  @Path("INFO/{vrsta}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response saljiInfo(@PathParam("vrsta") String vrsta) {
    Response response;

    Status status = null;

    AP1Klijent ap1Klijent = new AP1Klijent();

    String odgovor = ap1Klijent.posaljiZahtjev("INFO " + vrsta);

    if (odgovor.contains("OK")) {
      status = new Status(200, odgovor);
      var gson = new Gson();
      var jsonStatus = gson.toJson(status);
      response = Response.ok().entity(jsonStatus).build();
    } else {
      response = Response.status(400, odgovor).build();
    }

    return response;
  }
}
