package org.foi.nwtis.podaci;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Marijan Kovač
 * @version 1.0.0
 */
@AllArgsConstructor()
public class Dnevnik {
  @Getter
  @Setter
  private int id;
  @Getter
  @Setter
  private Timestamp spremljeno;
  @Getter
  @Setter
  private String vrsta;
  @Getter
  @Setter
  private String zahtjev;

  public Dnevnik() {}
}
