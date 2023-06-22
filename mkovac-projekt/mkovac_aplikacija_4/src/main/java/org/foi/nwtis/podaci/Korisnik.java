package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class Korisnik {

  @Getter
  @Setter
  private String korisnik;
  @Getter
  @Setter
  private String lozinka;
  @Getter
  @Setter
  private String ime;
  @Getter
  @Setter
  private String prezime;

  public Korisnik() {}

}
