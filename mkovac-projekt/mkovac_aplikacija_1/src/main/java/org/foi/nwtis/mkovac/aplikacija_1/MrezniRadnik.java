package org.foi.nwtis.mkovac.aplikacija_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

public class MrezniRadnik implements Runnable {
  private Socket uticnica;

  private AtomicBoolean status;
  private AtomicBoolean ispis;
  private AtomicBoolean kraj;
  private AtomicInteger brojacUdaljenosti;

  public MrezniRadnik(Socket uticnica, AtomicBoolean status, AtomicBoolean kraj,
      AtomicBoolean ispis, AtomicInteger brojacUdaljenosti) {
    this.uticnica = uticnica;

    this.status = status;
    this.kraj = kraj;
    this.ispis = ispis;
    this.brojacUdaljenosti = brojacUdaljenosti;
  }

  @Override
  public void run() {
    // flag.set(!flag.get());

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

      // TODO UKLONIT KASNIJE
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

  private String obradiZahtjev(String zahtjev) {

    String regex1 = "(STATUS)$";
    String regex2 = "(KRAJ)$";
    String regex3 = "(INIT)$";
    String regex4 = "(PAUZA)$";
    String regex5 = "(INFO) (DA|NE)$";
    String regex6 =
        "(UDALJENOST) ((\\d*\\.)?\\d+) ((\\d*\\.)?\\d+) ((\\d*\\.)?\\d+) ((\\d*\\.)?\\d+)$";

    if (provjeriIzraz(zahtjev, regex1)) { // STATUS
      if (this.status.get())
        return "OK 1";
      else
        return "OK 0";
    }

    if (provjeriIzraz(zahtjev, regex2)) { // KRAJ
      this.kraj.set(true);
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
        return "OK" + brojacUdaljenosti.get();
      } else { // ako je vec pauziran
        return "ERROR 01 Poslužitelj je pauziran";
      }
    }

    if (provjeriIzraz(zahtjev, regex5)) { // INFO
      var naredba = razdvojiIzraz(zahtjev, regex5);

      if (this.status.get()) { // ako je aktivan
        if (naredba[1].equals("DA") && !this.ispis.get()) {
          this.ispis.set(true);
          return "OK";
        }
        if (naredba[1].equals("DA") && this.ispis.get())
          return "ERROR 03 Poslužitelj već ispisuje na standardni izlaz";
        if (naredba[1].equals("NE") && this.ispis.get()) {
          this.ispis.set(false);
          return "OK";
        }
        if (naredba[1].equals("NE") && !this.ispis.get())
          return "ERROR 04 Poslužitelj već ne ispisuje na standardni izlaz";
      } else { // ako nije aktivan
        return "ERROR 01 Poslužitelj je pauziran";
      }
    }

    if (provjeriIzraz(zahtjev, regex6)) { // UDALJENOST

    }

    return "ERROR 05 Nepoznata naredba";
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
