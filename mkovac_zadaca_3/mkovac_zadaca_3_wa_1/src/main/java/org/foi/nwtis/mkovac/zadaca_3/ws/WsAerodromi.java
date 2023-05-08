package org.foi.nwtis.mkovac.zadaca_3.ws;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.UdaljenostKlasa;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "aerodromi")
public class WsAerodromi {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @WebMethod
  public List<Aerodrom> dajSveAerodrome(@WebParam int odBroja,
      @WebParam int broj) {
    List<Aerodrom> aerodromi = new ArrayList<>();
    Aerodrom ad = new Aerodrom("LDZA", "Airport Zagreb", "HR",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LDVA", "Airport Varaždin", "HR",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDF", "Airport Frankfurt", "DE",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDB", "Airport Berlin", "DE",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LOWW", "Airport Vienna", "AT",
        new Lokacija("0", "0"));
    aerodromi.add(ad);

    return aerodromi;
  }

  @WebMethod
  public Aerodrom dajAerodrom(@WebParam String icao) {
    List<Aerodrom> aerodromi = new ArrayList<>();
    Aerodrom ad = new Aerodrom("LDZA", "Airport Zagreb", "HR",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LDVA", "Airport Varaždin", "HR",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDF", "Airport Frankfurt", "DE",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("EDDB", "Airport Berlin", "DE",
        new Lokacija("0", "0"));
    aerodromi.add(ad);
    ad = new Aerodrom("LOWW", "Airport Vienna", "AT",
        new Lokacija("0", "0"));
    aerodromi.add(ad);

    Aerodrom aerodrom = null;

    for (Aerodrom a : aerodromi) {
      if (a.getIcao().compareTo(icao) == 0) {
        aerodrom = a;
        break;
      }
    }

    return aerodrom;
  }

  @WebMethod
  public List<UdaljenostKlasa> dajUdaljenostiAerodroma(
      @WebParam String icaoOd, @WebParam String icaoDo) {
    var udaljenosti = new ArrayList<UdaljenostKlasa>();
    var u1 = new UdaljenostKlasa("HR", 85.64f);
    var u2 = new UdaljenostKlasa("SI", 41.29f);
    var u3 = new UdaljenostKlasa("AT", 123.56f);
    udaljenosti.add(u1);
    udaljenosti.add(u2);
    udaljenosti.add(u3);
    return udaljenosti;
  }

}
