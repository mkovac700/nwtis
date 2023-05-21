package org.foi.nwtis.mkovac.zadaca_3.dretve;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_3.jpa.Airports;
import org.foi.nwtis.mkovac.zadaca_3.jpa.LetoviPolasci;
import org.foi.nwtis.mkovac.zadaca_3.zrna.AirportFacade;
import org.foi.nwtis.mkovac.zadaca_3.zrna.JmsPosiljatelj;
import org.foi.nwtis.mkovac.zadaca_3.zrna.LetoviPolasciFacade;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;
import jakarta.annotation.Resource;
import jakarta.persistence.NoResultException;

public class SakupljacLetovaAviona extends Thread {
  private Konfiguracija konfig;

  private String[] aerodromiSakupljanje;
  private int ciklusTrajanje;
  private String preuzimanjeOd;
  private String preuzimanjeDo;
  private String osKorisnik;
  private String osLozinka;

  private final String regexDatum =
      "(0[1-9]|[1-2][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19[7-9][0-9]|20[0-2][0-9]|203[0-7])";

  private boolean kraj = false;

  // @Inject
  // LetoviPolasciFacade letoviPolasciFacade;
  //
  // @Inject
  // AirportFacade airportFacade;

  LetoviPolasciFacade letoviPolasciFacade;
  AirportFacade airportFacade;
  JmsPosiljatelj jmsPosiljatelj;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  public SakupljacLetovaAviona(Konfiguracija konfig, LetoviPolasciFacade letoviPolasciFacade,
      AirportFacade airportFacade, JmsPosiljatelj jmsPosiljatelj) {
    this.konfig = konfig;
    this.aerodromiSakupljanje = this.konfig.dajPostavku("aerodromi.sakupljanje").trim().split(" ");
    this.ciklusTrajanje = Integer.parseInt(this.konfig.dajPostavku("ciklus.trajanje"));
    this.preuzimanjeOd = this.konfig.dajPostavku("preuzimanje.od");
    this.preuzimanjeDo = this.konfig.dajPostavku("preuzimanje.do");

    this.letoviPolasciFacade = letoviPolasciFacade;
    this.airportFacade = airportFacade;
    this.jmsPosiljatelj = jmsPosiljatelj;
  }

  @Override
  public synchronized void start() {
    super.start();
  }

  @Override
  public void interrupt() {
    this.kraj = true;
    Logger.getGlobal().log(Level.INFO, "Dretva zaustavljena!");
    super.interrupt();
  }

