package org.foi.nwtis.mkovac.zadaca_2.rest;

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
  private javax.sql.DataSource ds;

  private ServletContext context = RestServisSlusac.getServletContext();
  private Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");

  private String korisnik;
  private String lozinka;

  public RestLetovi() {
    this.korisnik = konf.dajPostavku("OpenSkyNetwork.korisnik");
    this.lozinka = konf.dajPostavku("OpenSkyNetwork.lozinka");
  }

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

    String regex =
        "(0[1-9]|[1-2][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19[7-9][0-9]|20[0-2][0-9]|203[0-7])";
    if (dan == null || dan.isEmpty() || !provjeriIzraz(dan, regex))
      return Response.status(404, "Dan nije unesen ili nije u formatu dd.mm.gggg").build();

    int offset = 1, limit = 20;

    if ((odBroja != null && !odBroja.isEmpty()) && (broj != null && !broj.isEmpty())) {
      try {
        offset = Integer.parseInt(odBroja);
        limit = Integer.parseInt(broj);
        if (offset < 1 || limit < 1)
          return Response.status(404, "Neispravni parametri").build();
      } catch (NumberFormatException e) {
        return Response.status(404, "Neispravni parametri").build();
      }
    }

    OSKlijent osKlijent = new OSKlijent(korisnik, lozinka);

    List<LetAviona> avioniPolasci = null;

    long[] odVremenaDoVremena = konvertirajOdVremenaDoVremena(dan);

    long odVremena = odVremenaDoVremena[0]; // 1677628800
    long doVremena = odVremenaDoVremena[1]; // 1677715200

    System.out.println("od vremena: " + odVremena);
    System.out.println("do vremena: " + doVremena);

    try {

      // avioniPolasci = osKlijent.getDepartures(icao, odVremena, doVremena).subList(offset - 1,
      // offset - 1 + limit);

      avioniPolasci = osKlijent.getDepartures(icao, odVremena, doVremena);

      System.out.println("Broj stvarnih podataka: " + avioniPolasci.size());

      // ako offset prelazi velicinu liste, onda se odmah vraca 404 da nema podataka
      if (offset > avioniPolasci.size()) {
        System.out.println("test1");
        return Response.status(404, "Nema podataka").build();
      }

      // ako limit prelazi velicinu liste, onda vrati sublist do velicine liste
      else if (offset - 1 + limit > avioniPolasci.size()) {
        System.out.println("test2");
        avioniPolasci = avioniPolasci.subList(offset - 1, avioniPolasci.size());
      }

      // ako su offset i limit u granicama liste, onda vrati sublist u zadanim granicama
      else {
        System.out.println("test3");
        avioniPolasci = avioniPolasci.subList(offset - 1, offset - 1 + limit);
      }

    } catch (NwtisRestIznimka e) {
      return Response.status(404, e.getMessage()).build();
    }

    var gson = new Gson();
    var jsonAvioniPolasci = gson.toJson(avioniPolasci);
    var odgovor = Response.ok().entity(jsonAvioniPolasci).build();

    return odgovor;
  }

  @GET
  @Path("{icaoOd}/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajLetoveOdDoAerodromaNaDan(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo, @QueryParam("dan") String dan,
      @QueryParam("odBroja") String odBroja, @QueryParam("broj") String broj) {

    String regex =
        "(0[1-9]|[1-2][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19[7-9][0-9]|20[0-2][0-9]|203[0-7])";
    if (dan == null || dan.isEmpty() || !provjeriIzraz(dan, regex))
      return Response.status(404, "Dan nije unesen ili nije u formatu dd.mm.gggg").build();

    int offset = 1, limit = 20;

    if ((odBroja != null && !odBroja.isEmpty()) && (broj != null && !broj.isEmpty())) {
      try {
        offset = Integer.parseInt(odBroja);
        limit = Integer.parseInt(broj);
        if (offset < 1 || limit < 1)
          return Response.status(404, "Neispravni parametri").build();
      } catch (NumberFormatException e) {
        return Response.status(404, "Neispravni parametri").build();
      }
    }

    OSKlijent osKlijent = new OSKlijent(korisnik, lozinka);

    List<LetAviona> avioniPolasci = null;

    long[] odVremenaDoVremena = konvertirajOdVremenaDoVremena(dan);

    long odVremena = odVremenaDoVremena[0]; // 1677628800
    long doVremena = odVremenaDoVremena[1]; // 1677715200

    System.out.println("od vremena: " + odVremena);
    System.out.println("do vremena: " + doVremena);

    try {

      // avioniPolasci = osKlijent.getDepartures(icao, odVremena, doVremena).subList(offset - 1,
      // offset - 1 + limit);

      avioniPolasci = osKlijent.getDepartures(icaoOd, odVremena, doVremena);
      avioniPolasci.removeIf(
          ap -> ap.getEstArrivalAirport() == null || !ap.getEstArrivalAirport().equals(icaoDo));
      // avioniPolasci.removeIf(ap -> !ap.getEstArrivalAirport().equals(icaoDo));

      System.out.println("Broj stvarnih podataka: " + avioniPolasci.size());

      // ako offset prelazi velicinu liste, onda se odmah vraca 404 da nema podataka
      if (offset > avioniPolasci.size()) {
        System.out.println("test1");
        return Response.status(404, "Nema podataka").build();
      }

      // ako limit prelazi velicinu liste, onda vrati sublist do velicine liste
      else if (offset - 1 + limit > avioniPolasci.size()) {
        System.out.println("test2");
        avioniPolasci = avioniPolasci.subList(offset - 1, avioniPolasci.size());
      }

      // ako su offset i limit u granicama liste, onda vrati sublist u zadanim granicama
      else {
        System.out.println("test3");
        avioniPolasci = avioniPolasci.subList(offset - 1, offset - 1 + limit);
      }

    } catch (NwtisRestIznimka e) {
      return Response.status(404, e.getMessage()).build();
    }

    var gson = new Gson();
    var jsonAvioniPolasci = gson.toJson(avioniPolasci);
    var odgovor = Response.ok().entity(jsonAvioniPolasci).build();

    return odgovor;
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

    // LocalDateTime dateTime = date.atStartOfDay();
    // LocalDateTime dateTime2 = date2.atStartOfDay();
    //
    // epochTime[0] = dateTime.toInstant(ZoneOffset.UTC).getEpochSecond();
    // epochTime[1] = dateTime2.toInstant(ZoneOffset.UTC).getEpochSecond();

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
