package org.foi.nwtis.mkovac.vjezba_02;

import java.util.Properties;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

public class Vjezba_02_4 {

	public Vjezba_02_4() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		if (args.length != 2)
			System.out.println("Program koristi 2 argumenta: <naziv_datoteke1> <naziv_datoteke2>");
		else {
			Konfiguracija konf1 = null; //in
			Konfiguracija konf2 = null; //out
			try {
				System.out.println("Učitavam postavke iz datoteke " + args[0] + "...");
				konf1 = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
				System.out.println("Zapisujem postavke u datoteku " + args[1] + "...");
				konf2 = KonfiguracijaApstraktna.kreirajKonfiguraciju(args[1]);
				
				Properties postavke = konf1.dajSvePostavke();
				for (Object o : postavke.keySet()) {
					String k = (String) o;
					String v = konf1.dajPostavku(k);
					konf2.spremiPostavku(k, v);
				}
				konf2.spremiKonfiguraciju();
				
				System.out.println("Postavke uspješno zapisane!");
				
			} catch (NeispravnaKonfiguracija e) {
				e.printStackTrace();
			}
		}
	}

}
