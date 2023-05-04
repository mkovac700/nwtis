package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class Info {
  @Getter
  @Setter
  private String autorIme;
  @Getter
  @Setter
  private String autorPrezime;
  @Getter
  @Setter
  private String autorPredmet;
  @Getter
  @Setter
  private String aplikacijaGodina;
  @Getter
  @Setter
  private String aplikacijaVerzija;

  public Info() {}
}
