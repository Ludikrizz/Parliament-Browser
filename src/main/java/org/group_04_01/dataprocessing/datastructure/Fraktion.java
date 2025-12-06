package org.group_04_01.dataprocessing.datastructure;

import java.util.Set;

/**
 * Interface für die Fraktionen des Bundestages.
 */
public interface Fraktion extends Comparable<Fraktion> {

    /**
     * Gibt die Mitglieder der Fraktion zurück
     * 
     * @return ein Set von Abgeordneten der Fraktion
     */
    Set<Abgeordneter> getMembers();

    /**
     * Gibt die Mitglieder der Fraktion zurück, die in der angegebenen Wahlperiode
     * Mitglied waren
     * 
     * @param wahlperiode die Wahlperiode, für die die Mitglieder der Fraktion
     *                    zurückgegeben werden sollen
     * @return ein Set von Abgeordneten der Fraktion
     */
    Set<Abgeordneter> getMembers(Wahlperiode wahlperiode);

    /**
     * Gibt den Namen der Fraktion zurück
     * 
     * @return ein String, der den Namen der Fraktion repräsentiert
     */
    String getName();
}