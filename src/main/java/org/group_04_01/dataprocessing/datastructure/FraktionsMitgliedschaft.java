package org.group_04_01.dataprocessing.datastructure;

import java.sql.Date;

/**
 * Interface für die Mitgliedschaften der Abgeordneten in den Fraktionen des
 * Bundestages.
 * Eine Mitgliedschaft ist immer auf eine Wahlperiode beschränkt.
 */
public interface FraktionsMitgliedschaft extends Comparable<FraktionsMitgliedschaft> {

    /**
     * Gibt den Abgeordneten zurück, der Mitglied der Fraktion war
     * 
     * @return ein Abgeordneter-Objekt, das den Abgeordneten repräsentiert
     */
    Abgeordneter getAbgeordneter();

    /**
     * Gibt an, seit wann der Abgeordnete Mitglied der Fraktion war (Gilt für eine
     * Wahlperiode)
     * Wenn das Datum nicht im XML-Dokument angegeben ist, wird das Startdatum der
     * Wahlperiode zurückgegeben
     * 
     * @return ein sgl.Date-Objekt, das das Datum repräsentiert, seit dem der
     *         Abgeordnete Mitglied der Fraktion war
     */
    Date fromDate();

    /**
     * Gibt an, bis wann der Abgeordnete Mitglied der Fraktion war (Gilt für eine
     * Wahlperiode).
     * Wenn das Datum nicht im XML-Dokument angegeben ist, wird das Enddatum der
     * Wahlperiode zurückgegeben.
     * Falls die Wahlperiode noch nicht beendet ist, wird null zurückgegeben.
     * 
     * @return ein sgl.Date-Objekt, das das Datum repräsentiert, bis wann der
     *         Abgeordnete Mitglied der Fraktion war
     */
    Date toDate();

    /**
     * Gibt die Fraktion zurück, der der Abgeordnete angehörte
     * 
     * @return ein Fraktion-Objekt, das die Fraktion repräsentiert
     */
    Fraktion getFraktion();

    /**
     * Gibt die Wahlperiode zurück, in der der Abgeordnete Mitglied der Fraktion war
     * 
     * @return ein Wahlperiode-Objekt, das die Wahlperiode repräsentiert
     */
    Wahlperiode getWahlperiode();
}
