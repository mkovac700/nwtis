package org.foi.nwtis.mkovac.zadaca_3.zrna;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;

@Singleton
public class SakupljacJmsPoruka {

  private List<String> jmsPoruke;

  public SakupljacJmsPoruka() {
    jmsPoruke = new ArrayList<String>();
    Logger.getGlobal().log(Level.INFO, "SakupljacJmsPoruka- kreiran");
  }

  @PostConstruct
  public void spreman() {
    Logger.getGlobal().log(Level.INFO, "SakupljacJmsPoruka- spreman");
  }

  public boolean dodajPoruku(String poruka) {
    if (jmsPoruke.contains(poruka))
      return false;
    else {
      jmsPoruke.add(poruka);
      return true;
    }
  }

  public List<String> dajSvePoruke() {
    return jmsPoruke;
  }

}
