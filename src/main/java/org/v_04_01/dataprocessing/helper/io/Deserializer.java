package org.v_04_01.dataprocessing.helper.io;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.SerialFormat;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.CasIOUtils;
import org.texttechnologylab.utilities.helper.TempFileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import static org.texttechnologylab.utilities.helper.ArchiveUtils.decompressGZ;

public class Deserializer {

    /**
     * Deserialisiert ein JCas-Objekt aus einer .xmi.gz-Datei
     * 
     * @param file die .xmi.gz-Datei
     * @return das JCas-Objekt
     * @throws IOException   Fehler beim Lesen der Datei
     * @throws UIMAException Fehler beim Erstellen des JCas-Objekts
     */
    public JCas deserialize(File file) throws IOException, UIMAException {
        File tempFile = TempFileHandler.getTempFileName(file.getName().replace(".gz", ""));
        decompressGZ(Paths.get(file.getAbsolutePath()), Paths.get(tempFile.getAbsolutePath()));

        InputStream inputStream = new FileInputStream(tempFile);

        JCas jCas = JCasFactory.createJCas();
        CasIOUtils.load(inputStream, jCas.getCas());
        inputStream.close();
        tempFile.delete();
        return jCas;
    }
}
