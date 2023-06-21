package org.foi.nwtis.mkovac.aplikacija_4.jpa;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;


/**
 * The persistent class for the KORISNICI database table.
 * 
 */
@Entity
@Table(name = "KORISNICI")
@NamedQuery(name = "Korisnici.findAll", query = "SELECT k FROM Korisnici k")
public class Korisnici implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private int id;

  @Column(unique = true, nullable = false)
  private String korisnik;

  @Column(nullable = false)
  private String lozinka;

  @Column(nullable = false)
  private String ime;

  @Column(nullable = false)
  private String prezime;

  public Korisnici() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getIme() {
    return this.ime;
  }

  public void setIme(String ime) {
    this.ime = ime;
  }

  public String getKorisnik() {
    return this.korisnik;
  }

  public void setKorisnik(String korisnik) {
    this.korisnik = korisnik;
  }

  public String getLozinka() {
    return this.lozinka;
  }

  public void setLozinka(String lozinka) {
    this.lozinka = lozinka;
  }

  public String getPrezime() {
    return this.prezime;
  }

  public void setPrezime(String prezime) {
    this.prezime = prezime;
  }

}
