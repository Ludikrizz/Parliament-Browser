package org.v_04_01.dataprocessing;

import de.tudarmstadt.ukp.dkpro.core.api.io.ProgressMeter;
import org.bson.BsonNull;
import org.bson.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.v_04_01.database.MongoDBHandler;
import org.v_04_01.dataprocessing.datastructure.Rede;
import org.v_04_01.dataprocessing.datastructure.imlementation.*;
import org.v_04_01.dataprocessing.helper.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataFactory {

    private Webscraper ws;
    private MongoDBHandler mDB;
    private NLPAnalyzer nlp;
    private double downloadProgress;

    public DataFactory() {
        ws = new Webscraper();
        mDB = new MongoDBHandler();
        nlp = new NLPAnalyzer();

    }

    /**
     * Lädt die Stammdaten der Abgeordneten herunter und speichert sie in der
     * Datenbank
     * 
     * @throws RuntimeException Fehler beim Verbinden mit der Website oder beim
     *                          Parsen des XML-Dokument
     */
    public void parseBaseDataRepresentative() {
        HashSet<Abgeordneter_File_Impl> abgeordnete = new HashSet<>();
        CreateAbgeordneter cA = new CreateAbgeordneter(abgeordnete);
        InputStream is = null;
        try {
            SAXReader reader = new SAXReader();
            is = ws.getBaseDataRepresentative();
            org.dom4j.Document document = reader.read(is);
            Element root = document.getRootElement();

            for (Iterator<Element> iterator = root.elementIterator("MDB"); iterator.hasNext();) {
                cA.create(iterator.next());
            }
        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                System.out.println("Error while closing InputStream" + e.getMessage());
            }
        }
        mDB.initDBForNewRepresentatives();
        mDB.addAbgeordnete(abgeordnete);
    }

    /**
     * Verarbeitet die XML-Datei einer Sitzung und speichert die Reden in der
     * Datenbank
     * 
     * @param is          InputStream der XML-Datei
     * @param reden       Set von Reden
     * @param abgeordnete Set von Abgeordneten
     * @throws DocumentException Fehler beim Parsen des XML-Dokuments
     */
    private void parseXMLSitzungen(InputStream is, HashSet<Rede> reden, HashSet<Abgeordneter_File_Impl> abgeordnete)
            throws DocumentException {
        CreateRede cr;
        DateConverter dc = new DateConverter();
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(is);

        Element root = document.getRootElement();
        Node wpCurrentNode = root.selectSingleNode("vorspann/kopfdaten/plenarprotokoll-nummer");

        int counter = 1;

        Wahlperiode_Impl wahlperiode = new Wahlperiode_Impl(wpCurrentNode.numberValueOf("wahlperiode").intValue());
        Node sitzungsverlauf = root.selectSingleNode("sitzungsverlauf");
        Sitzung_File_Impl sitzungFile = new Sitzung_File_Impl(
                dc.convert(root.valueOf("vorspann/kopfdaten/veranstaltungsdaten/datum/@date")),
                dc.convertTime(root.valueOf("@sitzung-start-uhrzeit")),
                dc.convertTime(root.valueOf("@sitzung-ende-uhrzeit")),
                root.numberValueOf("vorspann/kopfdaten/sitzungstitel/sitzungsnr").intValue(),
                wahlperiode);
        cr = new CreateRede(abgeordnete, sitzungFile);
        if (sitzungsverlauf instanceof Element) {
            for (Iterator<Element> iterator = ((Element) sitzungsverlauf)
                    .elementIterator("tagesordnungspunkt"); iterator.hasNext();) {
                Element tagesordnungspunkt = iterator.next();
                Tagesordnung_File_Impl tagesordnung = new Tagesordnung_File_Impl(
                        counter++,
                        sitzungFile,
                        tagesordnungspunkt.valueOf("@top-id"));
                System.out.println("Verarbeite Tagesordnungspunkt " + tagesordnung.getTitel());
                sitzungFile.addTagesordnungspunkt(tagesordnung);
                for (Iterator<Element> iteratorReden = tagesordnungspunkt.elementIterator("rede"); iteratorReden
                        .hasNext();) {
                    Rede_File_Impl rede = cr.create(iteratorReden.next(), tagesordnung);
                    if (rede != null) {
                        tagesordnung.addRede(rede);
                        reden.add(rede);
                    }
                }
            }
        }
    }

    /**
     * Gibt die Anzahl der Protokolle zurück, die in der Datenbank fehlen aus der 20
     * (aktuellen) Wahlperiode
     * 
     * @return Anzahl der fehlenden Protokolle
     */
    public int missingProtocols() {
        int protocolsInDB = mDB.aggregate(Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("sitzung.wahlperiode", 20)),
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("field1", "$sitzung.wahlperiode")
                                        .append("field2", "$sitzung.sitzungsnummer"))),
                new Document()
                        .append("$group", new Document()
                                .append("_id", new BsonNull())
                                .append("totalGroups", new Document()
                                        .append("$sum", 1)))),
                "reden").get(0).getInteger("totalGroups");
        int protocolsInWeb = 0;
        try {
            protocolsInWeb = ws.getPlenaryProtocolCount(20);
        } catch (IOException e) {
            System.out.println("Error while getting protocol count from web" + e.getMessage());
            return -1;
        }
        return protocolsInWeb - protocolsInDB;
    }

    public long missingProcessedSpeeches() {
        return mDB.count(new Document().append("nlp_text", new Document().append("$exists", false)), "reden");
    }

    /**
     * Fügt die fehlenden Protokolle in die Datenbank ein. Einschließlich der
     * Protokolle der 19 Wahlperiode, sofern diese fehlen
     * 
     */
    public void addMissingPrototols() {
        downloadProgress = 0.0;
        HashSet<Rede> reden = new HashSet<>();
        HashSet<Abgeordneter_File_Impl> abgeordnete = new HashSet<>();
        List<Document> downloadedSpeeches = mDB.aggregate(Arrays.asList(
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("wahlperiode", "$sitzung.wahlperiode")
                                        .append("sitzungsnummer", "$sitzung.sitzungsnummer"))
                                .append("uniqueDocument", new Document()
                                        .append("$first", "$$ROOT.sitzung"))),
                new Document()
                        .append("$replaceRoot", new Document()
                                .append("newRoot", "$uniqueDocument")),
                new Document()
                        .append("$sort", new Document()
                                .append("wahlperiode", 1.0)
                                .append("sitzungsnummer", 1.0))),
                "reden");
        int counter = 0;
        int missingProtocols = missingProtocols();
        for (int wp = 19; wp <= 20; wp++) {
            try {
                for (int i = 1; i <= ws.getPlenaryProtocolCount(wp); i++) {

                    if (!downloadedSpeeches.isEmpty() && downloadedSpeeches.get(0).getInteger("wahlperiode") == wp
                            && downloadedSpeeches.get(0).getInteger("sitzungsnummer") == i) {
                        downloadedSpeeches.remove(0);
                    } else {
                        InputStream is = ws.getPlenaryProtocol(wp, i);
                        if (is != null) {
                            try {
                                parseXMLSitzungen(is, reden, abgeordnete);
                                counter++;
                                downloadProgress = (double) counter / missingProtocols;
                            } catch (DocumentException e) {
                                System.out.println("Error while parsing XML" + e.getMessage());
                            } finally {
                                is.close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error while getting protocol count from web" + e.getMessage());
            }
        }
        mDB.addReden(reden);
        abgeordnete.forEach(
                abgeordneter -> mDB.update(abgeordneter));
        try {
            nlp.analyzeMany(reden);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verarbeitet Reden die noch nicht analysiert wurden und speichert die Analyse
     * in der Datenbank
     * 
     */
    public void addUnprocessedSpeeches() {
        HashSet<Rede> reden = new HashSet<>();
        mDB.read(new Document().append("nlp_text", new Document().append("$exists", false)), "reden").forEach(
                document -> reden.add(new Rede_MongoDB_Impl(document)));
        try {
            nlp.analyzeMany(reden);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Gibt den Fortschritt des Downloads zurück
     * 
     * @return Fortschritt des Downloads als double
     */
    public double getDownloadProgress() {
        return downloadProgress;
    }

    /**
     * Gibt den Fortschritt des NLP-Prozesses zurück
     * 
     * @return Fortschritt des NLP-Prozesses als int in Prozent
     */
    public int getProcessProgress() {
        if (nlp.getProgressMeter() == null) {
            return 0;
        }
        return nlp.getProgressMeter().getPercentage();

    }
}
