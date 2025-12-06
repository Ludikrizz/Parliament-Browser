package org.group_04_01.dataprocessing.datastructure;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import java.sql.Date;
import java.util.TreeSet;

/**
 * Ein Interface, das eine Rede repräsentiert.
 */
public interface Rede extends Comparable<Rede> {

    /**
     * Gibt den Abgeordneten zurück, der die Rede gehalten hat.
     * 
     * @return ein Abgeordneter-Objekt, das den Redner repräsentiert.
     */
    Abgeordneter getAbgeordneter();

    /**
     * Gibt den Text der Rede zurück.
     * Für das Frontend sollte die Funktion getPreprocessedText() verwendet werden
     * von Rede_MongoDB_Impl
     * 
     * @return ein String, der den Text der Rede repräsentiert.
     */
    String getText();

    /**
     * Gibt die Länge der Rede zurück.
     * Die Länge wird in Wörtern angegeben und eingeworfene Kommentare werden nicht
     * mitgezählt!
     * 
     * @return ein int, das die Länge der Rede repräsentiert.
     */
    int getLaenge();

    /**
     * Gibt das Datum, an dem die Rede gehalten wurde, zurück.
     * 
     * @return ein sql.Date-Objekt, das das Datum der Rede repräsentiert.
     */
    Date getDate();

    /**
     * Gibt die eindeutige ID der Rede zurück.
     * Die ID stammt aus den Daten des Bundestages und ist eindeutig.
     * 
     * @return ein String-Objekt, das die ID der Rede repräsentiert.
     */
    String getID();

    /**
     * Gibt die Sitzung zurück, in der die Rede gehalten wurde.
     * 
     * @return ein Sitzung-Objekt, das die Sitzung repräsentiert.
     */
    Sitzung getSitzung();

    /**
     * Gibt die einzelnen Kommentare, die während der Rede geäußert wurden, zurück.
     * 
     * @return ein Set von Kommentar-Objekten, das die Kommentare repräsentiert.
     */
    TreeSet<Kommentar> getKommentare();

    /**
     * Gibt den Tagesordnungspunkt zurück, in dem die Rede gehalten wurde.
     * 
     * @return ein Tagesordnung-Objekt, das den Tagesordnungspunkt repräsentiert.
     */
    Tagesordnung getTagesordnung();

    /**
     * Gibt ein JCas-Objekt zurück, welches den Text der Rede enthält.
     * 
     * @return JCas-Objekt
     * @throws UIMAException wenn das JCas-Objekt nicht erstellt werden kann
     */
    JCas getJCas() throws UIMAException;

    /**
     * Gibt einen String zurück, welcher Informationen über den Redner und die Rede
     * in eine LaTeX angepasste schreibweise schreibt
     * 
     * @return
     */
    String toTeX();
}
