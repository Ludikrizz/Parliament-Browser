package org.v_04_01.dataprocessing.helper;

import de.tudarmstadt.ukp.dkpro.core.api.io.ProgressMeter;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.InvalidXMLException;
import org.bson.Document;
import org.dkpro.core.io.xmi.XmiWriter;
import org.texttechnologylab.DockerUnifiedUIMAInterface.DUUIComposer;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIRemoteDriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIUIMADriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.io.DUUIAsynchronousProcessor;
import org.texttechnologylab.DockerUnifiedUIMAInterface.io.reader.DUUIFileReader;
import org.texttechnologylab.DockerUnifiedUIMAInterface.lua.DUUILuaContext;
import org.texttechnologylab.DockerUnifiedUIMAInterface.pipeline_storage.sqlite.DUUISqliteStorageBackend;
import org.v_04_01.database.MongoDBHandler;
import org.v_04_01.dataprocessing.datastructure.Rede;
import org.v_04_01.dataprocessing.datastructure.imlementation.Rede_MongoDB_Impl;
import org.v_04_01.dataprocessing.datastructure.imlementation.Rede_NLP_Impl;
import org.v_04_01.dataprocessing.helper.io.Deserializer;
import org.v_04_01.dataprocessing.helper.io.ParBrowReader;
import org.v_04_01.dataprocessing.helper.io.ParBrowWriter;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

public class NLPAnalyzer {

        private DUUIAsynchronousProcessor asynchronousProcessor;

        /**
         * Analysiert ein Set von Reden mit dem Reader und Writer. Dieser Teil läuft im
         * Programm
         * 
         * @param reden Set von Reden
         * @throws Exception Fehler
         */
        public void analyzeMany(HashSet<Rede> reden) throws Exception {
                LinkedList<Rede> redenList = new LinkedList<>(reden);

                asynchronousProcessor = new DUUIAsynchronousProcessor(new ParBrowReader(redenList));

                DUUILuaContext ctx = new DUUILuaContext().withJsonLibrary();
                int iWorkers = 20;

                DUUIComposer composer = new DUUIComposer()
                                .withSkipVerification(true)
                                .withLuaContext(ctx)
                                .withWorkers(iWorkers);

                DUUIRemoteDriver remoteDriver = new DUUIRemoteDriver();
                DUUIUIMADriver uimaDriver = new DUUIUIMADriver();

                composer.addDriver(uimaDriver, remoteDriver);
                composer.add(new DUUIRemoteDriver.Component("http://localhost:1000")
                                .withScale(iWorkers).build());

                composer.add(new DUUIRemoteDriver.Component("http://localhost:1001")
                                .withScale(iWorkers).build());

                composer.add(new DUUIRemoteDriver.Component("http://localhost:1002")
                                .withScale(iWorkers).build());
                composer.add(new DUUIUIMADriver.Component(createEngineDescription(ParBrowWriter.class))
                                .build());

                composer.run(asynchronousProcessor, "ParBrowReader");
                composer.shutdown();
        }

        /**
         * Gibt den Fortschritt des asynchronen Prozessors zurück
         * 
         * @return ein ProgressMeter der den Fortschritt des asynchronen Prozessors
         *         darstellt
         */
        public ProgressMeter getProgressMeter() {
                if (asynchronousProcessor == null) {
                        return null;
                }
                return asynchronousProcessor.getProgress();
        }

        /**
         * Analysiert eine Rede und nutzt den Writer.
         * 
         * @deprecated Nutze analyzeMany (nur für Testzwecke)
         * @param rede Rede
         * @throws Exception Fehler
         */
        public void analyzeOne(Rede rede) throws Exception {

                // do a check if already analyzed

                DUUILuaContext ctx = new DUUILuaContext().withJsonLibrary();
                int iWorkers = 1;

                DUUIComposer composer = new DUUIComposer()
                                .withSkipVerification(true)
                                .withLuaContext(ctx)
                                .withWorkers(iWorkers);

                DUUIRemoteDriver remoteDriver = new DUUIRemoteDriver();
                DUUIUIMADriver uimaDriver = new DUUIUIMADriver();

                composer.addDriver(uimaDriver, remoteDriver);
                composer.add(new DUUIRemoteDriver.Component("http://192.168.178.70:1000")
                                .withScale(iWorkers).build());

                composer.add(new DUUIRemoteDriver.Component("http://192.168.178.70:1001")
                                .withScale(iWorkers).build());

                composer.add(new DUUIRemoteDriver.Component("http://192.168.178.70:1002")
                                .withScale(iWorkers).build());
                composer.add(new DUUIUIMADriver.Component(createEngineDescription(ParBrowWriter.class))
                                .build());

                JCas jCas = rede.getJCas();
                DocumentMetaData dmd;
                dmd = DocumentMetaData.create(jCas);
                dmd.setDocumentId(rede.getID());
                dmd.setDocumentUri("src/main/resources/data/speeches/" + rede.getID());
                composer.run(jCas);

        }

}
