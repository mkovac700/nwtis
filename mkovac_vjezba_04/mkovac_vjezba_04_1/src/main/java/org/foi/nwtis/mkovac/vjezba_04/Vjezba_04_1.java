package org.foi.nwtis.mkovac.vjezba_04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.vjezba_04.podaci.SystemInfo;
import com.google.gson.Gson;

public class Vjezba_04_1 {

  SystemInfo systemInfo;

  public static void main(String[] args) {
    var vjezba = new Vjezba_04_1();
    if (!vjezba.provjeriArgumente(args)) {
      Logger.getGlobal().log(Level.SEVERE, "Mora biti upisan jedan argument!");
      return;
    }

    vjezba.systemInfo = new SystemInfo();
    vjezba.ispisSystemInfo();
    try {
      vjezba.spremiSystemInfo(args[0]);
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }

  }

  private boolean provjeriArgumente(String[] args) {
    return (args.length == 1) ? true : false;
  }

  private void ispisSystemInfo() {
    // lombok automatski dodaje gettere i settere bez obzira sto nigdje nisu postavljeni
    Logger.getGlobal().log(Level.INFO, "OS: " + this.systemInfo.getNazivOS());
    Logger.getGlobal().log(Level.INFO, "Vendor: " + this.systemInfo.getProizvodacVM());
    Logger.getGlobal().log(Level.INFO, "Version: " + this.systemInfo.getVerzijaVM());
    Logger.getGlobal().log(Level.INFO, "VM dir: " + this.systemInfo.getDirektorijVM());
    Logger.getGlobal().log(Level.INFO, "Temp dir: " + this.systemInfo.getDirektorijTemp());
    Logger.getGlobal().log(Level.INFO, "User dir: " + this.systemInfo.getDirektorijKorisnik());

  }

  private boolean spremiSystemInfo(String nazivDatoteke) throws IOException {
    var datoteka = Path.of(nazivDatoteke);
    if (Files.exists(datoteka) && (!Files.isRegularFile(datoteka) || !Files.isWritable(datoteka))) {
      throw new IOException("Datoteka: " + nazivDatoteke + " ne može se upisati u nju!");
    } else {
      var gson = new Gson();
      var json = gson.toJson(this.systemInfo);
      Logger.getGlobal().log(Level.INFO, json);
      var pisac = Files.newBufferedWriter(datoteka);
      pisac.write(json);
      pisac.close();
      return true;
    }
  }

}
