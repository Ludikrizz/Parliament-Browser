package org.group_04_01.dataprocessing.datastructure;

import java.util.Set;

public interface Kommentar extends Comparable<Kommentar> {

    /**
     * Gibt den Text des Kommentars zurück, wie er im Protokoll steht mit angaben
     * zum Kommentargeber.
     * 
     * @return ein String, der den Text des Kommentars repräsentiert.
     */
    String getText();

    /**
     * Gibt die Position des Kommentars in der Rede zurück.
     * Die Position ist eindeutig.
     * 
     * @return ein int, der die Position des Kommentars repräsentiert.
     */
    int getPos();

    /**
     * Gibt den Abgeordneten zurück, der den Kommentar gegeben hat, sofern er
     * bekannt ist.
     * 
     * @return ein Abgeordneter-Objekt, das den Abgeordneten repräsentiert.
     */
    Abgeordneter getAbgeordneter();

    /**
     * Gibt die Fraktion zurück, die den Kommentar gegeben hat, sofern sie bekannt
     * ist.
     * Zum Beispiel Applaus
     * 
     * @return ein Fraktion-Objekt, das die Fraktion repräsentiert.
     */
    Set<Fraktion> getFraktionen();
}
