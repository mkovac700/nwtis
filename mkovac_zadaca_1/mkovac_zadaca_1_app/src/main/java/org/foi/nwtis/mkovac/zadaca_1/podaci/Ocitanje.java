package org.foi.nwtis.mkovac.zadaca_1.podaci;

public record Ocitanje(String id, String vrijeme, String temp, String vlaga, String tlak,
    boolean alarmTemp, boolean alarmVlaga, boolean alarmTlak, boolean alarm) {

}
