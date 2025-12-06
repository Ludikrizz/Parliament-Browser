package org.group_04_01.dataprocessing.datastructure;

import java.util.UUID;

/**
 * Interface für die Mandate der Abgeordneten des Bundestages.
 * Ein Mandat ist immer auf eine Wahlperiode beschränkt.
 */
public interface Mandat extends Comparable<Mandat> {

    /**
     * Gibt den Abgeordneten zurück, der das Mandat innehatte.
     * 
     * @return ein Abgeordneter-Objekt, das den Abgeordneten repräsentiert.
     */
    Abgeordneter getAbgeordneter();

    /**
     * Gibt den Typ des Mandats an (Direktwahl, Landesliste)
     * 
     * @return ein Types.MANDAT (Enum), das den Typ des Mandats repräsentiert.
     */
    Types.MANDAT getTyp();

    /**
     * Gibt die Wahlperiode zurück, in der das Mandat ausgeübt wurde.
     * 
     * @return ein Wahlperiode-Objekt, das die Wahlperiode repräsentiert.
     */
    Wahlperiode getWahlperiode();

    /**
     * Gibt eine eindeutige ID für das Mandat zurück.
     * 
     * @return ein UUID-Objekt
     */
    UUID getID();

}
