import org.bson.Document;
import org.group_04_01.database.MongoDBHandler;
import org.group_04_01.dataprocessing.datastructure.Sitzung;
import org.group_04_01.dataprocessing.datastructure.Tagesordnung;
import org.group_04_01.dataprocessing.datastructure.imlementation.Sitzung_MongoDB_Impl;
import org.group_04_01.dataprocessing.datastructure.imlementation.Tagesordnung_MongoDB_Impl;
import org.group_04_01.dataprocessing.helper.LatexDokumentExport;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class TestLatex {

        @Test
        public void testExportiereLatexDokument() {
                MongoDBHandler mDB = new MongoDBHandler();
                List<Sitzung> sitzungen = new LinkedList<>();
                mDB.aggregate(Arrays.asList(
                                new Document()
                                                .append("$match", new Document()
                                                                .append("sitzung.wahlperiode", 20)
                                                                .append("sitzung.sitzungsnummer", 100)),
                                new Document()
                                                .append("$group", new Document()
                                                                .append("_id", new Document()
                                                                                .append("field1",
                                                                                                "$sitzung.wahlperiode")
                                                                                .append("field2",
                                                                                                "$sitzung.sitzungsnummer"))
                                                                .append("uniqueDocument", new Document()
                                                                                .append("$first", "$$ROOT.sitzung"))
                                                                .append("agendas", new Document()
                                                                                .append("$addToSet", "$$ROOT.agenda"))),
                                new Document()
                                                .append("$replaceRoot", new Document()
                                                                .append("newRoot", new Document()
                                                                                .append("$mergeObjects", Arrays.asList(
                                                                                                "$uniqueDocument",
                                                                                                new Document()
                                                                                                                .append("agendas",
                                                                                                                                "$agendas")))))),
                                "reden").forEach(
                                                sitzung -> sitzungen.add(new Sitzung_MongoDB_Impl((sitzung))));
                for (Sitzung sitzung : sitzungen) {
                        for (Tagesordnung top : sitzung.getTagesordnungspunkte()) {
                                if (top instanceof Tagesordnung_MongoDB_Impl) {
                                        ((Tagesordnung_MongoDB_Impl) top).addReden(mDB.aggregate(Arrays.asList(
                                                        new Document()
                                                                        .append("$match", new Document()
                                                                                        .append("sitzung.sitzungsnummer",
                                                                                                        sitzung.getSitzungsnummer())
                                                                                        .append("sitzung.wahlperiode",
                                                                                                        sitzung.getWahlperiode()
                                                                                                                        .getNumber())
                                                                                        .append("agenda.agenda_nr", top
                                                                                                        .getTagesordnungspunkt())),
                                                        new Document()
                                                                        .append("$lookup", new Document()
                                                                                        .append("from", "abgeordnete")
                                                                                        .append("localField",
                                                                                                        "abgeordneter")
                                                                                        .append("foreignField", "_id")
                                                                                        .append("as", "abgeordneter")),
                                                        new Document()
                                                                        .append("$unwind", new Document()
                                                                                        .append("path", "$abgeordneter"))),
                                                        "reden"));
                                }
                        }
                }

                LatexDokumentExport export = new LatexDokumentExport();
                export.exportiereLatexDokument(sitzungen, "src/main/resources/test.tex");
        }
}
