package org.v_04_01.dataprocessing.datastructure.imlementation;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.v_04_01.dataprocessing.datastructure.Abgeordneter;
import org.v_04_01.dataprocessing.datastructure.Kommentar;
import org.v_04_01.dataprocessing.datastructure.Rede;

import java.util.TreeSet;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Rede_MongoDB_Impl extends Rede_Abstract_Impl implements Rede {

    private int laenge;
    private String preprocessedText;

    /**
     * Konstruktor für eine Rede aus der Datenbank ohne Text um Rechenzeit zu sparen
     * 
     * @param document Datenbankobjekt
     * @author Kester Rumke
     */
    public Rede_MongoDB_Impl(Document document, Abgeordneter abgeordneter) {

        id = document.getString("_id");
        this.abgeordneter = abgeordneter;
        sitzung = new Sitzung_MongoDB_Impl(document.get("sitzung", Document.class));
    }

    /**
     * Konstruktor für eine Rede aus der Datenbank mit Text und ggf. Abgeordneten
     * der die Rede gehalten hat.
     * Wenn das Document keine Abgeordneten enthält, wird der Abgeordnete auf null
     * gesetzt.
     * 
     * @param document Datenbankobjekt
     * @author Kester Rumke
     */
    public Rede_MongoDB_Impl(Document document) {
        kommentare = new TreeSet<>();
        id = document.getString("_id");
        if (document.containsKey("laenge")) {
            laenge = document.getInteger("laenge");
        }
        if (document.containsKey("text")) {
            text = document.getString("text");
        }
        Object abgeordneterData = document.get("abgeordneter");
        if (abgeordneterData instanceof Document) {
            abgeordneter = new Abgeordneter_MongoDB_Impl((Document) abgeordneterData);
        } else {
            abgeordneter = new Abgeordneter_MongoDB_Impl(abgeordneterData.toString());
        }
        if (document.containsKey("sitzung")) {
            sitzung = new Sitzung_MongoDB_Impl(document.get("sitzung", Document.class));
        }
        if (document.containsKey("agenda")) {
            tagesordnung = new Tagesordnung_MongoDB_Impl(document.get("agenda", Document.class));
        }
        if (document.containsKey("kommentare")) {
            document.getList("kommentare", Document.class).forEach(
                    kommentar -> kommentare.add(new Kommentar_Impl(kommentar.getInteger("pos"),
                            kommentar.getString("text"),
                            kommentar.get("abgeordneter") instanceof Document
                                    ? new Abgeordneter_MongoDB_Impl(kommentar.get("abgeordneter", Document.class))
                                    : kommentar.get("abgeordneter") instanceof Integer ? new Abgeordneter_MongoDB_Impl(
                                            String.valueOf(kommentar.getInteger("abgeordneter"))) : null,
                            new HashSet<>(kommentar.getList("fraktionen", String.class).stream().map(
                                    Fraktion_Impl::new).collect(Collectors.toList())))));
        }
        if (document.containsKey("nlp_text")) {
            preprocessedText = document.getString("nlp_text");
        }
    }

    /**
     * Konstruktor fuer eine Rede aus der Datenbank nur mit der ID. Als dummy, wenn
     * aus den Abgeordneten gelesen wird
     * 
     * @param id die ID der Rede
     * @author Kester Rumke
     */
    public Rede_MongoDB_Impl(String id) {
        this.id = id;
    }

    /**
     * Überschriebene Methode aus dem Interface Rede. Gibt die Länge der Rede
     * zurück.
     * Kann aus der DB ausgelesen werden und spart somit Rechenzeit.
     * 
     * @return Länge der Rede in Wörtern
     */
    @Override
    public int getLaenge() {
        if (laenge == 0) {
            return super.getLaenge();
        }
        return laenge;
    }

    /**
     * Gibt den Verarbeiteten Text zurück, der die Kommentare enthält und wenn mit
     * nlp verarbeitet, auch die Entities.
     * Diese Funktion sollte im Frontend verwendet werden, um den Text anzuzeigen.
     * 
     * @return String Objekt, dass den Text repräsentiert
     * @author Kester Rumke
     */
    public String getPreprocessedText() {
        if (preprocessedText != null) {
            return preprocessedText;
        }
        StringBuilder textWithComments = new StringBuilder();
        textWithComments.append("<p>");
        textWithComments.append(text);
        textWithComments.append("</p>");
        int offset = 3;
        for (Kommentar kommentar : kommentare) {
            int oldLength = textWithComments.length();
            textWithComments.insert(kommentar.getPos() + offset,
                    "<span class=\"kommentar\">" + kommentar.getText() + "</span>");
            offset += textWithComments.length() - oldLength;
        }
        return textWithComments.toString();
    }

    public void addText(String text) {
        this.text = text;
    }
}
