package org.foi.nwtis.mkovac.aplikacija_4.pomocnici;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUpravitelj {

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
