package org.v_04_01.dataprocessing.datastructure.imlementation;

import java.util.HashSet;
import java.util.Set;

import org.v_04_01.dataprocessing.datastructure.Rede;
import org.v_04_01.dataprocessing.datastructure.Sitzung;
import org.v_04_01.dataprocessing.datastructure.Tagesordnung;

public abstract class Tagesordnung_Abstract_Impl implements Tagesordnung {

    protected int tagesordnungspunkt;
    protected Sitzung sitzung;
    protected HashSet<Rede> reden;
    protected String titel;

    @Override
    public int getTagesordnungspunkt() {
        return tagesordnungspunkt;
    }

    @Override
    public Set<Rede> listReden() {
        return reden;
    }

    @Override
    public Sitzung getSitzung() {
        return sitzung;
    }

    @Override
    public String getTitel() {
        return titel;
    }

    @Override
    public int compareTo(Tagesordnung o) {
        return Integer.compare(tagesordnungspunkt, o.getTagesordnungspunkt());
    }

    @Override
    public String toTeX() {
        StringBuilder sb = new StringBuilder();
        sb.append("\\subsection{").append(titel).append("}\n");
        for (Rede rede : reden) {
            sb.append(rede.toTeX());
        }
        return sb.toString();
    }
}
