package org.v_04_01.dataprocessing.datastructure.imlementation;

import java.util.HashSet;

import org.v_04_01.dataprocessing.datastructure.Tagesordnung;

public class Tagesordnung_File_Impl extends Tagesordnung_Abstract_Impl implements Tagesordnung {

    /**
     * Erzeugt ein Objekt vom Typ Tagesordnung fuer das Einlesen aus einem File
     * 
     * @param tagesordnungspunkt die Nummer des Tagesordnungspunkts
     * @param sitzung            die Sitzung, zu der die Tagesordnung gehört
     */
    public Tagesordnung_File_Impl(int tagesordnungspunkt, Sitzung_File_Impl sitzung, String titel) {
        this.tagesordnungspunkt = tagesordnungspunkt;
        this.sitzung = sitzung;
        this.titel = titel;
        reden = new HashSet<>();
    }

    /**
     * fügt der Tagesordnung eine Rede hinzu
     * 
     * @param rede die Rede, die hinzugefügt werden soll
     */
    public void addRede(Rede_File_Impl rede) {
        reden.add(rede);
    }
}
