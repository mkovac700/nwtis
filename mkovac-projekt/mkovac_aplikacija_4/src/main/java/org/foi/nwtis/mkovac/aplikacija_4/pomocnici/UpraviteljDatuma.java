package org.foi.nwtis.mkovac.aplikacija_4.pomocnici;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa za rad s datumima
 * 
 * @author Marijan Kovač
 *
 */
public class UpraviteljDatuma {

  private static final String regexDatum =
      "(0[1-9]|[1-2][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19[7-9][0-9]|20[0-2][0-9]|203[0-7])";

  /**
   * Provjerava ispravnost formata datuma
   * 
   * @param datum Zadani datum
   * @return true ako je u redu, inače false
   */
  public static boolean provjeriFormatDatuma(String datum) {
    return provjeriIzraz(datum, regexDatum);
  }

  /**
   * Konvertira datum u formatu dd.MM.yyyy u vrijeme u sekundama proteklo od 1.1.1970. (eng. epoch
   * time)
   * 
   * @param dan Datum u formatu dd.MM.yyyy
   * @return Vrijeme proteklo u sekundama od 1.1.1970.
   */
  public static long konvertirajDan(String dan) {
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
  public static long dodajDan(long epoch) {
    long epochSecond = epoch;
    long epochTime;

    Instant instant = Instant.ofEpochSecond(epochSecond);
    LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault()).plusDays(1);
    ZonedDateTime dateTime = date.atStartOfDay(ZoneId.systemDefault());

    epochTime = dateTime.toInstant().getEpochSecond();

    return epochTime;
  }

  /**
   * Provjerava ispravnost danog izraza koristeći regularne izraze
   * 
   * @param izraz Izraz koji se provjerava
   * @param regex Regularni izraz s kojim se provjerava
   * @return true ako je u redu, inače false
   */
  private static boolean provjeriIzraz(String izraz, String regex) {
    String s = izraz.trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }
}
