package org.foi.nwtis.mkovac.aplikacija_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa koja obavlja ulogu dretve za GlavniPosluzitelj.
 * 
 * @author Marijan Kovač
 *
 */
public class MrezniRadnik implements Runnable {
  private ServerSocket posluzitelj;
  private Socket uticnica;

  private AtomicBoolean status;
  private AtomicBoolean ispis;
  private AtomicInteger brojacUdaljenosti;

  private final double r = 6371;

  public MrezniRadnik(ServerSocket posluzitelj, Socket uticnica, AtomicBoolean status,
      AtomicBoolean ispis, AtomicInteger brojacUdaljenosti) {

    this.posluzitelj = posluzitelj;
    this.uticnica = uticnica;

    this.status = status;
    this.ispis = ispis;
    this.brojacUdaljenosti = brojacUdaljenosti;
  }

  /**
   * Dio koda koji obrađuje dretva za GlavniPosluzitelj. Čita dolazni zahtjev te ga obrađuje i šalje
   * odgovor, tj. rezultat obrade ili pogrešku.
   */
  @Override
  public void run() {
    try {

      var citac = new BufferedReader(
          new InputStreamReader(this.uticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.uticnica.getOutputStream(), Charset.forName("UTF-8")));

      var poruka = new StringBuilder();

      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        if (this.ispis.get())
          System.out.println(red);
        poruka.append(red);
      }

      this.uticnica.shutdownInput();

      String odgovor = this.obradiZahtjev(poruka.toString());

      if (this.ispis.get())
        System.out.println("Odgovor: " + odgovor);

      pisac.write(odgovor);
      pisac.flush();

      this.uticnica.shutdownOutput();
      this.uticnica.close();


    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  /**
   * Obrađuje dolazni zahtjev na temelju pripremljenih dozvoljenih zahtjeva. Nakon obrade vraća
   * poruku za klijenta.
   * 
   * @param zahtjev Dolazni formatirani zahtjev
   * @return Vraća traženi podatak klijentu ako je uspješno ili grešku.
   */
  private String obradiZahtjev(String zahtjev) {

    String regex1 = "(STATUS)$";
    String regex2 = "(KRAJ)$";
    String regex3 = "(INIT)$";
    String regex4 = "(PAUZA)$";
    String regex5 = "(INFO) (DA|NE)$";
    String regex6 =
        "(UDALJENOST) (-?(\\d*\\.)?\\d+) (-?(\\d*\\.)?\\d+) (-?(\\d*\\.)?\\d+) (-?(\\d*\\.)?\\d+)$";

    if (provjeriIzraz(zahtjev, regex1)) { // STATUS
      if (this.status.get())
        return "OK 1";
      else
        return "OK 0";
    }

    if (provjeriIzraz(zahtjev, regex2)) { // KRAJ
      try {
        this.posluzitelj.close();
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());;
      }
      return "OK";
    }

    if (provjeriIzraz(zahtjev, regex3)) { // INIT
      if (!this.status.get()) { // ako je pauziran
        this.status.set(true);
        this.brojacUdaljenosti.set(0);
        return "OK";
      } else // ako je vec aktivan
        return "ERROR 02 Poslužitelj je već aktivan";
    }

    if (provjeriIzraz(zahtjev, regex4)) { // PAUZA
      if (this.status.get()) { // ako je aktivan
        this.status.set(false);
        return "OK " + brojacUdaljenosti.get();
      } else { // ako je vec pauziran
        return "ERROR 01 Poslužitelj je pauziran";
      }
    }

    if (provjeriIzraz(zahtjev, regex5)) { // INFO

      if (this.status.get()) { // ako je aktivan
        var naredba = razdvojiIzraz(zahtjev, regex5);

        if (naredba[2].equals("DA") && !this.ispis.get()) {
          this.ispis.set(true);
          return "OK";
        }
        if (naredba[2].equals("DA") && this.ispis.get())
          return "ERROR 03 Poslužitelj već ispisuje na standardni izlaz";
        if (naredba[2].equals("NE") && this.ispis.get()) {
          this.ispis.set(false);
          return "OK";
        }
        if (naredba[2].equals("NE") && !this.ispis.get())
          return "ERROR 04 Poslužitelj već ne ispisuje na standardni izlaz";

      } else { // ako nije aktivan
        return "ERROR 01 Poslužitelj je pauziran";
      }
    }

    if (provjeriIzraz(zahtjev, regex6)) { // UDALJENOST
      if (this.status.get()) {
        var podaci = razdvojiIzraz(zahtjev, regex6);
        brojacUdaljenosti.incrementAndGet();
        return "OK " + String.format("%.2f", izracunajUdaljenost(podaci));
      } else {
        return "ERROR 01 Poslužitelj je pauziran";
      }
    }

    return "ERROR 05 Nepoznata naredba";
  }

  /**
   * Računa udaljenost na temelju podataka iz dolaznog zahtjeva. Računanje se obavlja korištenjem
   * haversine formule. Više na: https://www.geeksforgeeks.org/program-distance-two-points-earth/
   * 
   * @param podaci Podaci s geografskom širinom i geografskom dužinom iz dolaznog zahtjeva
   * @return Vraća udaljenost u kilometrima (km)
   */
  private double izracunajUdaljenost(String[] podaci) {

    double gpsSirina1 = Math.toRadians(Double.parseDouble(podaci[2]));
    double gpsDuzina1 = Math.toRadians(Double.parseDouble(podaci[4]));
    double gpsSirina2 = Math.toRadians(Double.parseDouble(podaci[6]));
    double gpsDuzina2 = Math.toRadians(Double.parseDouble(podaci[8]));

    double sirina = gpsSirina2 - gpsSirina1;
    double duzina = gpsDuzina2 - gpsDuzina1;

    double a = Math.pow(Math.sin(sirina / 2), 2)
        + Math.cos(gpsSirina1) * Math.cos(gpsSirina2) * Math.pow(Math.sin(duzina / 2), 2);

    double c = 2 * Math.asin(Math.sqrt(a));

    return c * r;
  }

  /**
   * Provjerava korektnost izraza korištenjem dozvoljenih izraza (eng. Regular expression)
   * 
   * @param string Izraz koji se provjerava
   * @param regex Regularni izraz s kojim se provjerava
   * @return Vraća true ako je u redu, inače false
   */
  private boolean provjeriIzraz(String string, String regex) {
    String s = string.trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }

  /**
   * Razdvaja izraz po grupama korištenjem dozvoljenih izraza (eng. Regular expression)
   * 
   * @param string Izraz koji se provjerava
   * @param regex Regularni izraz s kojim se provjerava
   * @return Vraća polje s razdvojenim dijelovima izraza
   */
  private String[] razdvojiIzraz(String string, String regex) {
    List<String> rezultat = new ArrayList<>();

    String s = string.trim();

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

}
