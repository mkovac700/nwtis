package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class UdaljenostAerodrom {
  @Getter
  @Setter
  private String icao;
  @Getter
  @Setter
  private float km;

  public UdaljenostAerodrom() {}
}
