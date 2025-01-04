package org.foi.nwtis.rest.klijenti;

import jakarta.json.Json;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import java.io.StringReader;
import java.util.Date;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import org.foi.nwtis.rest.podaci.MeteoPodaciOriginal;

public class OWMKlijent {
   String apiKey;
   OWMRESTHelper helper;
   Client client;

   public OWMKlijent(String apiKey) {
      this.apiKey = apiKey.trim();
      this.helper = new OWMRESTHelper(apiKey);
      this.client = ClientBuilder.newClient();
   }

   public MeteoPodaci getRealTimeWeather(String latitude, String longitude) throws NwtisRestIznimka {
      latitude = latitude.trim();
      longitude = longitude.trim();
      WebTarget webResource = this.client.target(OWMRESTHelper.getOWM_BASE_URI()).path(OWMRESTHelper.getOWM_Current_Path());
      webResource = webResource.queryParam("lat", new Object[]{latitude});
      webResource = webResource.queryParam("lon", new Object[]{longitude});
      webResource = webResource.queryParam("lang", new Object[]{"hr"});
      webResource = webResource.queryParam("units", new Object[]{"metric"});
      webResource = webResource.queryParam("appid", new Object[]{this.apiKey});
      String odgovor = (String)webResource.request(new String[]{"application/json"}).get(String.class);

      try {
         JsonReader reader = Json.createReader(new StringReader(odgovor));
         JsonObject jo = reader.readObject();
         MeteoPodaci mp = new MeteoPodaci();
         mp.setSunRise(new Date(jo.getJsonObject("sys").getJsonNumber("sunrise").bigDecimalValue().longValue() * 1000L));
         mp.setSunSet(new Date(jo.getJsonObject("sys").getJsonNumber("sunset").bigDecimalValue().longValue() * 1000L));
         mp.setTemperatureValue((float)jo.getJsonObject("main").getJsonNumber("temp").doubleValue());
         mp.setTemperatureMin((float)jo.getJsonObject("main").getJsonNumber("temp_min").doubleValue());
         mp.setTemperatureMax((float)jo.getJsonObject("main").getJsonNumber("temp_max").doubleValue());
         mp.setTemperatureUnit("celsius");
         mp.setHumidityValue((float)jo.getJsonObject("main").getJsonNumber("humidity").doubleValue());
         mp.setHumidityUnit("%");
         mp.setPressureValue((float)jo.getJsonObject("main").getJsonNumber("pressure").doubleValue());
         mp.setPressureUnit("hPa");
         JsonNumber wind_deg;
         if (jo.isNull("wind")) {
            wind_deg = jo.getJsonObject("wind").getJsonNumber("speed");
            if (wind_deg != null) {
               mp.setWindSpeedValue((float)jo.getJsonObject("wind").getJsonNumber("speed").doubleValue());
            } else {
               mp.setWindSpeedValue((Float)null);
            }
         } else {
            mp.setWindSpeedValue((Float)null);
         }

         mp.setWindSpeedName("");
         if (jo.isNull("wind")) {
            wind_deg = jo.getJsonObject("wind").getJsonNumber("deg");
            if (wind_deg != null) {
               mp.setWindDirectionValue((float)jo.getJsonObject("wind").getJsonNumber("deg").doubleValue());
            } else {
               mp.setWindDirectionValue((Float)null);
            }
         } else {
            mp.setWindDirectionValue((Float)null);
         }

         mp.setWindDirectionCode("");
         mp.setWindDirectionName("");
         mp.setCloudsValue(jo.getJsonObject("clouds").getInt("all"));
         mp.setCloudsName(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
         mp.setPrecipitationMode("");
         mp.setWeatherNumber(jo.getJsonArray("weather").getJsonObject(0).getInt("id"));
         mp.setWeatherValue(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
         mp.setWeatherIcon(jo.getJsonArray("weather").getJsonObject(0).getString("icon"));
         mp.setLastUpdate(new Date(jo.getJsonNumber("dt").bigDecimalValue().longValue() * 1000L));
         return mp;
      } catch (Exception var9) {
         StringBuilder tekst = new StringBuilder();
         tekst.append("Nema podataka za lokaciju: ").append("lat: ").append(latitude).append(" lon: ").append(longitude);
         throw new NwtisRestIznimka(tekst.toString());
      }
   }

   public MeteoPodaciOriginal getRealTimeWeatherOriginal(String latitude, String longitude) throws NwtisRestIznimka {
      latitude = latitude.trim();
      longitude = longitude.trim();
      WebTarget webResource = this.client.target(OWMRESTHelper.getOWM_BASE_URI()).path(OWMRESTHelper.getOWM_Current_Path());
      webResource = webResource.queryParam("lat", new Object[]{latitude});
      webResource = webResource.queryParam("lon", new Object[]{longitude});
      webResource = webResource.queryParam("lang", new Object[]{"hr"});
      webResource = webResource.queryParam("units", new Object[]{"metric"});
      webResource = webResource.queryParam("appid", new Object[]{this.apiKey});
      String odgovor = (String)webResource.request(new String[]{"application/json"}).get(String.class);

      try {
         JsonReader reader = Json.createReader(new StringReader(odgovor));
         JsonObject jo = reader.readObject();
         MeteoPodaciOriginal mpo = new MeteoPodaciOriginal();
         mpo.setDt(jo.getJsonNumber("dt").bigDecimalValue().longValue());
         mpo.setCoordLon(Double.toString(jo.getJsonObject("coord").getJsonNumber("lon").doubleValue()));
         mpo.setCoordLat(Double.toString(jo.getJsonObject("coord").getJsonNumber("lat").doubleValue()));
         mpo.setWeatherId(jo.getJsonArray("weather").getJsonObject(0).getInt("id"));
         mpo.setWeatherMain(jo.getJsonArray("weather").getJsonObject(0).getString("main"));
         mpo.setWeatherDescription(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
         mpo.setWeatherDescription(jo.getJsonArray("weather").getJsonObject(0).getString("icon"));
         mpo.setBase(jo.getString("base"));
         mpo.setMainTemp((float)jo.getJsonObject("main").getJsonNumber("temp").doubleValue());
         mpo.setMainFeels_like((float)jo.getJsonObject("main").getJsonNumber("feels_like").doubleValue());
         mpo.setMainPressure((float)jo.getJsonObject("main").getJsonNumber("pressure").doubleValue());
         mpo.setMainHumidity(jo.getJsonObject("main").getJsonNumber("humidity").intValue());
         mpo.setMainTemp_min((float)jo.getJsonObject("main").getJsonNumber("temp_min").doubleValue());
         mpo.setMainTemp_max((float)jo.getJsonObject("main").getJsonNumber("temp_max").doubleValue());
         if (jo.getJsonObject("main").getJsonNumber("sea_level") != null) {
            mpo.setMainSea_level((float)jo.getJsonObject("main").getJsonNumber("sea_level").doubleValue());
         } else {
            mpo.setMainSea_level((Float)null);
         }

         if (jo.getJsonObject("main").getJsonNumber("grnd_level") != null) {
            mpo.setMainGrnd_level((float)jo.getJsonObject("main").getJsonNumber("grnd_level").doubleValue());
         } else {
            mpo.setMainGrnd_level((Float)null);
         }

         mpo.setVisibility(jo.getInt("visibility"));
         JsonNumber snow1h;
         JsonNumber snow3h;
         if (jo.getJsonObject("wind") != null) {
            snow1h = jo.getJsonObject("wind").getJsonNumber("speed");
            if (snow1h != null) {
               mpo.setWindSpeed((float)snow1h.doubleValue());
            } else {
               mpo.setWindSpeed((Float)null);
            }

            snow3h = jo.getJsonObject("wind").getJsonNumber("deg");
            if (snow3h != null) {
               mpo.setWindDeg(snow3h.intValue());
            } else {
               mpo.setWindDeg((Integer)null);
            }

            JsonNumber windGust = jo.getJsonObject("wind").getJsonNumber("gust");
            if (windGust != null) {
               mpo.setWindGust((float)windGust.doubleValue());
            } else {
               mpo.setWindGust((Float)null);
            }
         } else {
            mpo.setWindSpeed((Float)null);
            mpo.setWindDeg((Integer)null);
            mpo.setWindGust((Float)null);
         }

         mpo.setCloudsAll(jo.getJsonObject("clouds").getInt("all"));
         if (jo.getJsonObject("rain") != null) {
            snow1h = jo.getJsonObject("rain").getJsonNumber("1h");
            if (snow1h != null) {
               mpo.setRain1h((float)snow1h.doubleValue());
            } else {
               mpo.setRain1h((Float)null);
            }

            snow3h = jo.getJsonObject("rain").getJsonNumber("3h");
            if (snow3h != null) {
               mpo.setRain3h((float)snow3h.doubleValue());
            } else {
               mpo.setRain3h((Float)null);
            }
         } else {
            mpo.setRain1h((Float)null);
            mpo.setRain3h((Float)null);
         }

         if (jo.getJsonObject("snow") != null) {
            snow1h = jo.getJsonObject("snow").getJsonNumber("1h");
            if (snow1h != null) {
               mpo.setSnow1h((float)snow1h.doubleValue());
            } else {
               mpo.setSnow1h((Float)null);
            }

            snow3h = jo.getJsonObject("snow").getJsonNumber("3h");
            if (snow3h != null) {
               mpo.setSnow3h((float)snow3h.doubleValue());
            } else {
               mpo.setSnow3h((Float)null);
            }
         } else {
            mpo.setSnow1h((Float)null);
            mpo.setSnow3h((Float)null);
         }

         if (jo.getJsonObject("sys").getJsonNumber("type") != null) {
            mpo.setSysType(jo.getJsonObject("sys").getInt("type"));
         } else {
            mpo.setSysType((Integer)null);
         }

         if (jo.getJsonObject("sys").getJsonNumber("id") != null) {
            mpo.setSysId(jo.getJsonObject("sys").getInt("id"));
         } else {
            mpo.setSysId((Integer)null);
         }

         if (jo.getJsonObject("sys").getJsonNumber("message") != null) {
            mpo.setSysMessage((float)jo.getJsonObject("sys").getJsonNumber("message").doubleValue());
         } else {
            mpo.setSysMessage((Float)null);
         }

         mpo.setSysCountry(jo.getJsonObject("sys").getString("country"));
         mpo.setSysSunrise(jo.getJsonObject("sys").getInt("sunrise"));
         mpo.setSysSunset(jo.getJsonObject("sys").getInt("sunset"));
         mpo.setTimezone(jo.getInt("timezone"));
         mpo.setCityId(jo.getInt("id"));
         mpo.setCityName(jo.getString("name"));
         mpo.setCod(jo.getInt("cod"));
         mpo.setJsonMeteo(odgovor);
         return mpo;
      } catch (Exception var11) {
         StringBuilder tekst = new StringBuilder();
         tekst.append("Nema podataka za lokaciju: ").append("lat: ").append(latitude).append(" lon: ").append(longitude);
         throw new NwtisRestIznimka(tekst.toString());
      }
   }
}
