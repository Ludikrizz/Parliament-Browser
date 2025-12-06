import org.bson.Document;
import org.dom4j.DocumentException;
import org.group_04_01.database.MongoDBHandler;
import org.group_04_01.dataprocessing.DataFactory;
import org.group_04_01.dataprocessing.datastructure.Abgeordneter;
import org.group_04_01.dataprocessing.datastructure.Rede;
import org.group_04_01.dataprocessing.datastructure.imlementation.Abgeordneter_File_Impl;
import org.group_04_01.dataprocessing.datastructure.imlementation.Abgeordneter_MongoDB_Impl;
import org.group_04_01.dataprocessing.datastructure.imlementation.Rede_File_Impl;
import org.group_04_01.dataprocessing.datastructure.imlementation.Rede_MongoDB_Impl;
import org.group_04_01.dataprocessing.helper.NLPAnalyzer;
import org.group_04_01.dataprocessing.helper.Webscraper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;

public class TestMongoDBClasses {

    MongoDBHandler mDB = new MongoDBHandler();

    @Test
    public void testAbgeordneter() {
        Document ab = mDB.read(new Document().append("_id", 11004378), "abgeordnete").get(0);
        Abgeordneter abgeordneter = new Abgeordneter_MongoDB_Impl(ab);
        System.out.println(abgeordneter.getVorname());
    }

    @Test
    public void testRede() {
        Document ab = mDB.read(new Document().append("_id", "ID2014500500"), "reden").get(0);
        Rede_MongoDB_Impl rede = new Rede_MongoDB_Impl(ab);
    }

    @Test
    public void testNLLP() {
        NLPAnalyzer nlp = new NLPAnalyzer();
        try {
            // nlp.analyzeOneFromDB("ID1913001500");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNLLPMany() {
        DataFactory df = new DataFactory();
        Webscraper ws = new Webscraper();
        MongoDBHandler mDB = new MongoDBHandler();
        HashSet<Rede> reden = new HashSet<>();
        HashSet<Abgeordneter_File_Impl> abgeordnete = new HashSet<>();
        NLPAnalyzer nlp = new NLPAnalyzer();
        try {
            df.parseXMLSitzungen(ws.getPlenaryProtocol(19, 100), reden, abgeordnete);
            mDB.addReden(reden);
            nlp.analyzeMany(reden);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
