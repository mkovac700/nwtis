package org.foi.nwtis.mkovac.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa GlavniKlijent služi kao korisnički program putem kojeg se mogu slati određene naredbe.
 * Obradu naredbi obavlja GlavniPosluzitelj.
 * 
 * @author Marijan Kovač
 *
 */
public class GlavniKlijent {

  private static final String regex =
      "(-k) ([a-zA-Z0-9_-]{3,10}) (-l) ([a-zA-Z0-9_\\-#!]{3,10}) (-a) ((?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-zA-Z_\\-.]+) (-v) ([8-9][0-9]{3}) (-t) ([0-9]+) ((--meteo ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+))|(--makstemp ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+)|--maksvlaga ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+)|--makstlak ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+))|(--alarm ('[a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_\\-\\s]+'))|(--udaljenost ('[a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_\\-\\s]+') ('[a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_\\-\\s]+'))|(--udaljenost spremi)|(--kraj))$";

  private static String zahtjev = null;

  /**
   * Glavna funkcija koja služi za pokretanje programa GlavniKlijent
   * 
   * @param args Naredba za izvršavanje određenog zahtjeva
   */
  public static void main(String[] args) {
    var gk = new GlavniKlijent();
    if (!gk.provjeriArgumente(args)) {
      Logger.getGlobal().log(Level.SEVERE, "Nisu ispravni ulazni argumenti!");
      return;
    }

    String izraz = String.join(" ", args);

    String[] podaci = razdvojiIzraz(izraz, regex);

    String posluzitelj = podaci[6];
    int mreznaVrata = Integer.parseInt(podaci[8]);

    zahtjev = gk.odrediVrstuRada(args);

    if (zahtjev == null)
      return;

    gk.spojiSeNaPosluzitelj(posluzitelj, mreznaVrata);


  }

