package org.group_04_01.dataprocessing.datastructure.imlementation;

import org.group_04_01.dataprocessing.datastructure.Kommentar;
import org.group_04_01.dataprocessing.datastructure.Rede;
import org.group_04_01.dataprocessing.datastructure.Sitzung;
import org.group_04_01.dataprocessing.datastructure.Tagesordnung;

import java.util.HashSet;
import java.util.TreeSet;

public class Rede_File_Impl extends Rede_Abstract_Impl implements Rede {

    /**
     * Erzeugt ein Objekt vom Typ Rede fuer das Einlesen aus einem File
     * 
     * @param abgeordneter der Abgeordnete, der die Rede gehalten hat
     * @param text         der Text der Rede
     * @param sitzung      die Sitzung, in der die Rede gehalten wurde
     * @param id           die ID der Rede
     * @param kommentare   die Kommentare, die zu der Rede gehalten wurden
     * @param tagesordnung die Tagesordnung, zu der die Rede gehalten wurde
     */
    public Rede_File_Impl(Abgeordneter_File_Impl abgeordneter, String text, Sitzung sitzung, String id,
            TreeSet<Kommentar> kommentare, Tagesordnung tagesordnung) {
        this.abgeordneter = abgeordneter;
        this.text = text;
        this.sitzung = sitzung;
        this.id = id;
        this.kommentare = kommentare;
        this.tagesordnung = tagesordnung;
    }

}
