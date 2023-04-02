package org.foi.nwtis.mkovac.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Udaljenost;

/**
 * Klasa PosluziteljUdaljenosti zadužena je za izračunavanje udaljenosti između dviju lokacija na
 * temelju njihove geografske širine i geografske dužine. Izračun se obavlja korištenjem haversine
 * formule. Program pamti zadnjih nekoliko izračuna, te ima mogućnost spremanja istih u datoteku.
 * Svoj rad započinje učitavanjem konfiguracijskih postavki. Klasa ima ulogu poslužitelja na koji se
 * mogu spajati klijenti putem mrežne utičnice.
 * 
 * @author Marijan Kovač
 *
 */
public class PosluziteljUdaljenosti {

  private int mreznaVrata = 8000;
  private int brojCekaca = 0;
  private int brojZadnjihSpremljenih = 0;
  private String datotekaSerijalizacija = "";

  private final double r = 6371;

  private List<Udaljenost> listaUdaljenosti = new ArrayList<>();
  List<Udaljenost> tmp;

  /**
   * Glavna funkcija koja služi za pokretanje programa PosluziteljUdaljenosti
   * 
   * @param args Naziv konfiguracijske datoteke s postavkama za pokretanje programa. Dozvoljeni
   *        formati za datoteku su: .txt | .xml | .bin | .json | .yaml
   */
  public static void main(String[] args) {
    var pu = new PosluziteljUdaljenosti();

    if (!PosluziteljUdaljenosti.provjeriArgumente(args)) {
      Logger.getGlobal().log(Level.SEVERE,
          "Nije upisan naziv datoteke ili datoteka nema odgovarajući format!");
      return;
    }

    try {
      Konfiguracija konf = pu.ucitajPostavke(args[0]);
      pu.pokreniPosluziteljaUdaljenosti(konf);
    } catch (NeispravnaKonfiguracija e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /**
   * Učitava postavke iz datoteke u objekt Konfiguracija
   * 
   * @param datoteka Datoteka s konfiguracijskim postavkama
   * @return Vraća objekt tipa Konfiguracija
   * @throws NeispravnaKonfiguracija Baca iznimku ako učitavanje postavki nije uspjelo
   */
  private Konfiguracija ucitajPostavke(String string) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(string);
  }

  /**
   * Funkcija provjerava je li ulazni argument ispravan.
   * 
   * @param args Ulazni argument
   * @return Vraća true ako je u redu, inače false.
   */
  private static boolean provjeriArgumente(String[] args) {
    if (args.length != 1)
      return false;

    String regex = "\\w+.(txt|xml|bin|json|yaml)$";
    String s = args[0].trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }

  /**
   * Funkcija obavlja pokretanje poslužitelja. Vrši učitavanje konfiguracijskih postavki, priprema
   * datoteku za serijalizaciju podataka o izračunima te otvara mrežna vrata.
   * 
   * @param konf Objekt s postavkama učitanim iz konfiguracijske datoteke
   * @see otvoriMreznaVrata
   */
  private void pokreniPosluziteljaUdaljenosti(Konfiguracija konf) {
    // ucitat postavke
    mreznaVrata = Integer.parseInt(konf.dajPostavku("mreznaVrata"));
    brojCekaca = Integer.parseInt(konf.dajPostavku("brojCekaca"));
    brojZadnjihSpremljenih = Integer.parseInt(konf.dajPostavku("brojZadnjihSpremljenih"));
    datotekaSerijalizacija = konf.dajPostavku("datotekaSerijalizacija");

    try {
      ucitajDatotekuSerijalizacija(datotekaSerijalizacija);
    } catch (IOException | ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    otvoriMreznaVrata();
  }

  /**
   * Učitava binarnu datoteku te obavlja deserijalizaciju podataka u listu s podacima.
   * 
   * @param nazivDatoteke Naziv binarne datoteke
   * @throws IOException Baca iznimku ako datoteka ne postoji ili ju nije moguće otvoriti
   * @throws ClassNotFoundException Baca iznimku ako se ne može obaviti cast objekta iz datoteke u
   *         listu
   */
  private void ucitajDatotekuSerijalizacija(String nazivDatoteke)
      throws IOException, ClassNotFoundException {
    var putanja = Path.of(nazivDatoteke);

    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    if (putanja.toFile().length() != 0) {
      ObjectInputStream citac = new ObjectInputStream(new FileInputStream(putanja.toFile()));

      listaUdaljenosti = (List<Udaljenost>) citac.readObject();

      citac.close();
    }

  }

  /**
   * Učitava binarnu datoteku te obavlja serijalizaciju podataka iz liste s podacima.
   * 
   * @param nazivDatoteke Naziv binarne datoteke
   * @throws IOException Baca iznimku ako datoteka ne postoji ili ju nije moguće otvoriti
   * 
   */
  private void spremiDatotekuSerijalizacija(String nazivDatoteke) throws IOException {
    var putanja = Path.of(nazivDatoteke);

    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isWritable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    ObjectOutputStream pisac = new ObjectOutputStream(new FileOutputStream(putanja.toFile()));

    pisac.writeObject(this.listaUdaljenosti);

    pisac.close();
  }

  /**
   * Otvara mrežna vrata te čeka na spajanje klijenata. Obrađuje dolazni zahtjev na način da obavlja
   * izračun udaljenosti ili sprema nedavno primljene podatke u datoteku.
   * 
   * @see obradiZahtjev
   */
  private void otvoriMreznaVrata() {
    try (ServerSocket posluzitelj = new ServerSocket(this.mreznaVrata, this.brojCekaca)) {
      while (true) {
        Socket uticnica = posluzitelj.accept();

        var citac = new BufferedReader(
            new InputStreamReader(uticnica.getInputStream(), Charset.forName("UTF-8")));
        var pisac = new BufferedWriter(
            new OutputStreamWriter(uticnica.getOutputStream(), Charset.forName("UTF-8")));

        var zahtjev = new StringBuilder();
        while (true) {
          var red = citac.readLine();
          if (red == null)
            break;
          zahtjev.append(red);
        }

        uticnica.shutdownInput();

        String odgovor = obradiZahtjev(zahtjev.toString());

        pisac.write(odgovor);
        pisac.flush();

        uticnica.shutdownOutput();
        uticnica.close();

      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Obrađuje dolazni zahtjev na temelju pripremljenih dozvoljenih zahtjeva. Ukoliko se zahtijeva
   * izračun udaljenosti, provjerava ima li već nedavno izračunatu udaljenost za isti zahtjev te ako
   * nema obavlja izračun. Ukoliko se zahtijeva spremanje, sprema nedavno primljene podatke u
   * datoteku.
   * 
   * @param zahtjev Dolazni formatirani zahtjev
   * @return Vraća traženi podatak klijentu ako je uspješno ili grešku.
   */
  private String obradiZahtjev(String zahtjev) {
    String regex1 =
        "UDALJENOST ((\\d*\\.)?\\d+) ((\\d*\\.)?\\d+) ((\\d*\\.)?\\d+) ((\\d*\\.)?\\d+)$";

    String regex2 = "UDALJENOST SPREMI$";

    if (provjeriIzraz(zahtjev, regex1)) {
      var podaci = zahtjev.split(" ");

      boolean postoji = false;
      String udaljenost = "";

      for (Udaljenost u : listaUdaljenosti) {
        if (podaci[1].equals(u.gpsSirina1()) && podaci[2].equals(u.gpsDuzina1())
            && podaci[3].equals(u.gpsSirina2()) && podaci[4].equals(u.gpsDuzina2())) {
          postoji = true;
          udaljenost = u.udaljenost();
          break;
        }
      }

      if (postoji) {
        return "OK " + String.format("%.2f", Double.parseDouble(udaljenost));
      } else {
        double rezultat = izracunajUdaljenost(podaci);
        if (listaUdaljenosti.size() == brojZadnjihSpremljenih)
          listaUdaljenosti.remove(0);
        listaUdaljenosti.add(
            new Udaljenost(podaci[1], podaci[2], podaci[3], podaci[4], Double.toString(rezultat)));

        return "OK " + String.format("%.2f", rezultat);
      }

    } else if (provjeriIzraz(zahtjev, regex2)) {
      try {
        spremiDatotekuSerijalizacija(datotekaSerijalizacija);
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        return "ERROR 19 Greska u spremanju podataka u datoteku";
      }
      return "OK";
    } else {
      return "ERROR 10 Format komande nije ispravan";
    }
  }

  /**
   * Računa udaljenost na temelju podataka iz dolaznog zahtjeva. Računanje se obavlja korištenjem
   * haversine formule. Više na: https://www.geeksforgeeks.org/program-distance-two-points-earth/
   * 
   * @param podaci Podaci s geografskom širinom i geografskom dužinom iz dolaznog zahtjeva
   * @return Vraća udaljenost u kilometrima (km)
   */
  private double izracunajUdaljenost(String[] podaci) {
    System.out.println(Double.parseDouble(podaci[1]) + " " + Double.parseDouble(podaci[2]) + " "
        + Double.parseDouble(podaci[3]) + " " + Double.parseDouble(podaci[4]));

    double gpsSirina1 = Math.toRadians(Double.parseDouble(podaci[1]));
    double gpsDuzina1 = Math.toRadians(Double.parseDouble(podaci[2]));
    double gpsSirina2 = Math.toRadians(Double.parseDouble(podaci[3]));
    double gpsDuzina2 = Math.toRadians(Double.parseDouble(podaci[4]));

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

}
