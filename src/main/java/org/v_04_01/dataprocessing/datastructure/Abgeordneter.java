package org.v_04_01.dataprocessing.datastructure;

import java.sql.Date;
import java.util.Set;

/**
 * Interface für die Abgeordneten des Bundestages.
 */
public interface Abgeordneter extends Comparable<Abgeordneter> {

    /**
     * Gibt die ID des Abgeordneten zurück, welche durch die XML-Datei festgelegt
     * ist.
     * 
     * @return ein Integer, der die ID des Abgeordneten repräsentiert.
     */
    int getID();

    /**
     * Gibt den Nachnamen des Abgeordneten zurück.
     * 
     * @return ein String, der den Namen des Abgeordneten repräsentiert.
     */
    String getName();

    /**
     * Gibt den Vornamen des Abgeordneten zurück.
     * 
     * @return ein String, der den Vornamen des Abgeordneten repräsentiert.
     */
    String getVorname();

    /**
     * Gibt den Ortszusatz des Abgeordneten zurück, sofern dieser in der XML-Datei
     * existiert.
     * 
     * @return ein String, der den Ortszusatz des Abgeordneten repräsentiert.
     */
    String getOrtszusatz();

    /**
     * Gibt den Adelstitel des Abgeordneten zurück, sofern dieser in der XML-Datei
     * existiert.
     * 
     * @return ein String, der den Adelstitel des Abgeordneten repräsentiert.
     */
    String getAdelssuffix();

    /**
     * Gibt die Anrede des Abgeordneten zurück, sofern diese in der XML-Datei
     * existiert.
     * 
     * @return ein String, der die Anrede des Abgeordneten repräsentiert.
     */
    String getAnrede();

    /**
     * Gibt den akademischen Titel des Abgeordneten zurück, sofern dieser in der
     * XML-Datei existiert.
     * 
     * @return ein String, der den akademischen Titel des Abgeordneten
     *         repräsentiert.
     */
    String getAkadTitel();

    /**
     * Gibt das Geburtsdatum des Abgeordneten zurück.
     * 
     * @return ein sql.Date-Objekt, das das Geburtsdatum des Abgeordneten
     *         repräsentiert.
     */
    Date getGeburtsDatum();

    /**
     * Gibt den Geburtsort des Abgeordneten zurück.
     * 
     * @return ein String, der den Geburtsort des Abgeordneten repräsentiert.
     */
    String getGeburtsOrt();

    /**
     * Gibt das Sterbedatum des Abgeordneten zurück, sofern dieses in der XML-Datei
     * existiert
     * (bzw. der Abgeordnete noch lebt).
     * 
     * @return ein sql.Date-Objekt, das das Sterbedatum des Abgeordneten
     *         repräsentiert.
     */
    Date getSterbeDatum();

    /**
     * Gibt das Geschlecht des Abgeordneten zurück.
     * 
     * @return ein Enum-Objekt (MAENNLICH, WEIBLICH), das das Geschlecht des
     *         Abgeordneten repräsentiert.
     */
    Types.GESCHLECHT getGeschlecht();

    /**
     * Gibt die Religion des Abgeordneten zurück, sofern diese in der XML-Datei
     * existiert
     * (bzw. der Abgeordnete keiner Religion angehört).
     * 
     * @return ein String, der die Religion des Abgeordneten repräsentiert.
     */
    String getReligion();

    /**
     * Gibt die Berufsbezeichnung des Abgeordneten zurück, sofern diese in der
     * XML-Datei existiert.
     * 
     * @return ein String, der die Berufsbezeichnung des Abgeordneten repräsentiert.
     */
    String getBeruf();

    /**
     * Gibt die Vita des Abgeordneten zurück, sofern diese in der XML-Datei
     * existiert.
     * 
     * @return ein String, der die Vita des Abgeordneten repräsentiert.
     */
    String getVita();

    /**
     * Gibt die Mandate des Abgeordneten zurück.
     * 
     * @return ein Set von Mandat, die der Abgeordnete hatte.
     */
    Set<Mandat> listMandate();

    /**
     * Gibt die Mandate des Abgeordneten für eine Wahlperiode zurück.
     * 
     * @param wahlperiode die Wahlperiode, für die die Mandate zurückgegeben werden
     *                    sollen.
     * @return ein Set von Mandat, die der Abgeordnete hatte.
     */
    Set<Mandat> listMandate(Wahlperiode wahlperiode);

    /**
     * Gibt an, ob der Abgeordnete in der übergebenen Wahlperiode ein Mandat hatte.
     * 
     * @param wahlperiode die Wahlperiode, für die geprüft werden soll, ob der
     *                    Abgeordnete ein Mandat hatte.
     * @return true, wenn der Abgeordnete in der übergebenen Wahlperiode ein Mandat
     *         hatte, sonst false.
     */
    boolean hasMandat(Wahlperiode wahlperiode);

    /**
     * Gibt die Partei des Abgeordneten zurück, sofern diese in der XML-Datei
     * existiert.
     * 
     * @return ein Objekt vom Typ Partei oder null, falls der Abgeordnete zu keiner
     *         Partei gehört
     */
    Partei getPartei();

    /**
     * Gibt die Reden des Abgeordneten zurück.
     * 
     * @return ein Set von Rede, die der Abgeordnete gehalten hat.
     */
    Set<Rede> listReden();

    /**
     * Gibt die Fraktionsmitgliedschaften des Abgeordneten zurück.
     * 
     * @return ein Set von FraktionsMitgliedschaft, die der Abgeordnete hatte.
     */
    Set<FraktionsMitgliedschaft> listFraktionsmitgliedschaften();

    /**
     * Gibt die Bilder des Abgeordneten zurück.
     * 
     * @return ein Set von Picture, die den Abgeordneten zeigen.
     */
    Set<Picture> listPictures();

    /**
     * Gibt die Institutionen des Abgeordneten zurück.
     * 
     * @return ein Set von Institution, die der Abgeordnete hatte.
     */
    Set<Institution> listInstitutionen();

    /**
     * Gibt das primäre Bild des Abgeordneten zurück, sofern noch nicht bestimmt
     * wird ein zufälliges Bild zurückgegeben.
     * 
     * @return ein Picture-Objekt, das das primäre Bild des Abgeordneten
     *         repräsentiert.
     */
    Picture getPrimaryPicture();
}
