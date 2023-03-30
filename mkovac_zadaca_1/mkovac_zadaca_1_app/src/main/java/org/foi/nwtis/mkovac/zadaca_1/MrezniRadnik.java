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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Korisnik;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Lokacija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Uredaj;
import org.foi.nwtis.mkovac.zadaca_1.podaci.UredajVrsta;

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

  private String obradiZahtjev(String zahtjev) {
    // KORISNIK korisnik LOZINKA lozinka KRAJ
    String regex1 = "KORISNIK ([a-zA-Z0-9_-]{3,10}) LOZINKA ([a-zA-Z0-9_\\-#!]{3,10}) KRAJ$";
    // KORISNIK korisnik LOZINKA lozinka SENZOR idUredaj vrijeme temp vlaga tlak
    String regex2 =
        "KORISNIK ([a-zA-Z0-9_-]{3,10}) LOZINKA ([a-zA-Z0-9_\\-#!]{3,10}) SENZOR ([a-zA-Z0-9_-]+) (\\d{1,2}:\\d{1,2}:\\d{2}) ([0-9]{1,3}(\\.\\d)?)( ([0-9]{1,3}(\\.\\d)?)?)?( ([0-9]{1,3}(\\.\\d)?)?)?$";

    if (provjeriIzraz(zahtjev, regex2)) {
      var podaci = zahtjev.split(" ");

      if (!autenticirajKorisnika(podaci[1], podaci[3]))
        return "ERROR 21 Korisnik ne postoji ili lozinka nije ispravna";

      if (!isAdministrator(podaci[1]))
        return "ERROR 22 Korisnik nije administrator";

      if (!postojiUredaj(podaci[5]))
        return "ERROR 23 Uredaj ne postoji";

      if (!odgovaraTipUredaja(podaci))
        return "ERROR 29 Ne odgovara tip uredaja";

      return podaci[5] + ": OK";
    }

    return "OK";

  }

  private boolean odgovaraTipUredaja(String[] podaci) {
    int brojPodataka = podaci.length - 7;
    boolean odgovara = false;

    switch (brojPodataka) {
      case 1:
        odgovara = this.uredaji.get(podaci[5]).vrsta() == UredajVrsta.SenzorTemperatura;
        break;

      case 2:
        odgovara = this.uredaji.get(podaci[5]).vrsta() == UredajVrsta.SenzorTemperaturaVlaga;
        break;

      case 3:
        odgovara = this.uredaji.get(podaci[5]).vrsta() == UredajVrsta.SenzorTemperaturaVlagaTlak;
        break;

      default:
        odgovara = false;
        break;
    }

    return odgovara;
  }

  private boolean postojiUredaj(String id) {
    return this.uredaji.get(id) != null;
  }

  private boolean isAdministrator(String korime) {
    var korisnik = this.korisnici.get(korime);
    if (korisnik == null)
      return false;
    if (!korisnik.administrator())
      return false;
    return true;
  }

  private boolean autenticirajKorisnika(String korime, String lozinka) {

    var korisnik = this.korisnici.get(korime);
    if (korisnik == null)
      return false;
    if (korisnik.lozinka() != lozinka)
      return false;
    return true;
  }

  private boolean provjeriIzraz(String string, String regex) {
    String s = string.trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }

  @Override
  public void interrupt() {
    // TODO isto kao za start
    super.interrupt();
  }

}
