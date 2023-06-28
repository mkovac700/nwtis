package org.foi.nwtis.mkovac.aplikacija_3.dretve;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_3.jpa.AerodromiLetovi;
import org.foi.nwtis.mkovac.aplikacija_3.jpa.Airports;
import org.foi.nwtis.mkovac.aplikacija_3.jpa.LetoviPolasci;
import org.foi.nwtis.mkovac.aplikacija_3.zrna.AerodromiLetoviFacade;
import org.foi.nwtis.mkovac.aplikacija_3.zrna.AirportFacade;
import org.foi.nwtis.mkovac.aplikacija_3.zrna.JmsPosiljatelj;
import org.foi.nwtis.mkovac.aplikacija_3.zrna.LetoviPolasciFacade;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.klijenti.OSKlijentBP;
import org.foi.nwtis.rest.podaci.LetAviona;
import jakarta.annotation.Resource;
import jakarta.persistence.NoResultException;

/**
 * Klasa dretve za preuzimanje letova aviona
 * 
 * @author Marijan Kovač
 *
 */
public class SakupljacLetovaAviona extends Thread {
  private Konfiguracija konfig;

  private List<String> aerodromiSakupljanje;
  private int ciklusTrajanje;
  private String preuzimanjeOd;
  private String preuzimanjeDo;
  private String osKorisnik;
  private String osLozinka;
  private String bpKorisnik;
  private String bpLozinka;
  private String klijent;

  private final String regexDatum =
      "(0[1-9]|[1-2][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19[7-9][0-9]|20[0-2][0-9]|203[0-7])";

  private boolean kraj = false;

  LetoviPolasciFacade letoviPolasciFacade;
  AirportFacade airportFacade;
  AerodromiLetoviFacade aerodromiLetoviFacade;
  JmsPosiljatelj jmsPosiljatelj;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  public SakupljacLetovaAviona(Konfiguracija konfig, LetoviPolasciFacade letoviPolasciFacade,
      AirportFacade airportFacade, AerodromiLetoviFacade aerodromiLetoviFacade,
      JmsPosiljatelj jmsPosiljatelj) {

    this.konfig = konfig;

    this.aerodromiSakupljanje = new ArrayList<>();

    this.ciklusTrajanje = Integer.parseInt(this.konfig.dajPostavku("ciklus.trajanje"));
    this.preuzimanjeOd = this.konfig.dajPostavku("preuzimanje.od");
    this.preuzimanjeDo = this.konfig.dajPostavku("preuzimanje.do");
    this.osKorisnik = this.konfig.dajPostavku("OpenSkyNetwork.korisnik");
    this.osLozinka = this.konfig.dajPostavku("OpenSkyNetwork.lozinka");
    this.bpKorisnik = this.konfig.dajPostavku("OSKlijentBP.korisnik");
    this.bpLozinka = this.konfig.dajPostavku("OSKlijentBP.lozinka");
    this.klijent = this.konfig.dajPostavku("preuzimanje.klijent");

    this.letoviPolasciFacade = letoviPolasciFacade;
    this.airportFacade = airportFacade;
    this.aerodromiLetoviFacade = aerodromiLetoviFacade;
    this.jmsPosiljatelj = jmsPosiljatelj;
  }

  @Override
  public synchronized void start() {
    super.start();
  }

  /**
   * Signalizira kraj rada podizanjem zastavice te zaustavlja dretvu
   */
  @Override
  public void interrupt() {
    this.kraj = true;
    Logger.getGlobal().log(Level.INFO, "Dretva zaustavljena!");
    super.interrupt();
  }

  /**
   * Obavlja preuzimanje letova od zadanog početnog datuma do zadanog konačnog datuma ili dok nije
   * podignuta zastavica za kraj rada. Preuzimanje se obavlja unutar ciklusa zadanog trajanja. Ako
   * je preuzimanje za određeni dan završilo prije isteka ciklusa, dretva će mirovati preostalo
   * vrijeme trajanja ciklusa. Ako je preuzimanje trajalo duže od zadanog ciklusa, tada se spavanje
   * preskače i nastavlja rad s novim ciklusom.
   */
  @Override
  public void run() {
    if (!provjeriFormatDatuma(this.preuzimanjeOd) || !provjeriFormatDatuma(this.preuzimanjeDo)) {
      Logger.getGlobal().log(Level.SEVERE, "Dan nije u formatu dd.mm.gggg");
      return;
    }

    long pocetniDan = konvertirajDan(this.preuzimanjeOd);
    long zavrsniDan = konvertirajDan(this.preuzimanjeDo);
    long trenutniDan;
    long zadnjiDan;

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
    } catch (Exception e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      zadnjiDan = 0;
    }

    if (zadnjiDan > pocetniDan)
      trenutniDan = dodajDan(zadnjiDan);
    else if (zadnjiDan < pocetniDan)
      trenutniDan = pocetniDan;
    else
      trenutniDan = pocetniDan;

    OSKlijent osKlijent = null;
    if (klijent.equals("OSKlijent"))
      osKlijent = new OSKlijent(osKorisnik, osLozinka);

    OSKlijentBP osKlijentBP = null;
    if (klijent.equals("OSKlijentBP"))
      osKlijentBP = new OSKlijentBP(bpKorisnik, bpLozinka);

    if (osKlijent == null && osKlijentBP == null) {
      Logger.getGlobal().log(Level.SEVERE, "Nepoznat klijent preuzimanja podataka o letovima!");
      return;
    }

