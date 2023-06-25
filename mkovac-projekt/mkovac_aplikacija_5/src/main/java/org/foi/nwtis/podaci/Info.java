package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class Info {
  @Getter
  @Setter
  private String ime;
  @Getter
  @Setter
  private String prezime;
  @Getter
  @Setter
  private String predmet;
  @Getter
  @Setter
  private String godina;
  @Getter
  @Setter
  private String verzija;

  public Info() {}

}
