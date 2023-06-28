package org.foi.nwtis.mkovac.aplikacija_4.iznimke;

/**
 * Klasa iznimke za obradu pogreške autentikacije
 * 
 * @author Marijan Kovač
 *
 */
public class PogresnaAutentikacija extends Exception {

  private static final long serialVersionUID = -4360882513795045098L;

  public PogresnaAutentikacija() {
    super();
  }

  public PogresnaAutentikacija(String message) {
    super(message);
  }

}
