package org.v_04_01.dataprocessing.helper;

import org.bson.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.v_04_01.database.MongoDBHandler;
import org.v_04_01.dataprocessing.datastructure.*;
import org.v_04_01.dataprocessing.datastructure.imlementation.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Hilfsklsse zum Auslesen der Protokolle aus xml Dateien und erstellen von
 * Reden fuer Abgeordnete
 * 
 * @author Kester Rumke
 */
public class CreateRede {

    private Sitzung_File_Impl sitzung;
    private HashSet<Abgeordneter_File_Impl> abgeordnete;
    private MongoDBHandler mDB;

    /**
     * Erzeugt ein Objekt vom Typ createRede.
     * Die Wahlperiode wird uebergeben, damit nur in dieser die Abgeordneten
     * durchsucht werden muessen
     * 
     * @param sitzung     ein Objekt vom Typ Sitzung, in die das Protokoll fällt
     * @param abgeordnete liste der Abgeordneten, fuer die die Reden erstellt werden
     *                    sollen
     */
    public CreateRede(HashSet<Abgeordneter_File_Impl> abgeordnete, Sitzung_File_Impl sitzung) {
        this.abgeordnete = abgeordnete;
        this.sitzung = sitzung;
        mDB = new MongoDBHandler();
    }

    /**
     * Liest aus dem Protokoll den Punkt Tagesordnungpunkt aus und erstellt davon
     * neue Reden fuer die Abgeordneten
     * 
     * @param element ein Objekt vom Typ Element von dom4j
     */
    public Rede_File_Impl create(Element element, Tagesordnung tagesordnung) {
        TreeSet<Kommentar> kommentare = new TreeSet<>();
        StringBuilder text = new StringBuilder();
        for (int i = 1, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if (node.getName() != null) {
                if (node.getName().equals("p")) {
                    String textNode = node.getText().replaceAll("\\n", " ").replaceAll("\\t", "").trim();
                    if (i < 3) {
                        String[] tempTextNode = textNode.split(":", 2);
                        if (tempTextNode.length > 1) {
                            textNode = tempTextNode[1];
                        }
                    }
                    if (textNode.isEmpty()) {
                        continue;
                    }
                    text.append(textNode).append(" ");
                } else if (node.getName().equals("name")) {
                    break;
                } else if (node.getName().equals("kommentar")) {
                    String kommentarGeber = node.getText().split(":", 2)[0];
                    kommentare.add(new Kommentar_Impl(text.length(), node.getText(),
                            getRepresentativeFromString(kommentarGeber), getCoalitionPartyFromString(kommentarGeber)));
                }
            }
        }
        Abgeordneter_File_Impl abgeordneter = null;
        for (Abgeordneter_File_Impl abgeordneterIterator : abgeordnete) {
            if (abgeordneterIterator.getID() == element.numberValueOf("p/redner/@id").intValue()) {
                abgeordneter = abgeordneterIterator;
                break;
            }
        }

        if (abgeordneter == null) {
            abgeordneter = new Abgeordneter_File_Impl(element.numberValueOf("p/redner/@id").intValue(),
                    element.valueOf("p/redner/name/nachname"), element.valueOf("p/redner/name/vorname"));
            abgeordnete.add(abgeordneter);
        }
        Rede_File_Impl rede = new Rede_File_Impl(
                abgeordneter,
                text.toString(),
                sitzung,
                element.valueOf("@id"),
                kommentare,
                tagesordnung);
        abgeordneter.addRede(rede);
        return rede;
    }

    /**
     * Liest aus einem Kommentar in String From die Fraktionen aus und gibt diese
     * als HashSet von Fraktionen zurueck
     * 
     * @param text der Text des Kommentars
     * @return HashSet von Fraktionen
     */
    private HashSet<Fraktion> getCoalitionPartyFromString(String text) {
        HashSet<Fraktion> fraktionen = new HashSet<>();
        if (text.contains("AfD")) {
            fraktionen.add(new Fraktion_Impl("AfD"));
        }
        if (text.contains("CDU/CSU")) {
            fraktionen
                    .add(new Fraktion_Impl("Fraktion der Christlich Demokratischen Union/Christlich - Sozialen Union"));
        }
        if (text.contains("SPD")) {
            fraktionen.add(new Fraktion_Impl("Fraktion der Sozialdemokratischen Partei Deutschlands"));
        }
        if (text.contains("FDP")) {
            fraktionen.add(new Fraktion_Impl("Fraktion der Freien Demokratischen Partei"));
        }
        if (text.contains("LINKE")) {
            fraktionen.add(new Fraktion_Impl("Fraktion DIE LINKE"));
        }
        if (text.contains("90/DIE")) {
            fraktionen.add(new Fraktion_Impl("Fraktion BÜNDNIS 90/DIE GRÜNEN"));
        }
        return fraktionen;
    }

    /**
     * Liest aus einem Kommentar in String From den Abgeordneten aus und gibt diesen
     * als Abgeordneter zurueck
     * 
     * @param text der Text des Kommentars
     * @return Abgeordneter
     */
    private Abgeordneter getRepresentativeFromString(String text) {
        if (text.contains("[") && text.indexOf("[") != 0) {
            String abgeordnetenText = text.substring(1, text.indexOf("["));
            String[] abgeordneterName = abgeordnetenText.split(" ");
            if (abgeordneterName.length == 2) {
                List<Document> abgeordnete = mDB.read(
                        new Document().append("nachname", abgeordneterName[1]).append("vorname", abgeordneterName[0]),
                        new Document().append("_id", 1.0), "abgeordnete");
                if (!abgeordnete.isEmpty()) {
                    return new Abgeordneter_File_Impl(abgeordnete.get(0).getInteger("_id"), abgeordneterName[0],
                            abgeordneterName[1]);
                }
            }
        }
        return null;
    }
}
