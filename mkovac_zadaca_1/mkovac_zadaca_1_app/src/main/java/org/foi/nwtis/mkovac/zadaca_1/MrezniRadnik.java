package org.foi.nwtis.mkovac.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Korisnik;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Lokacija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Uredaj;

public class MrezniRadnik extends Thread {

  protected Socket mreznaUticnica;
  protected Konfiguracija konfig;
  private int ispis = 0;

  protected Map<String, Korisnik> korisnici;
  protected Map<String, Lokacija> lokacije;
  protected Map<String, Uredaj> uredaji;

  public MrezniRadnik(Socket mreznaUticnica, Konfiguracija konfig) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.konfig = konfig;
    this.ispis = Integer.parseInt(konfig.dajPostavku("ispis"));
  }

  @Override
  public synchronized void start() {
    // TODO ovdje ide vlastiti dio, a super.start je obavezan i ide na kraju
    super.start();
  }

  @Override
  public void run() {
    // TODO ovdje ne treba super.run, samo se pise vlastiti dio
    try {
      var citac = new BufferedReader(
          new InputStreamReader(this.mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        if (this.ispis == 1) {
          Logger.getGlobal().log(Level.INFO, red);
        }
        poruka.append(red);
      }

      this.mreznaUticnica.shutdownInput();// prebacujemo se s primanja na slanje

      String odgovor = this.obradiZahtjev(poruka.toString());
      Logger.getGlobal().log(Level.INFO, "Odgovor: " + odgovor);
      pisac.write(odgovor);
      pisac.flush();
      this.mreznaUticnica.shutdownOutput();
      this.mreznaUticnica.close(); // zatvara konekciju klijent-posluzitelj
      // dretva zavrsava



    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private String obradiZahtjev(String string) {
    return "OK";

  }

  @Override
  public void interrupt() {
    // TODO isto kao za start
    super.interrupt();
  }

}
