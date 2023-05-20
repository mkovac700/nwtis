package org.foi.nwtis.mkovac.zadaca_3.dretve;

import org.foi.nwtis.Konfiguracija;

public class SakupljacLetovaAviona extends Thread {
  private Konfiguracija konfig;

  public SakupljacLetovaAviona(Konfiguracija konfig) {
    this.konfig = konfig;
  }

  @Override
  public synchronized void start() {
    // TODO Auto-generated method stub
    super.start();
  }

  @Override
  public void interrupt() {
    // TODO Auto-generated method stub
    super.interrupt();
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    super.run();
  }

}
