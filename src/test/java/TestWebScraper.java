import org.bson.Document;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;
import org.v_04_01.database.MongoDBHandler;
import org.v_04_01.dataprocessing.DataFactory;
import org.v_04_01.dataprocessing.datastructure.Rede;
import org.v_04_01.dataprocessing.datastructure.imlementation.Abgeordneter_File_Impl;
import org.v_04_01.dataprocessing.datastructure.imlementation.Abgeordneter_MongoDB_Impl;
import org.v_04_01.dataprocessing.datastructure.imlementation.Rede_File_Impl;
import org.v_04_01.dataprocessing.datastructure.imlementation.Rede_MongoDB_Impl;
import org.v_04_01.dataprocessing.helper.NLPAnalyzer;
import org.v_04_01.dataprocessing.helper.Webscraper;

import java.io.IOException;
import java.util.HashSet;

public class TestWebScraper {

    @Test
    public void testGetPicture() {
        Webscraper ws = new Webscraper();
        try {
            ws.getPictureData("Abelein", "Manfred", 123456789);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseBaseDataRepresentative() {
        DataFactory df = new DataFactory();
        df.parseBaseDataRepresentative();
    }

    @Test
    public void testParseMissingSpeeches() {
        DataFactory df = new DataFactory();
        for (int i = 0; i < 5; i++) {

            df.addMissingPrototols();
        }
    }

    @Test
    public void TestParseUnanalysedSpeeches() {
        HashSet<Rede> reden = new HashSet<>();
        MongoDBHandler mDB = new MongoDBHandler();
        mDB.read(new Document().append("entities", new Document().append("$exists", false)), "reden").forEach(
                rede -> reden.add(new Rede_MongoDB_Impl(rede)));
        NLPAnalyzer nlp = new NLPAnalyzer();
        try {
            nlp.analyzeMany(reden);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseReden() {
        DataFactory df = new DataFactory();
        Webscraper ws = new Webscraper();
        MongoDBHandler mDB = new MongoDBHandler();
        HashSet<Rede> reden = new HashSet<>();
        HashSet<Abgeordneter_File_Impl> abgeordnete = new HashSet<>();
        try {
            df.parseXMLSitzungen(ws.getPlenaryProtocol(19, 130), reden, abgeordnete);
        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        }
        mDB.addReden(reden);
    }

    @Test
    public void downloadPrimaryPictures() {
        MongoDBHandler mDB = new MongoDBHandler();
        HashSet<Abgeordneter_MongoDB_Impl> abgeordnete = new HashSet<>();
        mDB.read(new Document(), "abgeordnete").forEach(
                abgeordneter -> abgeordnete.add(new Abgeordneter_MongoDB_Impl(abgeordneter)));
        for (Abgeordneter_MongoDB_Impl ab : abgeordnete) {
            ab.getPrimaryPicture().getLocalURL();
        }
    }
}
