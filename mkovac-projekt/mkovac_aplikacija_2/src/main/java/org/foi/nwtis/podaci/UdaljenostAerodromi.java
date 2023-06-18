package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class UdaljenostAerodromi {
  @Getter
  @Setter
  private String icaoOd;
  @Getter
  @Setter
  private String icaoDo;
  @Getter
  @Setter
  private float km;

  public UdaljenostAerodromi() {}
}
