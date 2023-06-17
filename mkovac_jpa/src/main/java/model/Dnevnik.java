package model;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * The persistent class for the DNEVNIK database table.
 * 
 */
@Entity
@Table(name = "DNEVNIK")
@NamedQuery(name = "Dnevnik.findAll", query = "SELECT d FROM Dnevnik d")
public class Dnevnik implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private int id;

  private Timestamp spremljeno;

  private String vrsta;

  private String zahtjev;

  public Dnevnik() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Timestamp getSpremljeno() {
    return this.spremljeno;
  }

  public void setSpremljeno(Timestamp spremljeno) {
    this.spremljeno = spremljeno;
  }

  public String getVrsta() {
    return this.vrsta;
  }

  public void setVrsta(String vrsta) {
    this.vrsta = vrsta;
  }

  public String getZahtjev() {
    return this.zahtjev;
  }

  public void setZahtjev(String zahtjev) {
    this.zahtjev = zahtjev;
  }

}
