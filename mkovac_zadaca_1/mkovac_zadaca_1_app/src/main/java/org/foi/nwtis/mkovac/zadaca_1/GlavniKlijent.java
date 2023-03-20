package org.foi.nwtis.mkovac.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlavniKlijent {

  public static void main(String[] args) {
    var gk = new GlavniKlijent();
    if (!gk.provjeriArgumente(args)) {
      Logger.getGlobal().log(Level.SEVERE, "Nisu ispravni ulazni argumenti!");
      return;
    }
    String posluzitelj = args[0];
    int mreznaVrata = Integer.parseInt(args[1]);
    gk.spojiSeNaPosluzitelj(posluzitelj, mreznaVrata);


  }

  private boolean provjeriArgumente(String[] args) {
    // TODO ovo nije isrpavno, treba prema opisu zaadace
    return args.length == 2 ? true : false;
  }

  private void spojiSeNaPosluzitelj(String adresa, int mreznaVrata) {

    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);

      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String zahtjev = "TEST"; // TODO ovdje treba stvarni zahtjev prema opisu

      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();

      // var poruka i while true
      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;

        poruka.append("RED: " + red);
      }

      Logger.getGlobal().log(Level.INFO, "Odgovor:" + poruka);
      mreznaUticnica.shutdownInput();// prebacujemo se s primanja na slanje
      mreznaUticnica.close(); // zatvara konekciju klijent-posluzitelj
      // dretva zavrsava

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
