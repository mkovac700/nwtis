package org.foi.nwtis.mkovac.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Klasa PokretacPosluzitelja zadužena je za pokretanje GlavniPosluzitelj na temelju
 * konfiguracijskih postavki.
 * 
 * @author Marijan Kovač
 *
 */
public class PokretacPosluzitelja {

  /**
   * Glavna funkcija koja služi za pokretanje programa PokretacPosluzitelja
   * 
   * @param args Naziv konfiguracijske datoteke s postavkama za pokretanje programa. Dozvoljeni
   *        formati za datoteku su: .txt | .xml | .bin | .json | .yaml
   */
  public static void main(String[] args) {

    var pokretac = new PokretacPosluzitelja();
    if (!pokretac.provjeriArgumente(args)) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke!");
      return;
    }

    try {
      var konf = pokretac.ucitajPostavke(args[0]);
      GlavniPosluzitelj posluzitelj = new GlavniPosluzitelj(konf);
      posluzitelj.pokreniPosluzitelja();
    } catch (NeispravnaKonfiguracija e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
    }

  }

  /**
   * Provjerava je li ulazni argument ispravan
   * 
   * @param args Ulazni argument
   * @return Vraća true ako je u redu, inače false.
   */
  private boolean provjeriArgumente(String[] args) {
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
   * Učitava postavke iz datoteke u objekt Konfiguracija
   * 
   * @param nazivDatoteke Datoteka s konfiguracijskim postavkama
   * @return Vraća objekt tipa Konfiguracija
   * @throws NeispravnaKonfiguracija Baca iznimku ako učitavanje postavki nije uspjelo
   */
  private Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);

  }

}
