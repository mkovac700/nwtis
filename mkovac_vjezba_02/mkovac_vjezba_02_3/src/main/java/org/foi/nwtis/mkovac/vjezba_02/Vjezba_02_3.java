package org.foi.nwtis.mkovac.vjezba_02;

import java.util.Properties;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

public class Vjezba_02_3 {

  public Vjezba_02_3() {
    // TODO Auto-generated constructor stub
  }

  // za projekte i zadace ne radit ovako da main baca exception, nego se mora napraviti
  // try..catch!!!!!!!!!!!!!!!!!
  public static void main(String[] args) throws NeispravnaKonfiguracija {
    Konfiguracija konf = null;
    switch (args.length) {
      case 1:
        System.out.println("Ispis svih postavki");
        konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
        Properties postavke = konf.dajSvePostavke();
        for (Object o : postavke.keySet()) {
          // cast uvijek ide od potklase na natklasu
          String k = (String) o;
          String v = konf.dajPostavku(k);
          System.out.println(k + "=> " + v);
        }
        break;
      case 2:
        System.out.println("Ispis jedne postavke");
        konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
        String v = konf.dajPostavku(args[1]);
        System.out.println(args[0] + "=> " + v);
        break;
      case 3:
        System.out.println("Dodavanje nove postavke");
        konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
        // samo u memoriji (u objektu konf)
        konf.spremiPostavku(args[1], args[2]);
        // tek sad u datoteci
        konf.spremiKonfiguraciju();
        break;
      default:
        System.out.println("Neispravni unos");
        break;
    }

  }

}
