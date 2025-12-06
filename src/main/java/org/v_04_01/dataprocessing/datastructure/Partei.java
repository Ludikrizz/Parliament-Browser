package org.v_04_01.dataprocessing.datastructure;

import java.util.Set;

/**
 * Interface für Parteien des Bundestages.
 * Eine Partei ist eindeutig durch ihren Namen definiert.
 */
public interface Partei extends Comparable<Partei> {

    /**
     * Gibt den Namen der Partei zurück. Dieser ist eindeutig und ist nur das Kürzel
     * der Partei.
     * 
     * @return ein String, der den Namen der Partei repräsentiert.
     */
    String getName();

    /**
     * Gibt alle Mitglieder der Partei zurück.
     * 
     * @return ein Set von Abgeordneten, die Mitglieder der Partei sind.
     */
    Set<Abgeordneter> getMembers();

    /**
     * Gibt alle Mitglieder der Partei zurück, die in der angegebenen Wahlperiode
     * Mitglied der Partei waren
     * und im Bundestag vertreten waren.
     * 
     * @param wahlperiode die Wahlperiode, für die die Mitglieder der Partei
     *                    zurückgegeben werden sollen.
     * @return ein Set von Abgeordneten, die Mitglieder der Partei waren.
     */
    Set<Abgeordneter> getMembers(Wahlperiode wahlperiode);
}