  @Override
  public void run() {
    if (!provjeriFormatDatuma(this.preuzimanjeOd) || !provjeriFormatDatuma(this.preuzimanjeDo)) {
      Logger.getGlobal().log(Level.SEVERE, "Dan nije u formatu dd.mm.gggg");
      return;
    }

    long pocetniDan = konvertirajDan(this.preuzimanjeOd); // this.preuzimanjeOd
    long zavrsniDan = konvertirajDan(this.preuzimanjeDo); // this.preuzimanjeDo
    long trenutniDan; // za koji se preuzima
    long zadnjiDan; // iz baze podataka

    if (pocetniDan >= zavrsniDan) {
      Logger.getGlobal().log(Level.SEVERE,
          "Početni dan ne može biti veći ili jednak završnom danu!");
      return;
    }

    try {
      zadnjiDan = letoviPolasciFacade.findLast().getFirstSeen();
    } catch (NoResultException e) {
      Logger.getGlobal().log(Level.INFO, e.getMessage());
      zadnjiDan = 0;
    }

    if (zadnjiDan > pocetniDan)
      trenutniDan = dodajDan(zadnjiDan);
    else if (zadnjiDan < pocetniDan)
      trenutniDan = pocetniDan;
    else
      trenutniDan = pocetniDan;

    OSKlijent osKlijent = new OSKlijent(osKorisnik, osLozinka);

    Logger.getGlobal().log(Level.INFO, "Započeto preuzimanje letova...");

    while (!this.kraj && trenutniDan < zavrsniDan) {
      // početak ciklusa preuzimanja
      long pocetnoVrijeme = System.currentTimeMillis();
      long ukupno = 0;

      // za svaki aerodrom iz polja aerodroma
      // LDZA LOWW EDDF EDDM
      for (String aerodrom : aerodromiSakupljanje) {
        // preuzmi letove za tekuci aerodrom za trenutni dan
        Airports airport = airportFacade.find(aerodrom);
        List<LetAviona> letoviAviona = null;
        try {
          letoviAviona = osKlijent.getDepartures(aerodrom, trenutniDan, dodajDan(trenutniDan));
          letoviAviona.removeIf(la -> la.getEstArrivalAirport() == null);
        } catch (NwtisRestIznimka e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        // iteriraj po preuzetim letovima za aerodrom, pretvori u JPA tip i spremi u bazu preko
        // CriteriaAPI
        if (letoviAviona != null) {
          for (LetAviona la : letoviAviona) {
            LetoviPolasci letoviPolasci = new LetoviPolasci();

            letoviPolasci.setArrivalAirportCandidatesCount(la.getArrivalAirportCandidatesCount());
            letoviPolasci.setCallsign(la.getCallsign());
            letoviPolasci
                .setDepartureAirportCandidatesCount(la.getDepartureAirportCandidatesCount());
            letoviPolasci.setEstArrivalAirport(la.getEstArrivalAirport());
            letoviPolasci.setEstArrivalAirportHorizDistance(la.getEstArrivalAirportHorizDistance());
            letoviPolasci.setEstArrivalAirportVertDistance(la.getEstArrivalAirportVertDistance());
            letoviPolasci
                .setEstDepartureAirportHorizDistance(la.getEstDepartureAirportHorizDistance());
            letoviPolasci
                .setEstDepartureAirportVertDistance(la.getEstDepartureAirportVertDistance());
            letoviPolasci.setFirstSeen(la.getFirstSeen());
            letoviPolasci.setIcao24(la.getIcao24());
            letoviPolasci.setLastSeen(la.getLastSeen());
            letoviPolasci.setStored(Timestamp.from(Instant.now()));
            letoviPolasci.setAirport(airport);

            letoviPolasciFacade.create(letoviPolasci);

            ukupno++;
          }
        }
      }

      // šalji JMS poruku
      String poruka = "Na dan: " + konvertirajDan(trenutniDan) + " preuzeto ukupno " + ukupno
          + " letova aviona";
      if (jmsPosiljatelj.saljiPoruku(poruka)) {
        System.out.println("Poruka je poslana!");
      } else {
        System.out.println("Greška kod slanja");
      }

      long zavrsnoVrijeme = System.currentTimeMillis();
      long radnoVrijeme = zavrsnoVrijeme - pocetnoVrijeme;
      long spavanje = ciklusTrajanje * 1000 - radnoVrijeme;

      System.out.println("Radno vrijeme: " + radnoVrijeme / (float) 1000);
      System.out.println("Spavanje: " + spavanje / (float) 1000);

      if (spavanje > 0 && !this.kraj) {
        try {
          Thread.sleep(spavanje);
        } catch (InterruptedException e) {
          Logger.getGlobal().log(Level.WARNING, e.getMessage());
        }
      }

      // dan++
      trenutniDan = dodajDan(trenutniDan);

    }
    Logger.getGlobal().log(Level.INFO, "Završeno preuzimanje letova...");
  }

  private String konvertirajDan(long epoch) {
    Date date = new Date(epoch * 1000);
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    return sdf.format(date);
  }

  private long konvertirajDan(String dan) {
    long epochTime;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate date = LocalDate.parse(dan, dtf);
    ZonedDateTime dateTime = date.atStartOfDay(ZoneId.systemDefault());

    epochTime = dateTime.toInstant().getEpochSecond();

    return epochTime;
  }

  private long dodajDan(long epoch) {
    long epochSecond = epoch;
    long epochTime;

    Instant instant = Instant.ofEpochSecond(epochSecond);
    LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault()).plusDays(1);
    ZonedDateTime dateTime = date.atStartOfDay(ZoneId.systemDefault());

    epochTime = dateTime.toInstant().getEpochSecond();

    return epochTime;
  }

  private boolean provjeriFormatDatuma(String datum) {
    return provjeriIzraz(datum, regexDatum);
  }

  private boolean provjeriIzraz(String izraz, String regex) {
    String s = izraz.trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }

}
