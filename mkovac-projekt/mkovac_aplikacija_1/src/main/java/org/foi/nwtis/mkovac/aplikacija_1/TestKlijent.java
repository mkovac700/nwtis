package org.foi.nwtis.mkovac.aplikacija_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Testna klasa za testiranje rada poslužitelja
 * 
 * @author Marijan Kovač
 *
 */
public class TestKlijent {

  private String zahtjev;

  /**
   * Glavna funkcija koja služi za pokretanje programa TestKlijent
   * 
   * @param args Naredba za izvršavanje određenog zahtjeva
   */
  public static void main(String[] args) {
    TestKlijent tk = new TestKlijent();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      System.out.println("Unesite naredbu: ");
      try {
        tk.zahtjev = br.readLine();
        System.out.println("Unijeli ste naredbu: " + tk.zahtjev);
        tk.spojiSeNaPosluzitelj("localhost", 8000);
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }
    }
  }

  /**
   * Spaja se na GlavniPosluzitelj, šalje korisnikov zahtjev i čeka na odgovor.
   * 
   * @param adresa GlavniPosluzitelj adresa
   * @param vrata GlavniPosluzitelj vrata
   */
  private void spojiSeNaPosluzitelj(String adresa, int vrata) {
    try {
      var mreznaUticnica = new Socket(adresa, vrata);

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
