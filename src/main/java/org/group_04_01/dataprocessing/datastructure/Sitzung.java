package org.group_04_01.dataprocessing.datastructure;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

/**
 * Interface für Sitzungen des Bundestages.
 */
public interface Sitzung extends Comparable<Sitzung> {

    /**
     * Gibt das Datum, an dem die Sitzung stattfand, zurück.
     * 
     * @return ein sql.Date-Objekt, das das Datum der Sitzung repräsentiert.
     */
    Date getDate();

    /**
     * Gibt die Uhrzeit zurück, zu der die Sitzung begann.
     * 
     * @return ein sql.Time-Objekt, das die Uhrzeit repräsentiert.
     */
    Time getSitzungsbeginn();

    /**
     * Gibt die Uhrzeit zurück, zu der die Sitzung endete.
     * 
     * @return ein sql.Time-Objekt, das die Uhrzeit repräsentiert.
     */
    Time getSitzungsende();

    /**
     * Gibt die Dauer der Sitzung in Minuten zurück.
     * 
     * @return ein long, das die Dauer der Sitzung in Minuten repräsentiert.
     */
    long getDauer();

    /**
     * Gibt die einzelnen Tagesordnungspunkte der Sitzung zurück.
     * 
     * @return ein Set von Tagesordnung, das die Tagesordnungspunkte der Sitzung
     *         repräsentieren.
     */
    Set<Tagesordnung> getTagesordnungspunkte();

    /**
     * Gibt die Nummer der Sitzung zurück.
     * 
     * @return ein Integer, der die Nummer der Sitzung repräsentiert.
     */
    int getSitzungsnummer();

    /**
     * Gibt die Wahlperiode der Sitzung zurück.
     * 
     * @return ein Wahlperiode-Objekt, das die Wahlperiode der Sitzung
     *         repräsentiert.
     */
    Wahlperiode getWahlperiode();

    /**
     * Gibt einen String zurück, welcher Informationen über die Sitzung und die
     * Tagesordnung in eine LaTeX angepasste schreibweise schreibt
     * 
     * @return
     */
    String toTeX();
}
