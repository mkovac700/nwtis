package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class Status {

  @Getter
  @Setter
  private int status;

  @Getter
  @Setter
  private String opis;

  public Status() {}

}