  /**
   * Određuje vrstu rada na temelju unesene naredbe na način da formira zahtjev za GlavniPosluzitelj
   * u točno određenom formatu.
   * 
   * @param args Ulazni argument
   * @return Vraća formirani zahtjev za GlavniPosluzitelj
   */
  private String odrediVrstuRada(String[] args) {
    String izraz = String.join(" ", args);

    String regex1 =
        "(-k) ([a-zA-Z0-9_-]{3,10}) (-l) ([a-zA-Z0-9_\\-#!]{3,10}) (-a) ((?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-zA-Z_\\-.]+) (-v) ([8-9][0-9]{3}) (-t) ([0-9]+) (--kraj)$";

    String regex2 =
        "(-k) ([a-zA-Z0-9_-]{3,10}) (-l) ([a-zA-Z0-9_\\-#!]{3,10}) (-a) ((?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-zA-Z_\\-.]+) (-v) ([8-9][0-9]{3}) (-t) ([0-9]+) (--meteo ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+))$";

    String regex3 =
        "(-k) ([a-zA-Z0-9_-]{3,10}) (-l) ([a-zA-Z0-9_\\-#!]{3,10}) (-a) ((?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-zA-Z_\\-.]+) (-v) ([8-9][0-9]{3}) (-t) ([0-9]+) (--makstemp ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+))$";

    String regex4 =
        "(-k) ([a-zA-Z0-9_-]{3,10}) (-l) ([a-zA-Z0-9_\\-#!]{3,10}) (-a) ((?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-zA-Z_\\-.]+) (-v) ([8-9][0-9]{3}) (-t) ([0-9]+) (--maksvlaga ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+))$";

    String regex5 =
        "(-k) ([a-zA-Z0-9_-]{3,10}) (-l) ([a-zA-Z0-9_\\-#!]{3,10}) (-a) ((?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-zA-Z_\\-.]+) (-v) ([8-9][0-9]{3}) (-t) ([0-9]+) (--makstlak ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+))$";

    String regex6 =
        "(-k) ([a-zA-Z0-9_-]{3,10}) (-l) ([a-zA-Z0-9_\\-#!]{3,10}) (-a) ((?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-zA-Z_\\-.]+) (-v) ([8-9][0-9]{3}) (-t) ([0-9]+) (--alarm ('[a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_\\-\\s]+'))$";

    String regex7 =
        "(-k) ([a-zA-Z0-9_-]{3,10}) (-l) ([a-zA-Z0-9_\\-#!]{3,10}) (-a) ((?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-zA-Z_\\-.]+) (-v) ([8-9][0-9]{3}) (-t) ([0-9]+) (--udaljenost ('[a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_\\-\\s]+') ('[a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_\\-\\s]+'))$";

    String regex8 =
        "(-k) ([a-zA-Z0-9_-]{3,10}) (-l) ([a-zA-Z0-9_\\-#!]{3,10}) (-a) ((?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-zA-Z_\\-.]+) (-v) ([8-9][0-9]{3}) (-t) ([0-9]+) (--udaljenost spremi)$";

    if (provjeriIzraz(izraz, regex1)) {
      String[] zahtjev = razdvojiIzraz(izraz, regex1);

      return "KORISNIK " + zahtjev[2] + " LOZINKA " + zahtjev[4] + " KRAJ";
    }

    if (provjeriIzraz(izraz, regex2)) {
      String[] zahtjev = razdvojiIzraz(izraz, regex2);

      return "KORISNIK " + zahtjev[2] + " LOZINKA " + zahtjev[4] + " METEO " + zahtjev[12];
    }

    if (provjeriIzraz(izraz, regex3)) {
      String[] zahtjev = razdvojiIzraz(izraz, regex3);

      return "KORISNIK " + zahtjev[2] + " LOZINKA " + zahtjev[4] + " MAKS TEMP " + zahtjev[12];
    }

    if (provjeriIzraz(izraz, regex4)) {
      String[] zahtjev = razdvojiIzraz(izraz, regex4);

      return "KORISNIK " + zahtjev[2] + " LOZINKA " + zahtjev[4] + " MAKS VLAGA " + zahtjev[12];
    }

    if (provjeriIzraz(izraz, regex5)) {
      String[] zahtjev = razdvojiIzraz(izraz, regex5);

      return "KORISNIK " + zahtjev[2] + " LOZINKA " + zahtjev[4] + " MAKS TLAK " + zahtjev[12];
    }

    if (provjeriIzraz(izraz, regex6)) {
      String[] zahtjev = razdvojiIzraz(izraz, regex6);

      return "KORISNIK " + zahtjev[2] + " LOZINKA " + zahtjev[4] + " ALARM " + zahtjev[12];
    }

    if (provjeriIzraz(izraz, regex7)) {
      String[] zahtjev = razdvojiIzraz(izraz, regex7);

      return "KORISNIK " + zahtjev[2] + " LOZINKA " + zahtjev[4] + " UDALJENOST " + zahtjev[12]
          + " " + zahtjev[13];
    }

    if (provjeriIzraz(izraz, regex8)) {
      String[] zahtjev = razdvojiIzraz(izraz, regex8);

      return "KORISNIK " + zahtjev[2] + " LOZINKA " + zahtjev[4] + " UDALJENOST SPREMI";
    }

    return null;
  }

  /**
   * Provjerava je li ulazni argument ispravan
   * 
   * @param args Ulazni argument
   * @return Vraća true ako je u redu, inače false
   */
  private boolean provjeriArgumente(String[] args) {

    String izraz = String.join(" ", args);

    return provjeriIzraz(izraz, regex);
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

  /**
   * Razdvaja izraz po grupama korištenjem dozvoljenih izraza (eng. Regular expression)
   * 
   * @param izraz Izraz koji se provjerava
   * @param regex Regularni izraz s kojim se provjerava
   * @return Vraća polje s razdvojenim dijelovima izraza
   */
  private static String[] razdvojiIzraz(String izraz, String regex) {
    List<String> rezultat = new ArrayList<>();

    String s = izraz.trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    if (status) {
      int poc = 0;
      int kraj = matcher.groupCount();
      for (int i = poc; i <= kraj; i++) {
        rezultat.add(matcher.group(i));
      }
    } else {
      rezultat = null;
    }

    return rezultat.toArray(new String[rezultat.size()]);
  }

  /**
   * Spaja se na GlavniPosluzitelj, šalje korisnikov zahtjev i čeka na odgovor.
   * 
   * @param adresa GlavniPosluzitelj adresa
   * @param mreznaVrata GlavniPosluzitelj vrata
   */
  private void spojiSeNaPosluzitelj(String adresa, int mreznaVrata) {

    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);

      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();


      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;

        poruka.append(red);
      }

      Logger.getGlobal().log(Level.INFO, "Odgovor: " + poruka);
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();

    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }

  }

}
