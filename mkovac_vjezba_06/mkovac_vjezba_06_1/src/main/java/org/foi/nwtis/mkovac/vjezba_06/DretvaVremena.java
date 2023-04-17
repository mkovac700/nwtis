package org.foi.nwtis.mkovac.vjezba_06;

public class DretvaVremena extends Thread {
  private static int brojDretve = 0;
  private int brojCiklusa;
  private int trajanjeCiklusa;
  private boolean kraj = false;

  public DretvaVremena(int brojCiklusa, int trajanjeCiklusa) {
    super("mkovac-" + brojDretve++);
    this.brojCiklusa = brojCiklusa;
    this.trajanjeCiklusa = trajanjeCiklusa;
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public void run() {
    int brojac = 0;
    /*
     * try { while (brojac < this.brojCiklusa) { System.out.println(
     * "Dretva: " + this.getName() + " brojac " + brojac++);
     * 
     * Thread.sleep(trajanjeCiklusa); } } catch (InterruptedException
     * e) { e.printStackTrace(); }
     */


    while (brojac < this.brojCiklusa && !this.kraj) {
      System.out.println(
          "Dretva: " + this.getName() + " brojac " + brojac++);
      try {
        Thread.sleep(trajanjeCiklusa);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }


  }

  @Override
  public void interrupt() {
    this.kraj = true;
    super.interrupt();
  }


}
