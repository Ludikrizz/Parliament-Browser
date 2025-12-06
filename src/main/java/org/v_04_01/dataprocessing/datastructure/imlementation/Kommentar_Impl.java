package org.v_04_01.dataprocessing.datastructure.imlementation;

import java.util.HashSet;

import org.v_04_01.dataprocessing.datastructure.Abgeordneter;
import org.v_04_01.dataprocessing.datastructure.Fraktion;
import org.v_04_01.dataprocessing.datastructure.Kommentar;

public class Kommentar_Impl implements Kommentar {

    private int pos;
    private String text;
    private Abgeordneter abgeordneter;
    private HashSet<Fraktion> fraktion;

    public Kommentar_Impl(int pos, String text, Abgeordneter abgeordneter, HashSet<Fraktion> fraktion) {
        this.pos = pos;
        this.text = text;
        this.abgeordneter = abgeordneter;
        this.fraktion = fraktion;
    }

    @Override
    public int getPos() {
        return pos;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Abgeordneter getAbgeordneter() {
        return abgeordneter;
    }

    @Override
    public HashSet<Fraktion> getFraktionen() {
        return fraktion;
    }

    @Override
    public int compareTo(Kommentar o) {
        return Integer.compare(pos, o.getPos());
    }
}
