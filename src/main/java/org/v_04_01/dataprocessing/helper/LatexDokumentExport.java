package org.v_04_01.dataprocessing.helper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.v_04_01.dataprocessing.datastructure.Sitzung;

/**
 * Eine Hilfsklasse, welche die erstellten Latex gerechten Strings in eine Latex
 * Datei exportiert
 * 
 * @author Fabio Dias Meda
 */

public class LatexDokumentExport {

    /**
     * Methode für den Export det .tex Datei
     * 
     * @param sitzungen
     * @param dateipfad
     */
    public void exportiereLatexDokument(List<Sitzung> sitzungen, String dateipfad) {

        StringBuilder sb = new StringBuilder();

        // Kopfteil des LaTeX-Dokuments
        sb.append("\\documentclass[11pt]{article}\n")
                .append("\\usepackage[utf8]{inputenc}\n")
                .append("\\usepackage{graphicx}\n") // Ermöglicht Bildintegration
                .append("\\usepackage{hyperref}\n") // Ermöglicht Hyperlinks
                .append("\\begin{document}\n")
                .append("\\tableofcontents\n") // Generiert das Inhaltsverzeichnis
                .append("\\newpage\n");

        // Generiert den Inhalt basierend auf den Sitzungen
        for (Sitzung sitzung : sitzungen) {
            sb.append(sitzung.toTeX());
        }

        sb.append("\\end{document}");

        // Schreibt den generierten Inhalt in eine .tex-Datei
        try (FileWriter writer = new FileWriter(dateipfad)) {
            writer.write(sb.toString());
            System.out.println("LaTeX-Dokument erfolgreich exportiert nach: " + dateipfad);
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben der LaTeX-Datei: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
