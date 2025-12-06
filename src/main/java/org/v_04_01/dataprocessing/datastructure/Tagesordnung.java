package org.v_04_01.dataprocessing.datastructure;

import java.util.Set;

/**
 * Ein Interface, das einen Tagesordnungspunkt repräsentiert.
 * Tagesordnungspunkte sind Teil einer Sitzung.
 */
public interface Tagesordnung extends Comparable<Tagesordnung> {

    /**
     * Gibt die Nummer des Tagesordnungspunktes zurück.
     * Wichtig um die Reihenfolge der Tagesordnungspunkte nachzuvollziehen.
     * 
     * @return ein Integer, der die Nummer des Tagesordnungspunktes repräsentiert.
     */
    int getTagesordnungspunkt();

    /**
     * Gibt alle Reden zurück, die während diesem Tagesordnungspunkt gehalten
     * wurden.
     * 
     * @return ein Set von Reden, die während diesem Tagesordnungspunkt gehalten
     *         wurden.
     */
    Set<Rede> listReden();

    /**
     * Gibt die Sitzung zurück, in der dieser Tagesordnungspunkt behandelt wurde.
     * 
     * @return ein Sitzung-Objekt, das die Sitzung repräsentiert, in der dieser
     *         Tagesordnungspunkt behandelt wurde.
     */
    Sitzung getSitzung();

    /**
     * Gibt den Titel des Tagesordnungspunktes zurück.
     * 
     * @return ein
     */
    String getTitel();

    /**
     * Gibt einen String zurück, welcher Informationen zur Tagesordnung in eine
     * LaTeX angepasste schreibweise schreibt
     * 
     * @return
     */
    String toTeX();
}
