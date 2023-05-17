package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class UdaljenostAerodromKlasa {
  @Getter
  @Setter
  private String icao;
  @Getter
  @Setter
  private float km;

  UdaljenostAerodromKlasa() {}
}
