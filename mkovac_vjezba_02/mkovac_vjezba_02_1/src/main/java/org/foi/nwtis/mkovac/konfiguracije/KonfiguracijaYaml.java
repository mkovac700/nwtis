package org.foi.nwtis.mkovac.konfiguracije;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

/**
 * Klasa konfiguracija za rad s postavkama konfiguracije u yaml formatu
 * 
 * @author Marijan Kovac
 *
 */
public class KonfiguracijaYaml extends KonfiguracijaApstraktna {

  /**
   * Konstanta TIP
   */
  public static final String TIP = "yaml";

  /**
   * Konstruktor za inicijalizaciju KonfiguracijaYaml
   * 
   * @param nazivDatoteke naziv datoteke
   */
  public KonfiguracijaYaml(String nazivDatoteke) {
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
      
      Map<Object, Object> map = new LinkedHashMap<>();
      for (String key : this.postavke.stringPropertyNames()) {
          String value = this.postavke.getProperty(key);
          map.put(key, value);
      }
      
      Dump dump = new Dump(DumpSettings.builder().build());
      
      String yaml = dump.dumpToString(map);
      
      FileWriter fw = new FileWriter(putanja.toFile()); //putanja.tostring
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(yaml);
      bw.close();
      
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
      
      Load load = new Load(LoadSettings.builder().build());
      
      FileReader fr = new FileReader(putanja.toFile());
      BufferedReader br = new BufferedReader(fr);
      
      String str = br.lines().collect(Collectors.joining("\n"));
      
      Map<?,?> map = (Map<?,?>) load.loadFromString(str);
      
      for(Object o : map.keySet()) {
    	  String k = (String) o;
    	  String v = map.get(k).toString();
    	  this.postavke.setProperty(k, v);
      }
      
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće čitati. " + e.getMessage());
    }

  }

}
