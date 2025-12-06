package org.group_04_01.dataprocessing.datastructure;

import java.sql.Date;
import java.util.Set;

/**
 * Ein Interface für die Wahlperioden des Bundestages.
 */
public interface Wahlperiode extends Comparable<Wahlperiode> {

    /**
     * Gibt die Nummer der Wahlperiode zurück.
     * Die Nummer der Wahlperiode ist eindeutig.
     * 
     * @return ein Integer, der die Nummer der Wahlperiode repräsentiert.
     */
    int getNumber();

    /**
     * Gibt das Startdatum der Wahlperiode zurück.
     * 
     * @return ein sql.Date-Objekt, das das Startdatum der Wahlperiode
     *         repräsentiert.
     */
    Date getStartDate();

    /**
     * Gibt das Enddatum der Wahlperiode zurück.
     * Falls die Wahlperiode noch nicht beendet ist, wird null zurückgegeben.
     * 
     * @return ein sql.Date-Objekt, das das Enddatum der Wahlperiode repräsentiert.
     */
    Date getEndeDate();

    /**
     * Gibt alle Mandate zurück, die während dieser Wahlperiode ausgeübt wurden.
     * 
     * @return ein Set von Mandaten, die während dieser Wahlperiode ausgeübt wurden.
     */
    Set<Mandat> listMandate();

    /**
     * Gibt alle Mandate zurück, die während dieser Wahlperiode ausgeübt wurden und
     * dem angegebenen Typ entsprechen.
     * 
     * @param mandatType der Typ des Mandats, das zurückgegeben werden soll.
     * @return ein Set von Mandaten, die während dieser Wahlperiode ausgeübt wurden
     *         und dem angegebenen Typ entsprechen.
     */
    Set<Mandat> listMandate(Types.MANDAT mandatType);

}
