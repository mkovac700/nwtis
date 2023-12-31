package org.foi.nwtis.mkovac.zadaca_2.rest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_2.slusaci.RestServisSlusac;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;
import org.foi.nwtis.rest.podaci.LetAvionaID;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST servis za letove
 * 
 * @author Marijan Kovač
 *
 */
@Path("letovi")
@RequestScoped
public class RestLetovi {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  private javax.sql.DataSource ds;

  private ServletContext context;
  private Konfiguracija konf;

  private String korisnik;
  private String lozinka;

  public RestLetovi() {
    context = RestServisSlusac.getServletContext();
    konf = (Konfiguracija) context.getAttribute("konfig");
    this.korisnik = konf.dajPostavku("OpenSkyNetwork.korisnik");
    this.lozinka = konf.dajPostavku("OpenSkyNetwork.lozinka");
  }

  /**
   * Vraća podatke o svim letovima s polaznog aerodroma na zadani dan u zadanom rasponu. Zadani
   * raspon je 1, 20.
   * 
   * Napomena: ne uključuje letove kojima je nepoznat odredišni aerodrom!
   * 
   * @param icao Oznaka polaznog aerodroma
   * @param dan Dan u formatu dd.MM.yyyy
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Vraća tražene podatke ili odgovor (pogrešku) sa statusnim kodom i opisom.
   */
  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajLetoveAerodromeNaDan(@PathParam("icao") String icao,
      @QueryParam("dan") String dan, @QueryParam("odBroja") String odBroja,
      @QueryParam("broj") String broj) {

    String regex =
        "(0[1-9]|[1-2][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19[7-9][0-9]|20[0-2][0-9]|203[0-7])";
    if (dan == null || dan.isEmpty() || !provjeriIzraz(dan, regex))
      return Response.status(400, "Dan nije unesen ili nije u formatu dd.mm.gggg").build();

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

    OSKlijent osKlijent = new OSKlijent(korisnik, lozinka);

    List<LetAviona> avioniPolasci = null;

    long[] odVremenaDoVremena = konvertirajOdVremenaDoVremena(dan);

    long odVremena = odVremenaDoVremena[0];
    long doVremena = odVremenaDoVremena[1];

    try {

      avioniPolasci = osKlijent.getDepartures(icao, odVremena, doVremena);
      avioniPolasci.removeIf(ap -> ap.getEstArrivalAirport() == null);

      if (offset > avioniPolasci.size())
        return Response.status(404, "Nema podataka").build();

      else if (offset - 1 + limit > avioniPolasci.size())
        avioniPolasci = avioniPolasci.subList(offset - 1, avioniPolasci.size());

      else
        avioniPolasci = avioniPolasci.subList(offset - 1, offset - 1 + limit);


    } catch (NwtisRestIznimka e) {
      return Response.status(404, e.getMessage()).build();
    }

    if (avioniPolasci == null)
      return Response.status(404, "Nema podataka").build();


    var gson = new Gson();
    var jsonAvioniPolasci = gson.toJson(avioniPolasci);
    var odgovor = Response.ok().entity(jsonAvioniPolasci).build();

    return odgovor;
  }

