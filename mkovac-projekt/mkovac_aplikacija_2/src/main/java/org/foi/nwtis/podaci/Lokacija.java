package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Marijan Kovač
 * @version 1.0.0
 */
@AllArgsConstructor()
public class Lokacija {

  @Getter
  @Setter
  private String latitude;
  @Getter
  @Setter
  private String longitude;

  public Lokacija() {}
}
