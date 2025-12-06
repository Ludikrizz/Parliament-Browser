package org.v_04_01.dataprocessing.datastructure.imlementation;

import java.util.HashSet;
import java.util.Set;

import org.v_04_01.dataprocessing.datastructure.Abgeordneter;
import org.v_04_01.dataprocessing.datastructure.Partei;
import org.v_04_01.dataprocessing.datastructure.Wahlperiode;

public class Partei_Impl implements Partei {

    private String name;
    private HashSet<Abgeordneter> abgeordnete;

    /**
     * Erzeugt ein Objekt vom Typ Partei fuer das Einlesen aus einem File
     * 
     * @param name der Name der Partei
     */
    public Partei_Impl(String name) {
        this.name = name;
        abgeordnete = new HashSet<>();
    }

    /**
     * fügt der Partei einen Abgeordneten hinzu
     * 
     * @param abgeordneter der Abgeordnete, der hinzugefügt werden soll
     */
    public void addMember(Abgeordneter abgeordneter) {
        abgeordnete.add(abgeordneter);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Abgeordneter> getMembers() {
        return abgeordnete;
    }

    @Override
    public Set<Abgeordneter> getMembers(Wahlperiode wahlperiode) {
        HashSet<Abgeordneter> abgeordneterInWahlperiode = new HashSet<>();
        for (Abgeordneter abgeordneter : abgeordnete) {
            if (!abgeordneter.listMandate(wahlperiode).isEmpty()) {
                abgeordneterInWahlperiode.add(abgeordneter);
            }
        }
        return abgeordneterInWahlperiode;
    }

    @Override
    public int compareTo(Partei o) {
        return name.compareTo(o.getName());
    }

}
