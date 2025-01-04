package org.foi.nwtis.rest.klijenti;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.foi.nwtis.rest.podaci.LetAviona;
import org.foi.nwtis.rest.podaci.LetAvionaPozicije;
import org.foi.nwtis.rest.podaci.LetPozicija;

public class OSKlijent {
   private static final String BASE_PROTOCOL = "https://";
   private static final String BASE_URI = "opensky-network.org/";
   private final Client client = ClientBuilder.newClient();
   private final String username;
   private final String password;

   public OSKlijent(String username, String password) {
      this.username = username;
      this.password = password;
   }

   public List<LetAviona> getDepartures(String aerodrom, long odVremena, long doVremena) throws NwtisRestIznimka {
      List<LetAviona> list = new ArrayList();
      Response restOdgovor = null;

      StringBuilder tekst;
      try {
         WebTarget webResource = this.client.target("https://" + this.username + ":" + this.password + "@" + "opensky-network.org/" + "api/flights/departure" + "?airport=" + aerodrom + "&begin=" + odVremena + "&end=" + doVremena);
         restOdgovor = webResource.request().get();
         if (restOdgovor.getStatus() == 200) {
            String odgovor = (String)restOdgovor.readEntity(String.class);
            Gson gson = new Gson();
            LetAviona[] avioniLete = (LetAviona[])gson.fromJson(odgovor, LetAviona[].class);
            list.addAll(Arrays.asList(avioniLete));
            return list;
         } else {
            tekst = new StringBuilder();
            tekst.append("Nema podataka za aerodrom: ").append(aerodrom).append(" Status: ").append(restOdgovor.getStatus()).append(" - ").append(restOdgovor.getStatusInfo());
            throw new NwtisRestIznimka(tekst.toString());
         }
      } catch (NwtisRestIznimka var12) {
         throw new NwtisRestIznimka(var12.getMessage());
      } catch (Exception var13) {
         tekst = new StringBuilder();
         tekst.append("Nema podataka za aerodrom: ").append(aerodrom).append(" ").append(var13.getMessage());
         if (restOdgovor != null) {
            tekst.append(" Status: ").append(restOdgovor.getStatus()).append(" - ").append(restOdgovor.getStatusInfo());
         }

         throw new NwtisRestIznimka(tekst.toString());
      }
   }

   public List<LetAviona> getDepartures(String aerodrom, Timestamp odVremena, Timestamp doVremena) throws NwtisRestIznimka {
      return this.getDepartures(aerodrom, odVremena.getTime() / 1000L, doVremena.getTime() / 1000L);
   }

   public List<LetAviona> getArrivals(String aerodrom, long odVremena, long doVremena) throws NwtisRestIznimka {
      List<LetAviona> list = new ArrayList();
      Response restOdgovor = null;

      StringBuilder tekst;
      try {
         WebTarget webResource = this.client.target("https://" + this.username + ":" + this.password + "@" + "opensky-network.org/" + "api/flights/arrival" + "?airport=" + aerodrom + "&begin=" + odVremena + "&end=" + doVremena);
         restOdgovor = webResource.request().get();
         if (restOdgovor.getStatus() == 200) {
            String odgovor = (String)restOdgovor.readEntity(String.class);
            Gson gson = new Gson();
            LetAviona[] avioniLete = (LetAviona[])gson.fromJson(odgovor, LetAviona[].class);
            list.addAll(Arrays.asList(avioniLete));
            return list;
         } else {
            tekst = new StringBuilder();
            tekst.append("Nema podataka za aerodrom: ").append(aerodrom).append(" Status: ").append(restOdgovor.getStatus()).append(" - ").append(restOdgovor.getStatusInfo());
            throw new NwtisRestIznimka(tekst.toString());
         }
      } catch (NwtisRestIznimka var12) {
         throw new NwtisRestIznimka(var12.getMessage());
      } catch (Exception var13) {
         tekst = new StringBuilder();
         tekst.append("Nema podataka za aerodrom: ").append(aerodrom).append(" ").append(var13.getMessage());
         if (restOdgovor != null) {
            tekst.append(" Status: ").append(restOdgovor.getStatus()).append(" - ").append(restOdgovor.getStatusInfo());
         }

         throw new NwtisRestIznimka(tekst.toString());
      }
   }

   public List<LetAviona> getArrivals(String aerodrom, Timestamp odVremena, Timestamp doVremena) throws NwtisRestIznimka {
      return this.getArrivals(aerodrom, odVremena.getTime() / 1000L, doVremena.getTime() / 1000L);
   }

