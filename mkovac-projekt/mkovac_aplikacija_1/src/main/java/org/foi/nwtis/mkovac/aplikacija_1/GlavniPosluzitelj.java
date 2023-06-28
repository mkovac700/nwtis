package org.foi.nwtis.mkovac.aplikacija_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;

/**
 * Klasa GlavniPosluzitelj zadužena je za otvaranje veze na određenim mrežnim vratima. Na
 * poslužitelja se mogu spojiti klijenti koji šalju različite zahtjeve za obradu.
 * 
 * @author Marijan Kovač
 *
 */
public class GlavniPosluzitelj {
  private Konfiguracija konfig;

  private AtomicBoolean status;

  private AtomicBoolean ispis;

  private AtomicInteger brojacUdaljenosti;

  private int mreznaVrata = 8000;
  private int brojRadnika = 10;
  private int brojCekaca = 5;

  public GlavniPosluzitelj(Konfiguracija konfig) {
    this.konfig = konfig;

    status = new AtomicBoolean(false);
    ispis = new AtomicBoolean(false);
    brojacUdaljenosti = new AtomicInteger(0);
  }

  /**
   * Učitava konfiguracijske podatke te potom otvara mrežna vrata.
   */
  public void pokreniPosluzitelja() {

    try {
      mreznaVrata = Integer.parseInt(konfig.dajPostavku("mreznaVrata"));
      brojRadnika = Integer.parseInt(konfig.dajPostavku("brojRadnika"));
      brojCekaca = Integer.parseInt(konfig.dajPostavku("brojCekaca"));
    } catch (NumberFormatException e) {
      Logger.getGlobal().log(Level.SEVERE, "Postavke nisu ispravne! " + e.getMessage()
          + " Nastavljam rad sa zadanim vrijednostima...");
    }

    otvoriMreznaVrata();
  }

  /**
   * Otvara mrežna vrata te čeka na spajanje klijenata. Nakon spajanja klijenta daljnji se rad
   * prebacuje u dretvu MrezniRadnik koja obrađuje dolazne zahtjeve.
   * 
   * @see MrezniRadnik
   */
  private void otvoriMreznaVrata() {
    List<Thread> dretve = new ArrayList<>();

    try (var posluzitelj = new ServerSocket(mreznaVrata, brojCekaca)) {

      ExecutorService executor = Executors.newFixedThreadPool(brojRadnika);

      while (true) {
        Socket uticnica = null;
        try {
          uticnica = posluzitelj.accept();
        } catch (SocketException e) {
          Logger.getGlobal().log(Level.INFO, e.getMessage());
          break;
        }

        var mrezniRadnik = new MrezniRadnik(posluzitelj, uticnica, this.status, this.ispis,
            this.brojacUdaljenosti);
        Thread dretva = new Thread(mrezniRadnik);

        executor.execute(dretva);

        dretve.add(dretva);
      }

      posluzitelj.close();

      executor.shutdown();
      while (!executor.isTerminated());

      Logger.getGlobal().log(Level.INFO, "Glavni poslužitelj - kraj rada");

    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }

  }



}