    Logger.getGlobal().log(Level.INFO, "Započeto preuzimanje letova...");

    while (!this.kraj && trenutniDan < zavrsniDan) {
      Logger.getGlobal().log(Level.INFO,
          "Preuzimanje letova za dan " + konvertirajDan(trenutniDan));

      aerodromiSakupljanje.clear();

      List<AerodromiLetovi> aerodromiLetovi = aerodromiLetoviFacade.findAll();
      if (aerodromiLetovi != null && !aerodromiLetovi.isEmpty()) {
        aerodromiLetovi.removeIf(al -> al.getPreuzimanje() == false);
        aerodromiLetovi.forEach(al -> aerodromiSakupljanje.add(al.getAirport().getIcao()));
      }

      long pocetnoVrijeme = System.currentTimeMillis();
      long ukupno = 0;

      for (String aerodrom : aerodromiSakupljanje) {
        Airports airport = airportFacade.find(aerodrom);
        List<LetAviona> letoviAviona = null;
        try {
          if (klijent.equals("OSKlijent"))
            letoviAviona = osKlijent.getDepartures(aerodrom, trenutniDan, dodajDan(trenutniDan));

          if (klijent.equals("OSKlijentBP"))
            letoviAviona = osKlijentBP.getDepartures(aerodrom, trenutniDan, dodajDan(trenutniDan));

          letoviAviona.removeIf(la -> la.getEstArrivalAirport() == null);

        } catch (NwtisRestIznimka e) {
          Logger.getGlobal().log(Level.WARNING, e.getMessage());
        }
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

            try {
              letoviPolasciFacade.create(letoviPolasci);
            } catch (Exception e) {
              Logger.getGlobal().log(Level.SEVERE, e.getMessage());
            }

            ukupno++;
          }
        }
      }

      String poruka = "Na dan: " + konvertirajDan(trenutniDan) + " preuzeto ukupno " + ukupno
          + " letova aviona";
      if (jmsPosiljatelj.saljiPoruku(poruka)) {
        Logger.getGlobal().log(Level.INFO, "Poruka je poslana: " + poruka);
      } else {
        Logger.getGlobal().log(Level.SEVERE, "Greška kod slanja poruke!");
      }

      long zavrsnoVrijeme = System.currentTimeMillis();
      long radnoVrijeme = zavrsnoVrijeme - pocetnoVrijeme;
      long spavanje = ciklusTrajanje * 1000 - radnoVrijeme;

      Logger.getGlobal().log(Level.INFO, "Radno vrijeme: " + radnoVrijeme / (float) 1000);
      Logger.getGlobal().log(Level.INFO, "Spavanje: " + spavanje / (float) 1000);

      if (spavanje > 0 && !this.kraj) {
        try {
          Thread.sleep(spavanje);
        } catch (InterruptedException e) {
          Logger.getGlobal().log(Level.WARNING, e.getMessage());
        }
      }

      trenutniDan = dodajDan(trenutniDan);

    }
    Logger.getGlobal().log(Level.INFO, "Završeno preuzimanje letova...");
  }

  /**
   * Konvertira vrijeme u sekundama proteklo od 1.1.1970. (eng. epoch time) u format datuma
   * dd.MM.yyyy
   * 
   * @param epoch Vrijeme u sekundama proteklo od 1.1.1970.
   * @return Datum u formatu dd.MM.yyyy
   */
  private String konvertirajDan(long epoch) {
    Date date = new Date(epoch * 1000);
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    return sdf.format(date);
  }

  /**
   * Konvertira datum u formatu dd.MM.yyyy u vrijeme u sekundama proteklo od 1.1.1970. (eng. epoch
   * time)
   * 
   * @param dan Datum u formatu dd.MM.yyyy
   * @return Vrijeme proteklo u sekundama od 1.1.1970.
   */
  private long konvertirajDan(String dan) {
    long epochTime;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate date = LocalDate.parse(dan, dtf);
    ZonedDateTime dateTime = date.atStartOfDay(ZoneId.systemDefault());

    epochTime = dateTime.toInstant().getEpochSecond();

    return epochTime;
  }

  /**
   * Dodaje jedan dan na vrijeme proteklo od 1.1.1970.
   * 
   * @param epoch Vrijeme u sekundama proteklo od 1.1.1970.
   * @return Vrijeme u sekundama proteklo od 1.1.1970. uvećano za jedan dan (početak dana, neovisno
   *         o dobu dana)
   */
  private long dodajDan(long epoch) {
    long epochSecond = epoch;
    long epochTime;

    Instant instant = Instant.ofEpochSecond(epochSecond);
    LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault()).plusDays(1);
    ZonedDateTime dateTime = date.atStartOfDay(ZoneId.systemDefault());

    epochTime = dateTime.toInstant().getEpochSecond();

    return epochTime;
  }

  /**
   * Provjerava ispravnost formata datuma
   * 
   * @param datum Zadani datum
   * @return true ako je u redu, inače false
   */
  private boolean provjeriFormatDatuma(String datum) {
    return provjeriIzraz(datum, regexDatum);
  }

  /**
   * Provjerava ispravnost danog izraza koristeći regularne izraze
   * 
   * @param izraz Izraz koji se provjerava
   * @param regex Regularni izraz s kojim se provjerava
   * @return true ako je u redu, inače false
   */
  private boolean provjeriIzraz(String izraz, String regex) {
    String s = izraz.trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }

}
