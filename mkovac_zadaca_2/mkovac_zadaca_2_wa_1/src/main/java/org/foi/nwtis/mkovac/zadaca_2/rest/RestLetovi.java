package org.foi.nwtis.mkovac.zadaca_2.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    if (dan == null || dan.isEmpty())
      return null;

    OSKlijent osKlijent = new OSKlijent(korisnik, lozinka);

    List<LetAviona> avioniPolasci = null;

    long[] odVremenaDoVremena = konvertirajOdVremenaDoVremena(dan);

    long odVremena = odVremenaDoVremena[0]; // 1677628800
    long doVremena = odVremenaDoVremena[1]; // 1677715200

    System.out.println("od vremena: " + odVremena);
    System.out.println("do vremena: " + doVremena);

    try {
      avioniPolasci = osKlijent.getDepartures(icao, odVremena, doVremena);
    } catch (NwtisRestIznimka e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(avioniPolasci);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

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
}
