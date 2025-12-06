package org.group_04_01.dataprocessing.datastructure;

public interface Picture extends Comparable<Picture> {

    /**
     * Gibt die lokale URL des Bildes zurück und ist fuer das Frontend zu verwenden.
     * Falls das Bild noch nicht lokal gespeichert wurde, wird es heruntergeladen
     * und die lokale URL zurückgegeben.
     * 
     * @return String der die URL des Bildes enthält
     */
    String getLocalURL();

    /**
     * Gibt die lokale URL des Bildes zurück und ist fuer das Backend zu verwenden.
     * 
     * @return String der die URL des Bildes enthält
     */
    String getLocalURLBackend();

    /**
     * Gibt die URL des Bildes zurück
     * 
     * @return String der die URL des Bildes enthält
     */
    String getRemoteURL();

    /**
     * Gibt den Ort, an dem das Bild aufgenommen wurde zurück
     * 
     * @return String der den Ort des Bildes enthält
     */
    String getLocation();

    /**
     * Gibt das Datum, an dem das Bild aufgenommen wurde zurück
     * 
     * @return String der das Datum des Bildes enthält
     */
    String getDate();

    /**
     * Gibt den Fotografen des Bildes zurück
     * 
     * @return String der den Fotografen des Bildes enthält
     */
    String getPhotographer();

    /**
     * Gibt die Priorität des Bildes zurück in welcher Reihenfolge es angezeigt
     * werden soll
     * Sofern die Priorität nicht gesetzt wurde, wird -1 zurückgegeben
     * 
     * @return int der die Priorität des Bildes enthält
     */
    int getPriority();

    /**
     * Setzt die Priorität des Bildes
     * 
     * @param priority int der die Priorität des Bildes enthält
     */
    void setPriority(int priority);
}
