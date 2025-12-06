package org.group_04_01.dataprocessing.datastructure.imlementation;

import org.group_04_01.dataprocessing.datastructure.Sitzung;
import org.group_04_01.dataprocessing.datastructure.Wahlperiode;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;

public class Sitzung_File_Impl extends Sitzung_Abstract_Impl implements Sitzung {

    /**
     * Erzeugt ein Objekt vom Typ Sitzung fuer das Einlesen aus einem File
     * 
     * @param date           das Datum der Sitzung
     * @param sitzungsbeginn die Uhrzeit, zu der die Sitzung begonnen hat
     * @param sitzungsende   die Uhrzeit, zu der die Sitzung beendet wurde
     * @param sitzungsnummer die Nummer der Sitzung
     */
    public Sitzung_File_Impl(Date date, Time sitzungsbeginn, Time sitzungsende, int sitzungsnummer,
            Wahlperiode wahlperiode) {
        this.date = date;
        this.sitzungsbeginn = sitzungsbeginn;
        this.sitzungsende = sitzungsende;
        this.sitzungsnummer = sitzungsnummer;
        this.wahlperiode = wahlperiode;
        tagesordnungspunkte = new HashSet<>();
    }

    /**
     * fügt der Sitzung einen Tagesordnungspunkt hinzu
     * 
     * @param tagesordnung der Tagesordnungspunkt, der hinzugefügt werden soll
     */
    public void addTagesordnungspunkt(Tagesordnung_File_Impl tagesordnung) {
        tagesordnungspunkte.add(tagesordnung);
    }
}
