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

public class PosluziteljUdaljenosti {

  private int mreznaVrata = 8000;
  private int brojCekaca = 0;
  private int brojZadnjihSpremljenih = 0;
  private String datotekaSerijalizacija = "";

  private final double r = 6371;

  private List<Udaljenost> listaUdaljenosti = new ArrayList<>();

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
    brojZadnjihSpremljenih = Integer.parseInt(konf.dajPostavku("brojZadnjihSpremljenih"));
    datotekaSerijalizacija = konf.dajPostavku("datotekaSerijalizacija");

    // pripremit dat serial
    try {
      ucitajDatotekuSerijalizacija(datotekaSerijalizacija);
    } catch (IOException | ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // otvorit port
    otvoriMreznaVrata();
  }

  private void ucitajDatotekuSerijalizacija(String nazivDatoteke)
      throws IOException, ClassNotFoundException {
    var putanja = Path.of(nazivDatoteke);

    if (Files.exists(putanja) && (Files.isDirectory(putanja) || !Files.isReadable(putanja))) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    if (putanja.toFile().length() != 0) {
      ObjectInputStream citac = new ObjectInputStream(new FileInputStream(putanja.toFile()));

      Object o;
      while ((o = citac.readObject()) != null)
        listaUdaljenosti = (List<Udaljenost>) o;

      citac.close();
    }

  }

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

  private void otvoriMreznaVrata() {
    try (ServerSocket posluzitelj = new ServerSocket(this.mreznaVrata, this.brojCekaca)) {
      while (true) {
        System.out.println("čekam zahtjev...");
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

  private String obradiZahtjev(String zahtjev) {
    String regex1 =
        "UDALJENOST ([(\\d*\\.)?\\d+]) ([(\\d*\\.)?\\d+]) ([(\\d*\\.)?\\d+]) ([(\\d*\\.)?\\d+])$";

    String regex2 = "UDALJENOST SPREMI$";

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
        double rezultat = izracunajUdaljenost(podaci);
        if (listaUdaljenosti.size() == brojZadnjihSpremljenih)
          listaUdaljenosti.remove(brojZadnjihSpremljenih);
        listaUdaljenosti.add(
            new Udaljenost(podaci[1], podaci[2], podaci[3], podaci[4], Double.toString(rezultat)));

        return "OK " + String.format("%.2f", rezultat);
      }

    } else if (provjeriIzraz(zahtjev, regex2)) { // UDALJENOST SPREMI
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

  private double izracunajUdaljenost(String[] podaci) {

    double gpsSirina1 = Math.toRadians(Double.parseDouble(podaci[1])); // latitude = sirina
    double gpsDuzina1 = Math.toRadians(Double.parseDouble(podaci[2])); // longitude = duzina
    double gpsSirina2 = Math.toRadians(Double.parseDouble(podaci[3]));
    double gpsDuzina2 = Math.toRadians(Double.parseDouble(podaci[4]));

    double sirina = gpsSirina2 - gpsSirina1; // lat
    double duzina = gpsDuzina2 + gpsDuzina1; // lon

    double a = Math.pow(Math.sin(sirina) / 2, 2)
        + Math.cos(gpsSirina1) * Math.cos(gpsSirina2) * Math.pow(Math.sin(duzina / 2), 2);

    double c = 2 * Math.asin(Math.sqrt(a));

    return c * r;
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
