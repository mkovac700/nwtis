package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Marijan Kovač
 * @version 1.0.0
 */
@AllArgsConstructor()
public class Udaljenost {
  @Getter
  @Setter
  private String drzava;
  @Getter
  @Setter
  private float km;

  public Udaljenost() {}
}
