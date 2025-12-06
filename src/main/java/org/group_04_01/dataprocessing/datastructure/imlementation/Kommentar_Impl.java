package org.group_04_01.dataprocessing.datastructure.imlementation;

import org.group_04_01.dataprocessing.datastructure.Abgeordneter;
import org.group_04_01.dataprocessing.datastructure.Fraktion;
import org.group_04_01.dataprocessing.datastructure.Kommentar;

import java.util.HashSet;

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
