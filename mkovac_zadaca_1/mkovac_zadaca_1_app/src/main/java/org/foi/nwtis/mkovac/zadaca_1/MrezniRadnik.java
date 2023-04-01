package org.foi.nwtis.mkovac.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Korisnik;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Lokacija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Ocitanje;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Uredaj;
import org.foi.nwtis.mkovac.zadaca_1.podaci.UredajVrsta;

public class MrezniRadnik extends Thread {

  protected Socket mreznaUticnica;
  protected Konfiguracija konfig;
  private int ispis = 0;

  protected Map<String, Korisnik> korisnici;
  protected Map<String, Lokacija> lokacije;
  protected Map<String, Uredaj> uredaji;

  protected List<Ocitanje> listaOcitanja;

  private float odstupanjeTemp;
  private float odstupanjeVlaga;
  private float odstupanjeTlak;

  private String posluziteljUdaljenostiAdresa;
  private int posluziteljUdaljenostiVrata;
  private int maksCekanje;

  public MrezniRadnik(Socket mreznaUticnica, Konfiguracija konfig) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.konfig = konfig;
    this.ispis = Integer.parseInt(konfig.dajPostavku("ispis"));
    this.odstupanjeTemp = Float.parseFloat(konfig.dajPostavku("odstupanjeTemp"));
    this.odstupanjeVlaga = Float.parseFloat(konfig.dajPostavku("odstupanjeVlaga"));
    this.odstupanjeTlak = Float.parseFloat(konfig.dajPostavku("odstupanjeTlak"));
    this.posluziteljUdaljenostiAdresa = konfig.dajPostavku("posluziteljUdaljenostiAdresa");
    this.posluziteljUdaljenostiVrata =
        Integer.parseInt(konfig.dajPostavku("posluziteljUdaljenostiVrata"));
    this.maksCekanje = Integer.parseInt(konfig.dajPostavku("maksCekanje"));
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
    // KORISNIK korisnik LOZINKA lozinka SENZOR idUredaj vrijeme temp vlaga tlak //SENZOR:
    // [a-zA-Z0-9_-]+
    String regex2 =
        "KORISNIK ([a-zA-Z0-9_-]{3,10}) LOZINKA ([a-zA-Z0-9_\\-#!]{3,10}) SENZOR ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+) (\\d{1,2}:\\d{1,2}:\\d{2}) ([0-9]{1,3}(\\.\\d)?)( ([0-9]{1,3}(\\.\\d)?)?)?( ([0-9]{1,4}(\\.\\d)?)?)?$";

    String regex3 =
        "KORISNIK ([a-zA-Z0-9_-]{3,10}) LOZINKA ([a-zA-Z0-9_\\-#!]{3,10}) METEO ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+)$";

    String regex4 =
        "KORISNIK ([a-zA-Z0-9_-]{3,10}) LOZINKA ([a-zA-Z0-9_\\-#!]{3,10}) MAKS (TEMP|VLAGA|TLAK) ([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_-]+)$";

    String regex5 =
        "(KORISNIK) ([a-zA-Z0-9_-]{3,10}) (LOZINKA) ([a-zA-Z0-9_\\-#!]{3,10}) (ALARM) '([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_\\-\\s]+)'$";

    String regex6 =
        "(KORISNIK) ([a-zA-Z0-9_-]{3,10}) (LOZINKA) ([a-zA-Z0-9_\\-#!]{3,10}) (UDALJENOST) '([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_\\-\\s]+)' '([a-zA-ZÀ-ÖØ-öø-ÿČčĆćŽžĐđŠš0-9_\\-\\s]+)'$";

    String regex7 =
        "(KORISNIK) ([a-zA-Z0-9_-]{3,10}) (LOZINKA) ([a-zA-Z0-9_\\-#!]{3,10}) (UDALJENOST) (SPREMI)$";

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

