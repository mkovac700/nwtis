package org.foi.nwtis.mkovac.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.mkovac.zadaca_1.podaci.MeteoSimulacija;

public class SimulatorMeteo {

  public static void main(String[] args) {
    var sm = new SimulatorMeteo();
    if (!SimulatorMeteo.provjeriArgumente(args)) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke!");
      return;
    }

    try {
      sm.ucitajPostavke(args[0]);
    } catch (NeispravnaKonfiguracija e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    MeteoSimulacija nesto = null; // klasa se moze kreirati na desni klik, pa onda odabrati i
                                  // package u koji se zeli staviti
  }

  private Konfiguracija ucitajPostavke(String string) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(string);

  }

  private static boolean provjeriArgumente(String[] args) {
    return args.length == 1 ? true : false;
  }

}
