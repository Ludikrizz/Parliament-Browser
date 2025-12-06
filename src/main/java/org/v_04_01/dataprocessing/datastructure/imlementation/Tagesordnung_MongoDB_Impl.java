package org.v_04_01.dataprocessing.datastructure.imlementation;

import org.bson.Document;
import org.v_04_01.dataprocessing.datastructure.Tagesordnung;

import java.util.HashSet;
import java.util.List;

public class Tagesordnung_MongoDB_Impl extends Tagesordnung_Abstract_Impl implements Tagesordnung {

    public Tagesordnung_MongoDB_Impl(Document document) {
        tagesordnungspunkt = document.getInteger("agenda_nr");
        titel = document.getString("titel");
    }

    public void addReden(List<Document> reden) {
        this.reden = new HashSet<>();
        for (Document rede : reden) {
            this.reden.add(new Rede_MongoDB_Impl(rede));
        }
    }

}