  /**
   * Vraća podatke o svim letovima od polaznog aerodroma do dolaznog aerodroma na zadani dan u
   * zadanom rasponu. Zadani raspon je 1, 20.
   * 
   * @param icaoOd Oznaka polaznog aerodroma
   * @param icaoDo Oznaka dolaznog aerodroma
   * @param dan Dan u formatu dd.MM.yyyy
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Vraća tražene podatke ili odgovor (pogrešku) sa statusnim kodom i opisom.
   */
  @GET
  @Path("{icaoOd}/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajLetoveOdDoAerodromaNaDan(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo, @QueryParam("dan") String dan,
      @QueryParam("odBroja") String odBroja, @QueryParam("broj") String broj) {

    String regex =
        "(0[1-9]|[1-2][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19[7-9][0-9]|20[0-2][0-9]|203[0-7])";
    if (dan == null || dan.isEmpty() || !provjeriIzraz(dan, regex))
      return Response.status(400, "Dan nije unesen ili nije u formatu dd.mm.gggg").build();

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

    OSKlijent osKlijent = new OSKlijent(korisnik, lozinka);

    List<LetAviona> avioniPolasci = null;

    long[] odVremenaDoVremena = konvertirajOdVremenaDoVremena(dan);

    long odVremena = odVremenaDoVremena[0];
    long doVremena = odVremenaDoVremena[1];

    try {

      avioniPolasci = osKlijent.getDepartures(icaoOd, odVremena, doVremena);
      avioniPolasci.removeIf(
          ap -> ap.getEstArrivalAirport() == null || !ap.getEstArrivalAirport().equals(icaoDo));

      if (offset > avioniPolasci.size())
        return Response.status(404, "Nema podataka").build();

      else if (offset - 1 + limit > avioniPolasci.size())
        avioniPolasci = avioniPolasci.subList(offset - 1, avioniPolasci.size());

      else
        avioniPolasci = avioniPolasci.subList(offset - 1, offset - 1 + limit);

    } catch (NwtisRestIznimka e) {
      return Response.status(404, e.getMessage()).build();
    }

    if (avioniPolasci == null)
      return Response.status(404, "Nema podataka").build();

    var gson = new Gson();
    var jsonAvioniPolasci = gson.toJson(avioniPolasci);
    var odgovor = Response.ok().entity(jsonAvioniPolasci).build();

    return odgovor;
  }

