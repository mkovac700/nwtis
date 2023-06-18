package org.foi.nwtis.mkovac.aplikacija_2.rest;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.mkovac.aplikacija_2.jpa.Airports;
import org.foi.nwtis.mkovac.aplikacija_2.klijenti.AP1Klijent;
import org.foi.nwtis.mkovac.aplikacija_2.zrna.AirportFacade;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
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

  /**
   * Vraća podatke o svim aerodromima u zadanom rasponu. Zadani raspon je 1, 20. Može imati
   * parametre traziNaziv (filtriranje po nazivu aerodroma) i/ili traziDrzavu (filtriranje po oznaci
   * države).
   * 
   * @param traziNaziv Za filtriranje po nazivu aerodroma
   * @param traziDrzavu Za filtriranje po oznaci države
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Vraća tražene podatke ili odgovor (pogrešku) sa statusnim kodom i opisom.
   */
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

  /**
   * Vraća podatke o udaljenosti između dva aerodroma.
   * 
   * @param icaoOd Oznaka polaznog aerodroma
   * @param icaoDo Oznaka dolaznog aerodroma
   * @return Vraća tražene podatke ili odgovor (pogrešku) sa statusnim kodom i opisom.
   */
  @GET
  @Path("{icaoOd}/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeduDvaAerodroma(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {

    if ((icaoOd == null || icaoOd.trim().length() == 0)
        || (icaoDo == null || icaoDo.trim().length() == 0))
      return null;

    var udaljenosti = new ArrayList<Udaljenost>();
    var distances = airportFacade.findDistances(icaoOd, icaoDo);

    for (Object[] o : distances) {
      udaljenosti.add(new Udaljenost(o[1].toString(), Float.parseFloat(o[0].toString())));
    }

    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonUdaljenosti).build();
    return odgovor;
  }

  /**
   * Vraća podatke o udaljenosti od zadanog aerodroma do svih ostalih aerodroma u zadanom rasponu.
   * Zadani raspon je 1, 20.
   * 
   * @param icao Oznaka aerodroma
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Vraća tražene podatke ili odgovor (pogrešku) sa statusnim kodom i opisom.
   */
  @GET
  @Path("{icao}/udaljenosti")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeđuSvihAerodroma(@PathParam("icao") String icao,
      @QueryParam("odBroja") String odBroja, @QueryParam("broj") String broj) {

    if (icao == null || icao.trim().length() == 0) {
      return Response.status(404, "Nema podatka!").build();
    }

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

    var udaljenosti = new ArrayList<UdaljenostAerodrom>();
    var distances = airportFacade.findDistances(icao, offset - 1, limit);

    for (Object[] o : distances) {
      udaljenosti.add(new UdaljenostAerodrom(o[1].toString(), Float.parseFloat(o[0].toString())));
    }

    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonUdaljenosti).build();
    return odgovor;

  }

  @GET
  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajIzracunUdaljenostiIzmeduDvaAerodroma(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {

    Response response;

    Airports a1 = airportFacade.find(icaoOd);
    Airports a2 = airportFacade.find(icaoDo);
    Lokacija l1 = null;
    Lokacija l2 = null;
    if (a1 != null && a2 != null) {
      var koord1 = a1.getCoordinates().split(", ");
      l1 = new Lokacija(koord1[1], koord1[0]);
      var koord2 = a2.getCoordinates().split(", ");
      l2 = new Lokacija(koord2[1], koord2[0]);
    } else {
      return Response.status(404, "Nema podatka!").build();
    }

    AP1Klijent ap1Klijent = new AP1Klijent();

    String odgovor = ap1Klijent.posaljiZahtjev(MessageFormat.format("UDALJENOST {0} {1} {2} {3}",
        new Object[] {l1.getLatitude(), l1.getLongitude(), l2.getLatitude(), l2.getLongitude()}));

    UdaljenostAerodrom udaljenost = new UdaljenostAerodrom();

    if (odgovor.contains("OK")) {
      float km = Float.parseFloat(odgovor.split(" ")[1]);
      udaljenost = new UdaljenostAerodrom(a2.getIcao(), km);
      var gson = new Gson();
      var jsonStatus = gson.toJson(udaljenost);
      response = Response.ok().entity(jsonStatus).build();
    } else {
      response = Response.status(400, odgovor).build();
    }

    return response;
  }

  @GET
  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenost1(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {

    Airports a1 = airportFacade.find(icaoOd);
    Airports a2 = airportFacade.find(icaoDo);
    Lokacija l1 = null;
    Lokacija l2 = null;
    if (a1 != null && a2 != null) {
      var koord1 = a1.getCoordinates().split(", ");
      l1 = new Lokacija(koord1[1], koord1[0]);
      var koord2 = a2.getCoordinates().split(", ");
      l2 = new Lokacija(koord2[1], koord2[0]);
    } else {
      return Response.status(404, "Nema podatka!").build();
    }

    AP1Klijent ap1Klijent = new AP1Klijent();

    String odgovor = ap1Klijent.posaljiZahtjev(MessageFormat.format("UDALJENOST {0} {1} {2} {3}",
        new Object[] {l1.getLatitude(), l1.getLongitude(), l2.getLatitude(), l2.getLongitude()}));

    List<UdaljenostAerodrom> udaljenosti = new ArrayList<>();

    if (odgovor.contains("OK")) {
      float km = Float.parseFloat(odgovor.split(" ")[1]);

      List<Airports> airports = airportFacade.findAll(a2.getIsoCountry());

      for (Airports a : airports) {
        var koord = a.getCoordinates().split(", ");
        var lokacija = new Lokacija(koord[1], koord[0]);

        String odgovor2 = ap1Klijent.posaljiZahtjev(
            MessageFormat.format("UDALJENOST {0} {1} {2} {3}", new Object[] {l1.getLatitude(),
                l1.getLongitude(), lokacija.getLatitude(), lokacija.getLongitude()}));

        if (odgovor2.contains("OK")) {
          float km2 = Float.parseFloat(odgovor2.split(" ")[1]);
          if (km2 < km) {
            udaljenosti.add(new UdaljenostAerodrom(a.getIcao(), km2));
          }
        }
      }

      var gson = new Gson();
      var jsonUdaljenosti = gson.toJson(udaljenosti);
      return Response.ok().entity(jsonUdaljenosti).build();

    } else {
      return Response.status(400, odgovor).build();
    }
  }

  @GET
  @Path("{icaoOd}/udaljenost2")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenost2(@PathParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") String km) {

    if ((icaoOd == null || icaoOd.isEmpty()) || (drzava == null || drzava.isEmpty())
        || (km == null || km.isEmpty())) {
      return Response.status(400, "Neispravni parametri").build();
    }

    float km1;

    try {
      km1 = Float.parseFloat(km);
    } catch (NumberFormatException e) {
      return Response.status(400, "Neispravni parametri").build();
    }

    Airports a1 = airportFacade.find(icaoOd);
    Lokacija l1 = null;

    if (a1 != null) {
      var koord1 = a1.getCoordinates().split(", ");
      l1 = new Lokacija(koord1[1], koord1[0]);
    } else {
      return Response.status(404, "Nema podatka!").build();
    }

    List<Airports> airports = airportFacade.findAll(drzava);

    List<UdaljenostAerodrom> udaljenosti = new ArrayList<>();

    if (airports != null && !airports.isEmpty()) {
      AP1Klijent ap1Klijent = new AP1Klijent();

      for (Airports a : airports) {
        var koord = a.getCoordinates().split(", ");
        var lokacija = new Lokacija(koord[1], koord[0]);

        String odgovor = ap1Klijent.posaljiZahtjev(
            MessageFormat.format("UDALJENOST {0} {1} {2} {3}", new Object[] {l1.getLatitude(),
                l1.getLongitude(), lokacija.getLatitude(), lokacija.getLongitude()}));

        if (odgovor.contains("OK")) {
          float km2 = Float.parseFloat(odgovor.split(" ")[1]);
          if (km2 < km1) {
            udaljenosti.add(new UdaljenostAerodrom(a.getIcao(), km2));
          }
        }
      }

      var gson = new Gson();
      var jsonUdaljenosti = gson.toJson(udaljenosti);
      return Response.ok().entity(jsonUdaljenosti).build();
    } else {
      return Response.status(404, "Nema podatka!").build();
    }
  }

}
