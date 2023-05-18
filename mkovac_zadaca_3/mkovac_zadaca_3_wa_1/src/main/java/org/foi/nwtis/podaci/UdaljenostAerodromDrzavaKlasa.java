package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class UdaljenostAerodromDrzavaKlasa {
  @Getter
  @Setter
  private String icao;
  @Getter
  @Setter
  private String drzava;
  @Getter
  @Setter
  private float km;

  public UdaljenostAerodromDrzavaKlasa() {}

}