   public List<LetAviona> getFlights(String avion, long odVremena, long doVremena) throws NwtisRestIznimka {
      List<LetAviona> list = new ArrayList();
      Response restOdgovor = null;

      StringBuilder tekst;
      try {
         WebTarget webResource = this.client.target("https://" + this.username + ":" + this.password + "@" + "opensky-network.org/" + "api/flights/aircraft" + "?icao24=" + avion + "&begin=" + odVremena + "&end=" + doVremena);
         restOdgovor = webResource.request().get();
         if (restOdgovor.getStatus() == 200) {
            String odgovor = (String)restOdgovor.readEntity(String.class);
            Gson gson = new Gson();
            LetAviona[] avioniLete = (LetAviona[])gson.fromJson(odgovor, LetAviona[].class);
            list.addAll(Arrays.asList(avioniLete));
            return list;
         } else {
            tekst = new StringBuilder();
            tekst.append("Nema podataka za avion: ").append(avion).append(" Status: ").append(restOdgovor.getStatus()).append(" - ").append(restOdgovor.getStatusInfo());
            throw new NwtisRestIznimka(tekst.toString());
         }
      } catch (NwtisRestIznimka var12) {
         throw new NwtisRestIznimka(var12.getMessage());
      } catch (Exception var13) {
         tekst = new StringBuilder();
         tekst.append("Nema podataka za avion: ").append(avion).append(" ").append(var13.getMessage());
         if (restOdgovor != null) {
            tekst.append(" Status: ").append(restOdgovor.getStatus()).append(" - ").append(restOdgovor.getStatusInfo());
         }

         throw new NwtisRestIznimka(tekst.toString());
      }
   }

   public List<LetAviona> getFlights(String avion, Timestamp odVremena, Timestamp doVremena) throws NwtisRestIznimka {
      return this.getFlights(avion, odVremena.getTime() / 1000L, doVremena.getTime() / 1000L);
   }

   public LetAvionaPozicije getTracks(String avion, long zaVrijeme) throws NwtisRestIznimka {
      LetAvionaPozicije avionLetiPozicije = new LetAvionaPozicije();
      Response restOdgovor = null;

      StringBuilder tekst;
      try {
         WebTarget webResource = this.client.target("https://" + this.username + ":" + this.password + "@" + "opensky-network.org/" + "api/tracks/all" + "?icao24=" + avion + "&time=" + zaVrijeme);
         restOdgovor = webResource.request().get();
         if (restOdgovor.getStatus() != 200) {
            tekst = new StringBuilder();
            tekst.append("Nema podataka za avion: ").append(avion).append(" Status: ").append(restOdgovor.getStatus()).append(" - ").append(restOdgovor.getStatusInfo());
            throw new NwtisRestIznimka(tekst.toString());
         } else {
            String odgovor = (String)restOdgovor.readEntity(String.class);
            if (odgovor != null && !odgovor.trim().isEmpty()) {
               Gson gson = new Gson();
               JsonObject jo = (JsonObject)gson.fromJson(odgovor, JsonObject.class);
               avionLetiPozicije.setIcao24(jo.get("icao24").getAsString());
               avionLetiPozicije.setStartTime(jo.get("startTime").getAsNumber().intValue());
               avionLetiPozicije.setEndTime(jo.get("endTime").getAsNumber().intValue());
               avionLetiPozicije.setCallsign(jo.get("callsign").getAsString());
               List<LetPozicija> pozicije = new ArrayList();
               JsonArray ja1 = jo.get("path").getAsJsonArray();
               Iterator var13 = ja1.iterator();

               while(var13.hasNext()) {
                  JsonElement je1 = (JsonElement)var13.next();
                  JsonArray ja2 = je1.getAsJsonArray();
                  String[] niz = new String[6];
                  int i = 0;

                  JsonElement je2;
                  for(Iterator var18 = ja2.iterator(); var18.hasNext(); niz[i++] = je2.getAsString()) {
                     je2 = (JsonElement)var18.next();
                  }

                  LetPozicija lp = new LetPozicija(Integer.parseInt(niz[0]), Float.parseFloat(niz[1]), Float.parseFloat(niz[2]), Float.parseFloat(niz[3]), Float.parseFloat(niz[4]), Boolean.parseBoolean(niz[5]));
                  pozicije.add(lp);
               }

               avionLetiPozicije.setPath(pozicije);
               return avionLetiPozicije;
            } else {
               return null;
            }
         }
      } catch (NwtisRestIznimka var19) {
         throw new NwtisRestIznimka(var19.getMessage());
      } catch (Exception var20) {
         tekst = new StringBuilder();
         tekst.append("Nema podataka za avion: ").append(avion).append(" ").append(var20.getMessage());
         if (restOdgovor != null) {
            tekst.append(" Status: ").append(restOdgovor.getStatus()).append(" - ").append(restOdgovor.getStatusInfo());
         }

         throw new NwtisRestIznimka(tekst.toString());
      }
   }

   public LetAvionaPozicije getTracks(String avion, Timestamp zaVrijeme) throws NwtisRestIznimka {
      return this.getTracks(avion, zaVrijeme.getTime() / 1000L);
   }
}
