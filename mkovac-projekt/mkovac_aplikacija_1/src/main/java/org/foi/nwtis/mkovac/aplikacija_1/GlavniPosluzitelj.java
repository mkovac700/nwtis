package org.foi.nwtis.mkovac.aplikacija_1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.foi.nwtis.Konfiguracija;
import lombok.Setter;

public class GlavniPosluzitelj {
  private Konfiguracija konfig;

  @Setter
  private boolean status;

  /**
   * Konstruktor za GlavniPosluzitelj
   * 
   * @param konfig Objekt tipa Konfiguracija s postavkama
   */
  public GlavniPosluzitelj(Konfiguracija konfig) {
    this.konfig = konfig;
  }

  public void pokreniPosluzitelja() {
    otvoriMreznaVrata();
  }

  private void otvoriMreznaVrata() {
    // try (var posluzitelj = new ServerSocket(0, 0)) {
    // var dretva = new MrezniRadnik(this::setStatus); // lombok
    // } catch (IOException e) {
    // Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    // }
    status = false;

    System.out.println("status na pocetku: " + status);

    ExecutorService executor = Executors.newFixedThreadPool(5);
    var dretva = new MrezniRadnik(this::setStatus);
    executor.execute(dretva);

    executor.shutdown();
    while (!executor.isTerminated());

    System.out.println("status nakon azuriranja: " + status);
    System.out.println("kraj rada");
  }



}