      return dodajOcitanje(podaci);
    }

    if (provjeriIzraz(zahtjev, regex3)) {
      var podaci = zahtjev.split(" ");

      if (!autenticirajKorisnika(podaci[1], podaci[3]))
        return "ERROR 21 Korisnik ne postoji ili lozinka nije ispravna";

      if (!postojiUredaj(podaci[5]))
        return "ERROR 23 Uredaj ne postoji";

      String komanda = podaci[4];
      String kljuc = podaci[5];

      Ocitanje ocitanje = vratiOcitanje(komanda, kljuc);

      if (ocitanje != null) {
        String poruka = "OK " + ocitanje.vrijeme() + " " + ocitanje.temp() + " " + ocitanje.vlaga()
            + " " + ocitanje.tlak();

        return poruka.replaceAll("\\s+", " ").trim();
      } else {
        return "ERROR 29 Nema podataka";
      }
    }

    if (provjeriIzraz(zahtjev, regex4)) {
      var podaci = zahtjev.split(" ");

      if (!autenticirajKorisnika(podaci[1], podaci[3]))
        return "ERROR 21 Korisnik ne postoji ili lozinka nije ispravna";

      if (!postojiUredaj(podaci[6]))
        return "ERROR 23 Uredaj ne postoji";

      String komanda = podaci[4] + " " + podaci[5];
      String kljuc = podaci[6];

      Ocitanje ocitanje = vratiOcitanje(komanda, kljuc);

      if (ocitanje != null) {
        String poruka = "OK " + ocitanje.vrijeme() + " " + ocitanje.temp() + " " + ocitanje.vlaga()
            + " " + ocitanje.tlak();

        return poruka.replaceAll("\\s+", " ").trim();
      } else {
        return "ERROR 29 Nema podataka";
      }
    }

    if (provjeriIzraz(zahtjev, regex5)) {
      var podaci = razdvojiIzraz(zahtjev, regex5); // 0 = SVI; 2 = korime; 4 = lozinka; 6 = lokacija

      if (!autenticirajKorisnika(podaci[2], podaci[4]))
        return "ERROR 21 Korisnik ne postoji ili lozinka nije ispravna";

      if (!postojiLokacija(podaci[6]))
        return "ERROR 24 Lokacija ne postoji";

      String komanda = podaci[5];
      String kljuc = pronadiUredaj(podaci[6]);// podaci[6]

      Ocitanje ocitanje = vratiOcitanje(komanda, kljuc);

      if (ocitanje != null) {
        String poruka = "OK " + ocitanje.id() + " " + ocitanje.vrijeme();
        if (ocitanje.alarmTemp())
          poruka += " TEMP";
        if (ocitanje.alarmVlaga())
          poruka += " VLAGA";
        if (ocitanje.alarmTlak())
          poruka += " TLAK";

        return poruka.replaceAll("\\s+", " ").trim();
      } else {
        return "ERROR 29 Nema podataka";
      }
    }

    if (provjeriIzraz(zahtjev, regex6)) {
      var podaci = razdvojiIzraz(zahtjev, regex6);

      if (!autenticirajKorisnika(podaci[2], podaci[4]))
        return "ERROR 21 Korisnik ne postoji ili lozinka nije ispravna";

      if (!postojiLokacija(podaci[6]))
        return "ERROR 24 Lokacija ne postoji";

      if (!postojiLokacija(podaci[7]))
        return "ERROR 24 Lokacija ne postoji";

      String komanda = podaci[5];
      String lokacija1 = podaci[6];
      String lokacija2 = podaci[7];

      String gpsSirina1 = this.lokacije.get(lokacija1).gpsSirina();
      String gpsDuzina1 = this.lokacije.get(lokacija1).gpsDuzina();
      String gpsSirina2 = this.lokacije.get(lokacija2).gpsSirina();
      String gpsDuzina2 = this.lokacije.get(lokacija2).gpsDuzina();

      String odgovor = kontaktirajPosluziteljUdaljenosti(komanda, gpsSirina1, gpsDuzina1,
          gpsSirina2, gpsDuzina2);

      return odgovor;
    }

    if (provjeriIzraz(zahtjev, regex7)) {
      var podaci = razdvojiIzraz(zahtjev, regex7);

      if (!autenticirajKorisnika(podaci[2], podaci[4]))
        return "ERROR 21 Korisnik ne postoji ili lozinka nije ispravna";

      String komanda1 = podaci[5];
      String komanda2 = podaci[6];

      String odgovor = kontaktirajPosluziteljUdaljenosti(komanda1, komanda2);

      return odgovor;
    }

    return "ERROR 20 Format komande nije ispravan";

  }

  private String kontaktirajPosluziteljUdaljenosti(String komanda1, String komanda2) {
    String odgovor = "";

    try {
      var mreznaUticnica = new Socket(posluziteljUdaljenostiAdresa, posluziteljUdaljenostiVrata);

      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String zahtjev = komanda1 + " " + komanda2;

      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();// sa slanja na primanje

      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        poruka.append(red);
      }

      odgovor = poruka.toString();

      mreznaUticnica.shutdownInput(); // s primanja na slanje
      mreznaUticnica.close();

    } catch (NumberFormatException | IOException e) {
      // TODO: handle exception
      odgovor = "ERROR 25 PosluziteljUdaljenosti ne radi";
      e.printStackTrace();
    }

    return odgovor;
  }

  private String kontaktirajPosluziteljUdaljenosti(String komanda, String gpsSirina1,
      String gpsDuzina1, String gpsSirina2, String gpsDuzina2) {

    String odgovor = "ERROR 29 Nema odgovora od PosluziteljUdaljenosti";

    try {
      var mreznaUticnica = new Socket(posluziteljUdaljenostiAdresa, posluziteljUdaljenostiVrata);

      mreznaUticnica.setSoTimeout(maksCekanje);

      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String zahtjev =
          komanda + " " + gpsSirina1 + " " + gpsDuzina1 + " " + gpsSirina2 + " " + gpsDuzina2;

      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();// sa slanja na primanje

      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        poruka.append(red);
      }

      odgovor = poruka.toString();

      mreznaUticnica.shutdownInput(); // s primanja na slanje
      mreznaUticnica.close();

    } catch (NumberFormatException | IOException e) {
      // TODO: handle exception
      odgovor = "ERROR 25 PosluziteljUdaljenosti ne radi";
      e.printStackTrace();
    }

    return odgovor;
  }

  private synchronized Ocitanje vratiOcitanje(String komanda, String kljuc) {
    Ocitanje ocitanje = null;
    List<Ocitanje> tmp = new ArrayList<>(listaOcitanja);
    Collections.reverse(tmp);

    if (komanda.equals("METEO")) { // METEO
      for (Ocitanje o : tmp) {
        if (o.id().equals(kljuc)) {
          ocitanje = o;
          break;
        }
      }
    }

    if (komanda.equals("MAKS TEMP")) {
      float max = -999;
      for (Ocitanje o : tmp) {
        if (o.id().equals(kljuc) && Float.parseFloat(o.temp()) > max) {
          max = Float.parseFloat(o.temp());
          ocitanje = o;
        }
      }
    }

    if (komanda.equals("MAKS VLAGA")) {
      float max = -999;
      for (Ocitanje o : tmp) {
        if (o.id().equals(kljuc) && Float.parseFloat(o.vlaga()) > max) {
          max = Float.parseFloat(o.temp());
          ocitanje = o;
        }
      }
    }

    if (komanda.equals("MAKS TLAK")) {
      float max = -999;
      for (Ocitanje o : tmp) {
        if (o.id().equals(kljuc) && Float.parseFloat(o.tlak()) > max) {
          max = Float.parseFloat(o.temp());
          ocitanje = o;
        }
      }
    }

    if (komanda.equals("ALARM")) {
      for (Ocitanje o : tmp) {
        if (o.id().equals(kljuc) && o.alarm()) {
          ocitanje = o;
          break;
        }
      }
    }

    return ocitanje;
  }

  // TODO malo pocistiti funkciju, izdvojiti dijelove ili napraviti override
  private synchronized String dodajOcitanje(String[] podaci) {
    boolean[] odstupaMeteo = {false, false, false};
    boolean alarm = false;
    String poruka = "";

    for (Ocitanje o : listaOcitanja) {
      if (o.id().equals(podaci[5])) {
        if (brojMeteoPodataka(podaci) >= 1
            && Math.abs(Float.parseFloat(o.temp()) - Float.parseFloat(podaci[7])) > odstupanjeTemp)
          odstupaMeteo[0] = true;
        if (brojMeteoPodataka(podaci) >= 2 && Math
            .abs(Float.parseFloat(o.vlaga()) - Float.parseFloat(podaci[8])) > odstupanjeVlaga)
          odstupaMeteo[1] = true;
        if (brojMeteoPodataka(podaci) == 3
            && Math.abs(Float.parseFloat(o.tlak()) - Float.parseFloat(podaci[9])) > odstupanjeTlak)
          odstupaMeteo[2] = true;
      }
    }

    if (odstupaMeteo[0] || odstupaMeteo[1] || odstupaMeteo[2]) {
      alarm = true;
      // obriši ranija očitanja
      Iterator<Ocitanje> i = listaOcitanja.iterator();
      while (i.hasNext()) {
        Ocitanje o = i.next();
        if (o.id().equals(podaci[5]))
          i.remove();
      }

      // dodaj zapis s alarmom
      if (brojMeteoPodataka(podaci) == 3)
        listaOcitanja.add(new Ocitanje(podaci[5], podaci[6], podaci[7], podaci[8], podaci[9],
            odstupaMeteo[0], odstupaMeteo[1], odstupaMeteo[2], alarm));
      if (brojMeteoPodataka(podaci) == 2)
        listaOcitanja.add(new Ocitanje(podaci[5], podaci[6], podaci[7], podaci[8], "",
            odstupaMeteo[0], odstupaMeteo[1], odstupaMeteo[2], alarm));
      if (brojMeteoPodataka(podaci) == 1)
        listaOcitanja.add(new Ocitanje(podaci[5], podaci[6], podaci[7], "", "", odstupaMeteo[0],
            odstupaMeteo[1], odstupaMeteo[2], alarm));

      poruka = "OK ALARM";
      if (odstupaMeteo[0])
        poruka += " TEMP";
      if (odstupaMeteo[1])
        poruka += " VLAGA";
      if (odstupaMeteo[2])
        poruka += " TLAK";
    } else { // samo dodaj zapis bez alarma
      if (brojMeteoPodataka(podaci) == 3)
        listaOcitanja.add(new Ocitanje(podaci[5], podaci[6], podaci[7], podaci[8], podaci[9],
            odstupaMeteo[0], odstupaMeteo[1], odstupaMeteo[2], alarm));
      if (brojMeteoPodataka(podaci) == 2)
        listaOcitanja.add(new Ocitanje(podaci[5], podaci[6], podaci[7], podaci[8], "",
            odstupaMeteo[0], odstupaMeteo[1], odstupaMeteo[2], alarm));
      if (brojMeteoPodataka(podaci) == 1)
        listaOcitanja.add(new Ocitanje(podaci[5], podaci[6], podaci[7], "", "", odstupaMeteo[0],
            odstupaMeteo[1], odstupaMeteo[2], alarm));
      poruka = "OK";
    }

    return poruka;
  }

  private int brojMeteoPodataka(String[] podaci) {
    return podaci.length - 7;
  }

  private boolean odgovaraTipUredaja(String[] podaci) {
    boolean odgovara = false;

    switch (brojMeteoPodataka(podaci)) {
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

  private String pronadiUredaj(String idLokacija) {
    String idUredaj = "";
    for (Map.Entry<String, Uredaj> entry : uredaji.entrySet()) {
      if (entry.getValue().idLokacija().equals(idLokacija)) {
        idUredaj = entry.getKey();
        break;
      }
    }
    return idUredaj;
  }

  private boolean postojiLokacija(String id) {
    return this.lokacije.get(id) != null;
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
    if (!korisnik.lozinka().equals(lozinka))
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

  @Override
  public void interrupt() {
    // TODO isto kao za start
    super.interrupt();
  }

}
