package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class AerodromLetovi {
  @Getter
  @Setter
  Aerodrom aerodrom;
  @Getter
  @Setter
  boolean preuzimanje;

  public AerodromLetovi() {}

}
