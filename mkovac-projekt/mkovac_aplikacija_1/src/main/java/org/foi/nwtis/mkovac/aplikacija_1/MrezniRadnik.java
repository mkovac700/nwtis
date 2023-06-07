package org.foi.nwtis.mkovac.aplikacija_1;

import java.util.function.Consumer;

public class MrezniRadnik implements Runnable {
  private Consumer<Boolean> azurirajStatus;

  public MrezniRadnik(Consumer<Boolean> azurirajStatus) {
    this.azurirajStatus = azurirajStatus;
  }

  @Override
  public void run() {
    boolean status = true;
    azurirajStatus.accept(status);
  }

}
