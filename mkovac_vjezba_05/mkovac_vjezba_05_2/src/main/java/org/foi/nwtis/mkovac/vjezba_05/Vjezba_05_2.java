package org.foi.nwtis.mkovac.vjezba_05;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.KonfiguracijaBP;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.PostavkeBazaPodataka;

public class Vjezba_05_2 {

  protected KonfiguracijaBP konfBP;

  public static void main(String[] args) {
    var vjezba = new Vjezba_05_2();
    if (!vjezba.provjeriArgumente(args)) {
      Logger.getGlobal().log(Level.SEVERE, "Mora biti upisan jedan argument!");
      return;
    }
    vjezba.konfBP = new PostavkeBazaPodataka(args[0]);
    try {
      vjezba.konfBP.ucitajKonfiguraciju();
    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  private boolean provjeriArgumente(String[] args) {
    return (args.length == 1) ? true : false;
  }

}
