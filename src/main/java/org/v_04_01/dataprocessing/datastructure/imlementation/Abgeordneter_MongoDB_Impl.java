package org.v_04_01.dataprocessing.datastructure.imlementation;

import org.bson.Document;
import org.v_04_01.dataprocessing.datastructure.*;
import org.v_04_01.dataprocessing.helper.DateConverter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class Abgeordneter_MongoDB_Impl extends Abgeordneter_Abstract_Impl implements Abgeordneter {

    /**
     * Konstruktor für einen neuen Abgeordneten aus der Datenbank über eine Fraktion
     * ausgelesen
     * 
     * @param document Datenbankobjekt
     */
    public Abgeordneter_MongoDB_Impl(Document document) {
        helpCreateThis(document);

    }

    public Abgeordneter_MongoDB_Impl(String id) {
        this.id = Integer.parseInt(id);
    }

    /**
     * Helper-Methode für die Konstruktoren um die Daten aus der Datenbank zu lesen
     * 
     * @param document Datenbankobjekt
     */
    private void helpCreateThis(Document document) {
        mandate = new TreeSet<>();
        fraktionsMitgliedschaften = new TreeSet<>();
        pictures = new TreeSet<>();
        institutionen = new HashSet<>();
        reden = new HashSet<>();
        DateConverter dc = new DateConverter("yyyy-MM-dd");
        id = document.getInteger("_id");
        name = document.getString("nachname");
        vorname = document.getString("vorname");
        ortszusatz = document.getString("ortszusatz");
        adelssuffix = document.getString("adelssuffix");
        anrede = document.getString("anrede");
        akadTitel = document.getString("akadTitel");
        geburtsdatum = dc.convert(document.getString("geburtsdatum"));
        geburtsort = document.getString("geburtsort");
        sterbedatum = dc.convert(document.getString("sterbedatum"));
        if (document.containsKey("geschlecht")) {
            geschlecht = document.getString("geschlecht").equals("WEIBLICH") ? Types.GESCHLECHT.WEIBLICH
                    : Types.GESCHLECHT.MAENNLICH;
        }
        religion = document.getString("religion");
        beruf = document.getString("beruf");
        vita = document.getString("vita");
        if (document.containsKey("mandate")) {
            document.getList("mandate", Document.class).forEach(
                    mandat -> mandate.add(new Mandat_Impl(this,
                            mandat.getString("mandatsart").equals("LANDESLISTE") ? Types.MANDAT.LANDESLISTE
                                    : Types.MANDAT.DIREKTWAHL,
                            new Wahlperiode_Impl(mandat.getInteger("wahlperiode"),
                                    dc.convert(mandat.getString("start_date")),
                                    dc.convert(mandat.getString("end_date"))))));
        }
        if (document.containsKey("fraktionsmitgliedschaften")) {
            document.getList("fraktionsmitgliedschaften", Document.class).forEach(
                    fraktionsmitgliedschaft -> fraktionsMitgliedschaften.add(new FraktionsMitgliedschaft_Impl(this,
                            dc.convert(fraktionsmitgliedschaft.getString("start_date")),
                            dc.convert(fraktionsmitgliedschaft.getString("end_date")),
                            new Fraktion_Impl(fraktionsmitgliedschaft.getString("fraktion")),
                            new Wahlperiode_Impl(fraktionsmitgliedschaft.getInteger("wahlperiode"),
                                    dc.convert(fraktionsmitgliedschaft.getString("start_date")),
                                    dc.convert(fraktionsmitgliedschaft.getString("end_date"))))));
        }
        if (document.containsKey("partei")) {
            partei = new Partei_Impl(document.getString("partei"));
        }
        if (document.containsKey("pictures")) {
            document.getList("pictures", Document.class).forEach(
                    picture -> pictures.add(new Picture_Impl(picture.getString("local_url"),
                            picture.getString("remote_url"),
                            picture.getString("location"),
                            picture.getString("date"),
                            picture.getString("photographer"),
                            picture.getInteger("priority"))));
        }
        if (document.containsKey("institutionen")) {
            document.getList("institutionen", Document.class).forEach(
                    institution -> institutionen.add(new Institution_Impl(institution.getString("inst_art"),
                            institution.getString("titel"),
                            institution.getString("funktion"),
                            dc.convert(institution.getString("start_date")))));
        }
        if (document.containsKey("reden_ids")) {
            List<?> redenDocuments = document.getList("reden_ids", Object.class);
            if (redenDocuments != null) {
                for (Object rede : redenDocuments) {
                    if (rede instanceof Document) {
                        reden.add(new Rede_MongoDB_Impl((Document) rede, this));
                    } else {
                        reden.add(new Rede_MongoDB_Impl((String) rede));
                    }
                }
            }
        }
    }

    /**
     * gibt die ID des Abgeordneten als String zurück
     * 
     * @return ID des Abgeordneten als String
     */
    public String getStringID() {
        return String.valueOf(id);
    }

    public Fraktion getCurrentFraktion() {
        for (FraktionsMitgliedschaft fraktionsMitgliedschaft : fraktionsMitgliedschaften) {
            if (fraktionsMitgliedschaft.getWahlperiode().getNumber() == 20) {
                return fraktionsMitgliedschaft.getFraktion();
            }
        }
        return null;
    }

    public LinkedList<FraktionsMitgliedschaft> listFraktionsmitgliedschaftenAsList() {
        return new LinkedList<>(fraktionsMitgliedschaften);
    }
}
