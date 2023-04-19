package org.foi.nwtis.mkovac.zadaca_2.rest;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import com.google.gson.Gson;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("aerodromi")
public class RestAerodromi {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome() {
    List<Aerodrom> aerodromi = new ArrayList<>();
    Aerodrom ad = new Aerodrom("LDZA", "Airport Zagreb", "HR",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LDVA", "Airport Varaždin", "HR",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDF", "Airport Frankfurt", "DE",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDB", "Airport Berlin", "DE",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LOWW", "Airport Vienna", "AT",
        new Lokacija("0", "0"));
    aerodromi.add(ad);

    var gson = new Gson();
    var jsonAerodromi = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(jsonAerodromi).build();
    return odgovor;
  }

  public Response dajAerodrom(String icao) {
    return null;
  }

  public Response dajUdaljenostiAerodroma(String icaoFrom,
      String icaoTo) {
    return null;
  }
}