  /**
   * Sprema let u bazu podataka
   * 
   * @param let Podaci u letu u JSON formatu
   * @return Vraća poruku o uspjehu ili pogrešku sa statusnim kodom i opisom.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response dodajLet(String let) {
    LetAviona letAviona = null;
    Gson gson = new Gson();
    try {
      letAviona = gson.fromJson(let, LetAviona.class);
    } catch (JsonSyntaxException e) {
      return Response.status(404, e.getMessage()).build();
    }

    String query =
        "INSERT INTO LETOVI_POLASCI (ICAO24, FIRSTSEEN, ESTDEPARTUREAIRPORT, LASTSEEN, ESTARRIVALAIRPORT, CALLSIGN, ESTDEPARTUREAIRPORTHORIZDISTANCE, ESTDEPARTUREAIRPORTVERTDISTANCE, ESTARRIVALAIRPORTHORIZDISTANCE, ESTARRIVALAIRPORTVERTDISTANCE, DEPARTUREAIRPORTCANDIDATESCOUNT, ARRIVALAIRPORTCANDIDATESCOUNT, STORED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, letAviona.getIcao24());
      stmt.setInt(2, letAviona.getFirstSeen());
      stmt.setString(3, letAviona.getEstDepartureAirport());
      stmt.setInt(4, letAviona.getLastSeen());
      stmt.setString(5, letAviona.getEstArrivalAirport());
      stmt.setString(6, letAviona.getCallsign());
      stmt.setInt(7, letAviona.getEstDepartureAirportHorizDistance());
      stmt.setInt(8, letAviona.getEstDepartureAirportVertDistance());
      stmt.setInt(9, letAviona.getEstArrivalAirportHorizDistance());
      stmt.setInt(10, letAviona.getEstArrivalAirportVertDistance());
      stmt.setInt(11, letAviona.getDepartureAirportCandidatesCount());
      stmt.setInt(12, letAviona.getArrivalAirportCandidatesCount());

      stmt.executeUpdate();

    } catch (SQLException e) {
      return Response.status(404, e.getMessage()).build();
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();

      } catch (SQLException e) {
        return Response.status(404, e.getMessage()).build();
      }
    }

    return Response.ok().entity("Zapis uspješno dodan!").build();
  }

  /**
   * Vraća podatke o spremljenim letovima u bazi podataka
   * 
   * @return Vraća tražene podatke ili odgovor (pogrešku) sa statusnim kodom i opisom.
   */
  @GET
  @Path("/spremljeni")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSpremljeneLetove() {

    List<LetAviona> letoviAviona = new ArrayList<LetAviona>();

    String query = "SELECT * FROM LETOVI_POLASCI";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        LetAvionaID letAvionaID = new LetAvionaID();

        letAvionaID.setId(rs.getLong(1));
        letAvionaID.setIcao24(rs.getString(2));
        letAvionaID.setFirstSeen(rs.getInt(3));
        letAvionaID.setEstDepartureAirport(rs.getString(4));
        letAvionaID.setLastSeen(rs.getInt(5));
        letAvionaID.setEstArrivalAirport(rs.getString(6));
        letAvionaID.setCallsign(rs.getString(7));
        letAvionaID.setEstDepartureAirportHorizDistance(rs.getInt(8));
        letAvionaID.setEstDepartureAirportVertDistance(rs.getInt(9));
        letAvionaID.setEstArrivalAirportHorizDistance(rs.getInt(10));
        letAvionaID.setEstArrivalAirportVertDistance(rs.getInt(11));
        letAvionaID.setDepartureAirportCandidatesCount(rs.getInt(12));
        letAvionaID.setArrivalAirportCandidatesCount(rs.getInt(13));

        letoviAviona.add(letAvionaID);

      }
      rs.close();

    } catch (SQLException e) {
      return Response.status(404, e.getMessage()).build();
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();

      } catch (SQLException e) {
        return Response.status(404, e.getMessage()).build();
      }
    }

    if (letoviAviona.isEmpty())
      return Response.status(404, "Nema podataka!").build();

    var gson = new Gson();
    var jsonLetoviAviona = gson.toJson(letoviAviona);
    var odgovor = Response.ok().entity(jsonLetoviAviona).build();
    return odgovor;
  }

  /**
   * Briše let iz baze podataka
   * 
   * @param id ID zapisa o letu
   * @return Vraća poruku o uspjehu ili pogrešku sa statusnim kodom i opisom.
   */
  @DELETE
  @Path("{id}")
  public Response obrisiLet(@PathParam("id") int id) {
    String query = "DELETE FROM LETOVI_POLASCI WHERE ID = ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setInt(1, id);

      stmt.executeUpdate();

    } catch (SQLException e) {
      return Response.status(404, e.getMessage()).build();
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();

      } catch (SQLException e) {
        return Response.status(404, e.getMessage()).build();
      }
    }

    return Response.ok().entity("Zapis uspješno obrisan!").build();

  }

  /**
   * Konvertira datum u broj sekundi proteklih od 1.1.1970. (eng. Epoch Time). Dodaje jedan dan tako
   * da se dobije raspon od početka do kraja dana. Konverzija se obavlja prema lokalnom (sustavskom)
   * vremenu.
   * 
   * @param dan Datum u formatu dd.MM.gggg
   * @return Polje s rasponom od početka do kraja zadanog dana u sekundama proteklim od 1.1.1970.
   */
  private long[] konvertirajOdVremenaDoVremena(String dan) {
    long[] epochTime = new long[2];

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate date = LocalDate.parse(dan, dtf);
    LocalDate date2 = date.plusDays(1);
    ZonedDateTime dateTime = date.atStartOfDay(ZoneId.systemDefault());
    ZonedDateTime dateTime2 = date2.atStartOfDay(ZoneId.systemDefault());

    epochTime[0] = dateTime.toInstant().getEpochSecond();
    epochTime[1] = dateTime2.toInstant().getEpochSecond();

    return epochTime;

  }

  /**
   * Provjerava korektnost izraza korištenjem dozvoljenih izraza (eng. Regular expression)
   * 
   * @param izraz Izraz koji se provjerava
   * @param regex Regularni izraz s kojim se provjerava
   * @return Vraća true ako je u redu, inače false
   */
  private boolean provjeriIzraz(String izraz, String regex) {
    String s = izraz.trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }
}
