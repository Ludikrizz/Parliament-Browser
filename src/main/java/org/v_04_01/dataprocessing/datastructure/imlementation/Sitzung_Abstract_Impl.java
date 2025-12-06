package org.v_04_01.dataprocessing.datastructure.imlementation;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.v_04_01.dataprocessing.datastructure.Sitzung;
import org.v_04_01.dataprocessing.datastructure.Tagesordnung;
import org.v_04_01.dataprocessing.datastructure.Wahlperiode;

public abstract class Sitzung_Abstract_Impl implements Sitzung {

    protected Date date;
    protected Time sitzungsbeginn;
    protected Time sitzungsende;
    protected Set<Tagesordnung> tagesordnungspunkte;
    protected int sitzungsnummer;
    protected Wahlperiode wahlperiode;

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Time getSitzungsbeginn() {
        return sitzungsbeginn;
    }

    @Override
    public Time getSitzungsende() {
        return sitzungsende;
    }

    @Override
    public long getDauer() {
        return (sitzungsende.getTime() - sitzungsbeginn.getTime()) / (1000 * 60);
    }

    @Override
    public Set<Tagesordnung> getTagesordnungspunkte() {
        return tagesordnungspunkte;
    }

    @Override
    public int getSitzungsnummer() {
        return sitzungsnummer;
    }

    @Override
    public Wahlperiode getWahlperiode() {
        return wahlperiode;
    }

    @Override
    public int compareTo(Sitzung o) {
        int compareNbr = Integer.compare(sitzungsnummer, o.getSitzungsnummer());
        if (compareNbr == 0) {
            return wahlperiode.compareTo(o.getWahlperiode());
        }
        return compareNbr;
    }

    @Override
    public String toTeX() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        // Situngsüberschrift
        sb.append("\\section{Sitzung Nr. ").append(sitzungsnummer).append(" - ").append(df.format(date)).append("}\n");

        // Tagesordnungspunkte als "Inhaltsverzeichnis" der Sitzung
        sb.append("\\subsection*{Tagesordnungspunkte:}\n");
        sb.append("\\begin{enumerate}\n");
        for (Tagesordnung top : tagesordnungspunkte) {
            sb.append("\\item ").append(top.getTitel()).append("\n");
        }
        sb.append("\\end{enumerate}\n");

        sb.append("\\newpage\n");

        // Detaillierte Ausgabe der Tagesordnungspunkte und Reden
        for (Tagesordnung top : tagesordnungspunkte) {
            sb.append(top.toTeX());
        }
        return sb.toString();
    }
}
