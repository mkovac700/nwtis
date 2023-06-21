package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the AIRPORTS database table.
 * 
 */
@Entity
@Table(name="AIRPORTS")
@NamedQuery(name="Airport.findAll", query="SELECT a FROM Airport a")
public class Airport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ICAO")
	private String icao;

	@Column(name="CONTINENT")
	private String continent;

	@Column(name="COORDINATES")
	private String coordinates;

	@Column(name="ELEVATION_FT")
	private String elevationFt;

	@Column(name="GPS_CODE")
	private String gpsCode;

	@Column(name="IATA_CODE")
	private String iataCode;

	@Column(name="ISO_COUNTRY")
	private String isoCountry;

	@Column(name="ISO_REGION")
	private String isoRegion;

	@Column(name="LOCAL_CODE")
	private String localCode;

	@Column(name="MUNICIPALITY")
	private String municipality;

	@Column(name="NAME")
	private String name;

	@Column(name="TYPE")
	private String type;

	//bi-directional many-to-one association to AerodromiLetovi
	@OneToMany(mappedBy="airport")
	private List<AerodromiLetovi> aerodromiLetovis;

	public Airport() {
	}

	public String getIcao() {
		return this.icao;
	}

	public void setIcao(String icao) {
		this.icao = icao;
	}

	public String getContinent() {
		return this.continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getElevationFt() {
		return this.elevationFt;
	}

	public void setElevationFt(String elevationFt) {
		this.elevationFt = elevationFt;
	}

	public String getGpsCode() {
		return this.gpsCode;
	}

	public void setGpsCode(String gpsCode) {
		this.gpsCode = gpsCode;
	}

	public String getIataCode() {
		return this.iataCode;
	}

	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}

	public String getIsoCountry() {
		return this.isoCountry;
	}

	public void setIsoCountry(String isoCountry) {
		this.isoCountry = isoCountry;
	}

	public String getIsoRegion() {
		return this.isoRegion;
	}

	public void setIsoRegion(String isoRegion) {
		this.isoRegion = isoRegion;
	}

	public String getLocalCode() {
		return this.localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	public String getMunicipality() {
		return this.municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<AerodromiLetovi> getAerodromiLetovis() {
		return this.aerodromiLetovis;
	}

	public void setAerodromiLetovis(List<AerodromiLetovi> aerodromiLetovis) {
		this.aerodromiLetovis = aerodromiLetovis;
	}

	public AerodromiLetovi addAerodromiLetovi(AerodromiLetovi aerodromiLetovi) {
		getAerodromiLetovis().add(aerodromiLetovi);
		aerodromiLetovi.setAirport(this);

		return aerodromiLetovi;
	}

	public AerodromiLetovi removeAerodromiLetovi(AerodromiLetovi aerodromiLetovi) {
		getAerodromiLetovis().remove(aerodromiLetovi);
		aerodromiLetovi.setAirport(null);

		return aerodromiLetovi;
	}

}