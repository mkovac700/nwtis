package org.foi.nwtis.mkovac.zadaca_1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Za testiranje opcija
 */
public class TestOpcija {

  /**
   * @param args argumenti komande linije
   */

  public static void main(String[] args) {

    // -k korisnik -l lozinka -a (ipadresa | adresa) -v mreznaVrata -t cekanje ((--meteo idUredaj) |
    // --kraj)
    // -k pero -l 123456 -s localhost -p 8000 -t 0 --meteo FOI1-BME280 //napomena: s i p se ne
    // koriste i to pada, po novom je a i v kao u prvoj komandi
    // -k pero -l 123456 -s localhost -p 8000 -t 0 --kraj

    // koristi indeksirane grupe (...) poklapanja
    String sintaksa1 = "-k ([0-9a-zA-Z_]+) -l ([0-9a-zA-Z_-]+) "
        + "-a ([0-9a-zA-Z_-[.]]+) -v ([0-9]+) -t ([0-9]+) ((--meteo ([0-9a-zA-Z_-[.]]+))|(--kraj))$";

    // koristi imenovane grupe (?<ime>...) poklpanja
    String sintaksa2 = "-k (?<korisnik>[0-9a-zA-Z_]+) -l (?<lozinka>[0-9a-zA-Z_-]+) "
        + "-a (?<adresa>[0-9a-zA-Z_-[.]]+) -v (?<mreznaVrata>[0-9]+) -t (?<cekanje>[0-9]+) ((--meteo (?<meteo>[0-9a-zA-Z_-[.]]+))|(?<kraj>--kraj))$";

    String sintaksa3 =
        "UDALJENOST ((\\d*\\.)?\\d+) ((\\d*\\.)?\\d+) ((\\d*\\.)?\\d+) ((\\d*\\.)?\\d+)$";

    String sintaksa4 = "UDALJENOST SPREMI$";

    String sintaksa5 = "KORISNIK ([a-zA-Z0-9_-]{3,10}) LOZINKA ([a-zA-Z0-9_\\-#!]{3,10}) KRAJ$";

    String sintaksa6 =
        "KORISNIK ([a-zA-Z0-9_-]{3,10}) LOZINKA ([a-zA-Z0-9_\\-#!]{3,10}) SENZOR ([a-zA-Z0-9_-]+) (\\d{1,2}:\\d{1,2}:\\d{2}) ([0-9]{1,3}(\\.\\d)?) ([0-9]{1,3}(\\.\\d)?) ([0-9]{1,3}(\\.\\d)?)$"; // (\\d{1,2}:\\d{1,2}:\\d{2})
    // (\\d{3}[0-9](\\.\\d)?)
    // (\\d{3}[0-9](\\.\\d)?)
    // (\\d{3}[0-9](\\.\\d)?)

    String sintaksa7 =
        "KORISNIK ([a-zA-Z0-9_-]{3,10}) LOZINKA ([a-zA-Z0-9_\\-#!]{3,10}) SENZOR ([a-zA-Z0-9_-]+) (\\d{1,2}:\\d{1,2}:\\d{2}) ([0-9]{1,3}(\\.\\d)?)( ([0-9]{1,3}(\\.\\d)?)?)?( ([0-9]{1,3}(\\.\\d)?)?)?$";

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      sb.append(args[i]).append(" ");
    }
    String s = sb.toString().trim();

    Pattern pattern1 = Pattern.compile(sintaksa1);
    Matcher m1 = pattern1.matcher(s);
    boolean status1 = m1.matches();
    if (status1) {
      int poc = 0;
      int kraj = m1.groupCount();
      for (int i = poc; i <= kraj; i++) {
        System.out.println(i + ". " + m1.group(i));
      }
    } else {
      System.out.println("Ne odgovara 1!");
    }

    Pattern pattern2 = Pattern.compile(sintaksa2);
    Matcher m2 = pattern2.matcher(s);
    boolean status2 = m2.matches();
    if (status2) {
      System.out.println("Korisnik: " + m2.group("korisnik"));
      System.out.println("Lozinka: " + m2.group("lozinka"));
      System.out.println("Adresa poslužitelja: " + m2.group("adresa"));
      System.out.println("Mrežna vrata: " + m2.group("mreznaVrata"));
      System.out.println("Meteo: " + m2.group("meteo"));
      System.out.println("Kraj: " + m2.group("kraj"));
    } else {
      System.out.println("Ne odgovara 2!");
    }

    Pattern pattern3 = Pattern.compile(sintaksa3);
    Matcher m3 = pattern3.matcher(s);
    boolean status3 = m3.matches();
    if (status3) {
      System.out.println("Odgovara 3!");
    } else {
      System.out.println("Ne odgovara 3!");
    }

    Pattern pattern4 = Pattern.compile(sintaksa4);
    Matcher m4 = pattern4.matcher(s);
    boolean status4 = m4.matches();
    if (status4) {
      System.out.println("Odgovara 4!");
    } else {
      System.out.println("Ne odgovara 4!");
    }

    Pattern pattern5 = Pattern.compile(sintaksa5);
    Matcher m5 = pattern5.matcher(s);
    boolean status5 = m5.matches();
    if (status5) {
      System.out.println("Odgovara 5!");
    } else {
      System.out.println("Ne odgovara 5!");
    }

    Pattern pattern6 = Pattern.compile(sintaksa6);
    Matcher m6 = pattern6.matcher(s);
    boolean status6 = m6.matches();
    if (status6) {
      System.out.println("Odgovara 6!");
    } else {
      System.out.println("Ne odgovara 6!");
    }

    Pattern pattern7 = Pattern.compile(sintaksa7);
    Matcher m7 = pattern7.matcher(s);
    boolean status7 = m7.matches();
    if (status7) {
      System.out.println("Odgovara 7!");
    } else {
      System.out.println("Ne odgovara 7!");
    }
  }
}
