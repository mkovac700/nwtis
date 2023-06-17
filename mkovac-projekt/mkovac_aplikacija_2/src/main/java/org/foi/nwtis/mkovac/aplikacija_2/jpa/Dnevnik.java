package org.foi.nwtis.mkovac.aplikacija_2.jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;


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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  private int id;

  @Column(nullable = false)
  private Timestamp spremljeno;

  @Column(nullable = false, length = 10)
  private String vrsta;

  @Column(nullable = false, length = 255)
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
