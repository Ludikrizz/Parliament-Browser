package org.v_04_01.dataprocessing.datastructure.imlementation;

import java.util.UUID;

import org.v_04_01.dataprocessing.datastructure.Abgeordneter;
import org.v_04_01.dataprocessing.datastructure.Mandat;
import org.v_04_01.dataprocessing.datastructure.Types;
import org.v_04_01.dataprocessing.datastructure.Wahlperiode;

public class Mandat_Impl implements Mandat {

    private Abgeordneter abgeordneter;
    private Types.MANDAT mandatTyp;
    private Wahlperiode wahlperiode;
    private UUID id;

    /**
     * Erzeugt ein Objekt vom Typ Mandat fuer das Einlesen aus einem File
     * 
     * @param abgeordneter der Abgeordnete, der das Mandat innehat
     * @param mandatTyp    der Typ des Mandats
     * @param wahlperiode  die Wahlperiode, in der das Mandat ausgeuebt wird
     */
    public Mandat_Impl(Abgeordneter abgeordneter, Types.MANDAT mandatTyp, Wahlperiode wahlperiode) {
        this.abgeordneter = abgeordneter;
        this.mandatTyp = mandatTyp;
        this.wahlperiode = wahlperiode;
        id = UUID.randomUUID();
    }

    @Override
    public Abgeordneter getAbgeordneter() {
        return abgeordneter;
    }

    @Override
    public Types.MANDAT getTyp() {
        return mandatTyp;
    }

    @Override
    public Wahlperiode getWahlperiode() {
        return wahlperiode;
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public int compareTo(Mandat o) {
        int result = wahlperiode.compareTo(o.getWahlperiode());
        if (result == 0) {
            return id.compareTo(o.getID());
        }
        return result;
    }
}
