package org.group_04_01.dataprocessing.datastructure.imlementation;

import org.group_04_01.dataprocessing.datastructure.Abgeordneter;
import org.group_04_01.dataprocessing.datastructure.Fraktion;
import org.group_04_01.dataprocessing.datastructure.Wahlperiode;

import java.util.HashSet;
import java.util.Set;

public class Fraktion_Impl implements Fraktion {

    private HashSet<Abgeordneter> abgeordnete;
    private String name;

    /**
     * Erzeugt ein Objekt vom Typ Fraktion fuer das Einlesen aus einem File
     * 
     * @param name der Name der Fraktion
     */
    public Fraktion_Impl(String name) {
        this.name = name;
        abgeordnete = new HashSet<>();
    }

    /**
     * fügt der Fraktion einen Abgeordneten hinzu
     * 
     * @param abgeordneter der Abgeordnete, der hinzugefügt werden soll
     */
    public void addMember(Abgeordneter abgeordneter) {
        abgeordnete.add(abgeordneter);
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
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Fraktion o) {
        return name.compareTo(o.getName());
    }
}
