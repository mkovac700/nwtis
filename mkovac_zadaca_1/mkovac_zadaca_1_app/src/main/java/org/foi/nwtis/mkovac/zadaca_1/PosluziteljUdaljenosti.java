package org.foi.nwtis.mkovac.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.Udaljenost;

public class PosluziteljUdaljenosti {

  private int mreznaVrata;
  private int brojCekaca;

  private List<Udaljenost> listaUdaljenosti;

  public static void main(String[] args) {
    var pu = new PosluziteljUdaljenosti();

    if (!PosluziteljUdaljenosti.provjeriArgumente(args)) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
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

  private Konfiguracija ucitajPostavke(String string) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(string);
  }

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

  private void pokreniPosluziteljaUdaljenosti(Konfiguracija konf) {
    // ucitat postavke
    mreznaVrata = Integer.parseInt(konf.dajPostavku("mreznaVrata"));
    brojCekaca = Integer.parseInt(konf.dajPostavku("brojCekaca"));

    // otvorit port
    otvoriMreznaVrata();
  }

  private void otvoriMreznaVrata() {
    try (ServerSocket posluzitelj = new ServerSocket(this.mreznaVrata, this.brojCekaca)) {
      while (true) {
        Socket uticnica = posluzitelj.accept(); // program stoji i ceka da dode zahtjev
        /*
         * var dretva = new MrezniRadnik(uticnica, konfig); dretva.start();
         */

        // nakon dolaska samo treba obraditi?
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

        obradiZahtjev(zahtjev.toString());



      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private String obradiZahtjev(String zahtjev) {
    String regex1 =
        "UDALJENOST ([(\\d*\\.)?\\d+]) ([(\\d*\\.)?\\d+]) ([(\\d*\\.)?\\d+]) ([(\\d*\\.)?\\d+])$";

    String regex2 = "";

    if (provjeriIzraz(zahtjev, regex1)) { // UDALJENOST 46.30771 16.33808 46.02419 15.90968
      var podaci = zahtjev.split(" "); // podaci[0] komanda

      // TODO provjeru postojanja izdvojiti u funkc
      boolean postoji = false;
      String udaljenost = "";

      for (Udaljenost u : listaUdaljenosti) {
        if (podaci[1] == u.gpsSirina1() && podaci[2] == u.gpsDuzina1()
            && podaci[3] == u.gpsSirina2() && podaci[4] == u.gpsDuzina2()) {
          postoji = true;
          udaljenost = u.udaljenost();
          break;
        }
      }

      if (postoji) {
        return "OK " + udaljenost;
      } else { // izracunaj udaljenost
        double gpsSirina1 = Double.parseDouble(podaci[1]);
        double gpsDuzina1 = Double.parseDouble(podaci[2]);
        double gpsSirina2 = Double.parseDouble(podaci[3]);
        double gpsDuzina2 = Double.parseDouble(podaci[4]);

        gpsSirina1 = Math.toRadians(gpsSirina1); // latitude = sirina
        gpsDuzina1 = Math.toRadians(gpsDuzina1); // longitude = duzina
        gpsSirina2 = Math.toRadians(gpsSirina2);
        gpsDuzina2 = Math.toRadians(gpsDuzina2);

        double sirina = gpsSirina2 - gpsSirina1; // lat
        double duzina = gpsDuzina2 + gpsDuzina1; // lon

        double a = Math.pow(Math.sin(sirina) / 2, 2)
            + Math.cos(gpsSirina1) * Math.cos(gpsSirina2) * Math.pow(Math.sin(duzina / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        double r = 6371;

        double rezultat = c * r;

        return "OK " + rezultat;
      }

    } else if (provjeriIzraz(zahtjev, regex2)) { // UDALJENOST SPREMI
      return "";
    } else { // neispravno; vrati ERROR 10
      return "ERROR 10";
    }
  }

  private boolean provjeriIzraz(String string, String regex) {
    // String regex = "\\w+.(txt|xml|bin|json|yaml)$";
    String s = string.trim();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);

    boolean status = matcher.matches();

    return status;
  }

}
