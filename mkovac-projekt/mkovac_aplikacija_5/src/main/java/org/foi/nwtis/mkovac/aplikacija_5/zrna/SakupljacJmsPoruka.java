package org.foi.nwtis.mkovac.aplikacija_5.zrna;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;

/**
 * Zrno za prikupljanje JMS poruka u kolekciju
 * 
 * @author Marijan Kovač
 *
 */
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

  /**
   * Dodaje poruku u kolekciju
   * 
   * @param poruka Tekst poruke
   * @return true ako je u redu, inače false
   */
  public boolean dodajPoruku(String poruka) {
    if (jmsPoruke.contains(poruka))
      return false;
    else {
      jmsPoruke.add(poruka);
      return true;
    }
  }

  /**
   * Vraća kolekciju poruka
   * 
   * @return Kolekcija poruka
   */
  public List<String> dajSvePoruke() {
    return jmsPoruke;
  }

  /**
   * Vraća kolekciju poruka u zadanom rasponu
   * 
   * @param odBroja Od kojeg podatka se želi dohvatiti (donja granica)
   * @param broj Koliko podataka se želi dohvatiti
   * @return Kolekcija poruka
   */
  public List<String> dajSvePoruke(int odBroja, int broj) {

    if (odBroja < 1 || broj < 1) {
      odBroja = 1;
      broj = 20;
    }

    if (odBroja > jmsPoruke.size())
      return null;

    else if (odBroja - 1 + broj > jmsPoruke.size())
      return jmsPoruke.subList(odBroja - 1, jmsPoruke.size());

    else
      return jmsPoruke.subList(odBroja - 1, odBroja - 1 + broj);

  }

  /**
   * Briše sve poruke iz kolekcije
   */
  public void obrisiSvePoruke() {
    jmsPoruke.clear();
  }

}
