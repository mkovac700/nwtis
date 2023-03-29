package org.foi.nwtis.mkovac.zadaca_1.podaci;

import java.io.Serializable;

public record Udaljenost(String gpsSirina1, String gpsDuzina1, String gpsSirina2, String gpsDuzina2,
    String udaljenost) implements Serializable {

}
