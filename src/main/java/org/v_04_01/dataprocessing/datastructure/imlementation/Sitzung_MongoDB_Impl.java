package org.v_04_01.dataprocessing.datastructure.imlementation;

import org.bson.Document;
import org.v_04_01.dataprocessing.datastructure.Sitzung;
import org.v_04_01.dataprocessing.helper.DateConverter;

import java.util.HashSet;
import java.util.TreeSet;

public class Sitzung_MongoDB_Impl extends Sitzung_Abstract_Impl implements Sitzung {

    /**
     * Konstruktor für eine neue Sitzung aus der Datenbank.
     * Wenn agendas mit übergeben werden, werden auch die Tagesordnungspunkte
     * erstellt.
     * 
     * @param document Datenbankobjekt
     */
    public Sitzung_MongoDB_Impl(Document document) {
        DateConverter dc = new DateConverter("yyyy-MM-dd");
        date = dc.convert(document.getString("date"));
        sitzungsbeginn = dc.convertTime(document.getString("sitzungsbeginn"));
        sitzungsende = dc.convertTime(document.getString("sitzungsende"));
        sitzungsnummer = document.getInteger("sitzungsnummer");
        wahlperiode = new Wahlperiode_Impl(document.getInteger("wahlperiode"));
        if (document.containsKey("agendas")) {
            tagesordnungspunkte = new TreeSet<>();
            document.getList("agendas", Document.class).forEach(
                    agenda -> tagesordnungspunkte.add(new Tagesordnung_MongoDB_Impl(agenda)));
        }
    }

}
