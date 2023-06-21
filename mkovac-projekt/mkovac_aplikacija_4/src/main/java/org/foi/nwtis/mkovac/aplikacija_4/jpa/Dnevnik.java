package org.foi.nwtis.mkovac.aplikacija_4.jpa;

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
  @Column(name = "ID", unique = true, nullable = false)
  private int id;

  @Column(name = "VREMENSKA_OZNAKA", nullable = false)
  private Timestamp vremenskaOznaka;

  @Column(name = "VRSTA", nullable = false, length = 10)
  private String vrsta;

  @Column(name = "METODA", nullable = false, length = 10)
  private String metoda;

  @Column(name = "ZAHTJEV", nullable = false, length = 255)
  private String zahtjev;

  public Dnevnik() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Timestamp getVremenskaOznaka() {
    return this.vremenskaOznaka;
  }

  public void setVremenskaOznaka(Timestamp vremenskaOznaka) {
    this.vremenskaOznaka = vremenskaOznaka;
  }

  public String getVrsta() {
    return this.vrsta;
  }

  public void setVrsta(String vrsta) {
    this.vrsta = vrsta;
  }

  public String getMetoda() {
    return this.metoda;
  }

  public void setMetoda(String metoda) {
    this.metoda = metoda;
  }

  public String getZahtjev() {
    return this.zahtjev;
  }

  public void setZahtjev(String zahtjev) {
    this.zahtjev = zahtjev;
  }

}
