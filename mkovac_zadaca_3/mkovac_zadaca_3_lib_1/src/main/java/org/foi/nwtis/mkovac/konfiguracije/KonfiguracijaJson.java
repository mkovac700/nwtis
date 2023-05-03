package org.foi.nwtis.mkovac.konfiguracije;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Klasa konfiguracija za rad s postavkama konfiguracije u json formatu
 * 
 * @author Marijan Kovac
 *
 */
public class KonfiguracijaJson extends KonfiguracijaApstraktna {

  /**
   * Konstanta TIP
   */
  public static final String TIP = "json";

  /**
   * Konstruktor za inicijalizaciju KonfiguracijaJson
   * 
   * @param nazivDatoteke naziv datoteke
   */
  public KonfiguracijaJson(String nazivDatoteke) {
    super(nazivDatoteke);
  }

  @Override
  public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
    var putanja = Path.of(datoteka);
    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);

    if (tip == null || tip.compareTo(TIP) != 0) {
      throw new NeispravnaKonfiguracija("Datoteka '" + datoteka + "' nije tip " + TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isWritable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' je direktorij ili nije moguće pisati.");
    }

    try {
      //this.postavke.store(Files.newOutputStream(putanja), "NWTiS mkovac 2023.");
      
      JsonObject jsonObject = new JsonObject();
      for(String k : this.postavke.stringPropertyNames()) {
    	  String v = this.postavke.getProperty(k);
    	  jsonObject.addProperty(k, v);
      }
    	
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String json = gson.toJson(jsonObject); //this.postavke
      
      FileWriter fw = new FileWriter(putanja.toFile()); //putanja.tostring
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(json);
      bw.close();
     
      System.out.println("json: " + json);
      
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće upisivati. " + e.getMessage());
    }
  }

  @Override
  public void ucitajKonfiguraciju() throws NeispravnaKonfiguracija {
    var datoteka = this.nazivDatoteke;
    var putanja = Path.of(datoteka);
    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);

    if (tip == null || tip.compareTo(TIP) != 0) {
      throw new NeispravnaKonfiguracija("Datoteka '" + datoteka + "' nije tip " + TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isReadable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' je direktorij ili nije moguće čitati.");
    }

    try {
      //this.postavke.load(Files.newInputStream(putanja));
      
      FileReader fr = new FileReader(putanja.toFile());
      BufferedReader br = new BufferedReader(fr);
      
      Gson gson = new Gson();
      JsonObject jsonObject = gson.fromJson(br, JsonObject.class);
      
      for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
    	  String k = entry.getKey();
    	  String v = entry.getValue().getAsString();
    	  this.postavke.setProperty(k, v);
      }
      
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće čitati. " + e.getMessage());
    }

  }

}
