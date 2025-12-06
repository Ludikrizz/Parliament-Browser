package org.v_04_01.dataprocessing.datastructure;

import java.sql.Date;

public interface Institution extends Comparable<Institution> {

    /**
     * Gibt die Art der Institution zurück
     * 
     * @return ein String, der die Art der Institution repräsentiert
     */
    String getInstArt();

    /**
     * Gibt den Titel der Institution zurück. Dieser ist eindeutig fuer den
     * Abgeordneten
     * 
     * @return ein String, der den Titel der Institution repräsentiert
     */
    String getTitel();

    /**
     * Gibt die Funktion des Abgeordneten in der Institution zurück
     * 
     * @return ein String, der die Funktion des Abgeordneten in der Institution
     *         repräsentiert
     */
    String getFunktion();

    /**
     * Gibt das Startdatum der Institution zurück. Es gibt kein Enddate, da die
     * Institutionen nur fuer die Wahlperiode 20 vorhanden sind.
     * 
     * @return ein Date-Objekt, das das Startdatum der Institution repräsentiert
     */
    Date getStartDate();
}
