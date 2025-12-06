package org.v_04_01.dataprocessing.datastructure.imlementation;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.v_04_01.dataprocessing.datastructure.*;
import org.v_04_01.dataprocessing.helper.io.Deserializer;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

public abstract class Rede_Abstract_Impl implements Rede {

    protected Abgeordneter abgeordneter;
    protected String text;
    protected String id;
    protected Sitzung sitzung;
    protected Tagesordnung tagesordnung;
    protected TreeSet<Kommentar> kommentare;

    @Override
    public Abgeordneter getAbgeordneter() {
        return abgeordneter;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getLaenge() {
        String tempText = text.replaceAll("\\{.*?\\}", "");
        return new StringTokenizer(tempText, "., ").countTokens();
    }

    @Override
    public Date getDate() {
        return sitzung.getDate();
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public Sitzung getSitzung() {
        return sitzung;
    }

    @Override
    public Tagesordnung getTagesordnung() {
        return tagesordnung;
    }

    @Override
    public int compareTo(Rede o) {
        return id.compareTo(o.getID());
    }

    @Override
    public TreeSet<Kommentar> getKommentare() {
        return kommentare;
    }

    @Override
    public JCas getJCas() throws UIMAException {
        return JCasFactory.createText(text, "de");
    }

    @Override
    public String toTeX() {
        StringBuilder sb = new StringBuilder();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        // Informationen des Redners
        sb.append("\\subsection*{")
                .append(abgeordneter.getAnrede())
                .append(" ")
                .append(abgeordneter.getVorname())
                .append(" ")
                .append(abgeordneter.getName())
                .append("}\\\\\n");

        sb.append("\\textbf{Geburtsdatum}: ")
                .append(df.format(abgeordneter.getGeburtsDatum())).append("\\\\\n")
                .append("\\textbf{Partei}: ")
                .append(abgeordneter.getPartei().getName()).append("\\\\\n");

        // Optionales Bild des Abgeordneten
        if (!abgeordneter.listPictures().isEmpty()) {
            Picture primaryPicture = abgeordneter.getPrimaryPicture();
            sb.append("\\begin{figure}[H]\\centering\n")
                    .append("\\includegraphics[width=0.5\\textwidth]{")
                    .append(primaryPicture.getLocalURLBackend())
                    .append("}\\end{figure}\n");
        }

        File file = new File("src/main/resources/data/speeches/" + sitzung.getSitzungsnummer()
                + tagesordnung.getTagesordnungspunkt() + "/" + id + ".xmi.gz");

        String textWithAnnotations = "";

        if (file.exists()) {
            Deserializer deserializer = new Deserializer();
            try {
                JCas jCas = deserializer.deserialize(file);
                textWithAnnotations = insertAnnotationsIntoText(jCas, kommentare);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (textWithAnnotations.isEmpty()) {
            // Kommentare in den Text der Rede einfügen
            textWithAnnotations = insertCommentsIntoText(text, kommentare);
        }

        // Inhalt der Rede mit eingefügten Kommentaren
        sb.append("\\paragraph{Rede: }\n")
                .append(textWithAnnotations.replaceAll("\n", "\\\\")) // Ersetzt Zeilenumbrüche durch LaTeX
                                                                      // Newline-Befehle
                .append("\n\n");

        return sb.toString();
    }

    /**
     * Fügt die Kommentare wieder an die richtige Stelle in der Rede ein
     * 
     * @param text
     * @param kommentare
     * @return
     * @author Fabio Dias Meda
     */
    private String insertCommentsIntoText(String text, TreeSet<Kommentar> kommentare) {
        StringBuilder sb = new StringBuilder(text);

        Iterator<Kommentar> iterator = kommentare.descendingIterator();

        while (iterator.hasNext()) {
            Kommentar kommentar = iterator.next();
            int pos = kommentar.getPos();
            if (pos <= sb.length()) {
                sb.insert(pos, String.format("[Kommentar: %s]", kommentar.getText()));
            }
        }
        return sb.toString();
    }

    private String insertAnnotationsIntoText(JCas jCas, TreeSet<Kommentar> kommentare) {
        return null;
    }

}
