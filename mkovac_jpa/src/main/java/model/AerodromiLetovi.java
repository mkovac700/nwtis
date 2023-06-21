package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the AERODROMI_LETOVI database table.
 * 
 */
@Entity
@Table(name="AERODROMI_LETOVI")
@NamedQuery(name="AerodromiLetovi.findAll", query="SELECT a FROM AerodromiLetovi a")
public class AerodromiLetovi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private byte preuzimanje;

	//bi-directional many-to-one association to Airport
	@ManyToOne
	@JoinColumn(name="icao")
	private Airport airport;

	public AerodromiLetovi() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getPreuzimanje() {
		return this.preuzimanje;
	}

	public void setPreuzimanje(byte preuzimanje) {
		this.preuzimanje = preuzimanje;
	}

	public Airport getAirport() {
		return this.airport;
	}

	public void setAirport(Airport airport) {
		this.airport = airport;
	}

}