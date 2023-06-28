package org.foi.nwtis.mkovac.aplikacija_2.klijenti;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.aplikacija_2.slusaci.RestServisSlusac;
import jakarta.servlet.ServletContext;

/**
 * Klijent za rad s poslužiteljem AP1
 * 
 * @author Marijan Kovač
 *
 */
public class AP1Klijent {

  private String AP1_adresa;
  private int AP1_vrata;

  public AP1Klijent() {
    ServletContext context = RestServisSlusac.getServletContext();
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");

    this.AP1_adresa = konf.dajPostavku("AP1.adresa");
    try {
      this.AP1_vrata = Integer.parseInt(konf.dajPostavku("AP1.vrata"));
    } catch (NumberFormatException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  /**
   * Spaja se na poslužitelj, šalje zahtjev i čeka na odgovor.
   * 
   * @param zahtjev Zahtjev koji se šalje
   */
  public String posaljiZahtjev(String zahtjev) {
    String odgovor = null;

    try {
      var mreznaUticnica = new Socket(this.AP1_adresa, this.AP1_vrata);

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

      odgovor = poruka.toString();
      Logger.getGlobal().log(Level.INFO, "Odgovor: " + poruka);

      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      odgovor = e.getMessage();
    }

    return odgovor;
  }
}
