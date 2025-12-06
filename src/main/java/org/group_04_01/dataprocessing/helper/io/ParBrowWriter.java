package org.group_04_01.dataprocessing.helper.io;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.SerialFormat;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasIOUtils;
import org.bson.Document;
import org.dkpro.core.api.io.JCasFileWriter_ImplBase;
import org.group_04_01.database.MongoDBHandler;
import org.group_04_01.dataprocessing.datastructure.Kommentar;
import org.group_04_01.dataprocessing.datastructure.imlementation.Abgeordneter_MongoDB_Impl;
import org.group_04_01.dataprocessing.datastructure.imlementation.Fraktion_Impl;
import org.group_04_01.dataprocessing.datastructure.imlementation.Kommentar_Impl;
import org.hucompute.textimager.uima.type.CategorizedSentiment;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.texttechnologylab.utilities.helper.ArchiveUtils;
import org.texttechnologylab.utilities.helper.TempFileHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Writer für die die NLP-Analyse der Reden
 */
public class ParBrowWriter extends JCasFileWriter_ImplBase {

    private MongoDBHandler mDB;

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext);
        mDB = new MongoDBHandler();

    }

    /**
     * Schreibt die NLP-Analyse der Reden in die Datenbank und speichert die Reden
     * als xmi.gz
     * 
     * @param jCas JCas das zu speichern ist
     * @throws AnalysisEngineProcessException Fehler beim Speichern
     */
    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        DocumentMetaData dmd = DocumentMetaData.get(jCas);
        String id = dmd.getDocumentId();
        TreeSet<Kommentar> kommentare = new TreeSet<>();
        mDB.read(new Document().append("_id", id), new Document().append("kommentare", 1.0), "reden").get(0)
                .getList("kommentare", Document.class).forEach(
                        kommentar -> kommentare.add(new Kommentar_Impl(kommentar.getInteger("pos"),
                                kommentar.getString("text"),
                                kommentar.get("abgeordneter") instanceof Document
                                        ? new Abgeordneter_MongoDB_Impl(kommentar.get("abgeordneter", Document.class))
                                        : null,
                                new HashSet<>(kommentar.getList("fraktionen", String.class).stream().map(
                                        Fraktion_Impl::new).collect(Collectors.toList())))));

        Document document = new Document()
                .append("nlp_text", getText(jCas, kommentare))
                .append("topics", getTopicDistribution(jCas).entrySet().stream().map(
                        topic -> new Document("label", topic.getKey())
                                .append("score", topic.getValue()))
                        .collect(Collectors.toList()))
                .append("sentiments", getSentimentDistribution(jCas).entrySet().stream().map(
                        sentiment -> new Document("label", sentiment.getKey())
                                .append("score", sentiment.getValue()))
                        .collect(Collectors.toList()))
                .append("entities", getNamedEntitiesDistribution(jCas).entrySet().stream().map(
                        entity -> new Document("label", entity.getKey())
                                .append("count", entity.getValue()))
                        .collect(Collectors.toList()))
                .append("pos", getPOSDistribution(jCas).entrySet().stream().map(
                        pos -> new Document("label", pos.getKey())
                                .append("count", pos.getValue()))
                        .collect(Collectors.toList()));
        mDB.update(document, id);

        try {

            File folder = new File(dmd.getDocumentBaseUri());
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(dmd.getDocumentUri());
            if (!file.exists()) {
                file.createNewFile();
            }
            File pTempFile = TempFileHandler.getTempFile("aaa", ".temp");
            CasIOUtils.save(jCas.getCas(), new FileOutputStream(pTempFile), SerialFormat.XMI_1_1);
            ArchiveUtils.compressGZ(Paths.get(pTempFile.getAbsolutePath()),
                    Paths.get(file.getAbsolutePath() + ".xmi.gz"));
            pTempFile.delete();
            file.delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Rede " + id + " processed.");
    }

    /**
     * Hilfsmethode, die den Text fuer das frontend generiert
     * 
     * @param jCas       JCas der Rede
     * @param kommentare Kommentare der Rede
     * @return Text fuer das frontend
     */
    private String getText(JCas jCas, TreeSet<Kommentar> kommentare) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sentenceBuilder = new StringBuilder();

        int actualLength = 0;
        TreeSet<Kommentar> kommentareInMethod = new TreeSet<>(kommentare);

        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            LinkedList<Integer> position = new LinkedList<>();
            for (Kommentar kommentar : kommentareInMethod) {
                if (kommentar.getPos() - actualLength <= sentence.getCoveredText().length()) {
                    position.add(kommentar.getPos() - actualLength);
                }
            }
            sb.append("<p>");
            sentenceBuilder.append(sentence.getCoveredText());
            actualLength += sentenceBuilder.length() + 1;
            for (NamedEntity entity : JCasUtil.selectCovered(NamedEntity.class, sentence)) {
                String markClass = "<mark class=\"" + entity.getValue() + "_NLP\">";
                int indexOf = sentenceBuilder.indexOf(entity.getCoveredText());
                for (int i = 0; i < position.size(); i++) {
                    if (position.get(i) > indexOf) {
                        position.set(i, position.get(i) + markClass.length() + 7);
                    }
                }

                sentenceBuilder.insert(indexOf, markClass);
                sentenceBuilder.insert(
                        sentenceBuilder.indexOf(entity.getCoveredText()) + entity.getCoveredText().length(), "</mark>");

            }
            int offset = 0;
            for (Integer integer : position) {
                int oldLength = sentenceBuilder.length();
                sentenceBuilder.insert(integer + offset,
                        "<span class=\"kommentar\">" + kommentareInMethod.first().getText() + "</span>");
                offset += sentenceBuilder.length() - oldLength;
                kommentareInMethod.remove(kommentareInMethod.first());
            }

            sb.append(sentenceBuilder.toString());
            sentenceBuilder.delete(0, sentenceBuilder.length());
            sb.append("<span class=\"sentiment\">");
            for (Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
                sb.append("Sentiment: ");
                sb.append(sentiment.getSentiment());
            }
            sb.append("</span>");
            sb.append("</p>");
        }

        return sb.toString();
    }

    /**
     * Hilfsmethode, die die Named Entities der Rede in ein HashMap speichert
     * 
     * @param jCas JCas der Rede
     * @return HashMap mit den Named Entities und ihrer Verteilung
     */
    private HashMap<String, Integer> getNamedEntitiesDistribution(JCas jCas) {
        HashMap<String, Integer> namedEntities = new HashMap<>();
        for (NamedEntity entity : JCasUtil.select(jCas, NamedEntity.class)) {
            namedEntities.put(entity.getValue(), namedEntities.getOrDefault(entity.getValue(), 0) + 1);
        }
        return namedEntities;
    }

    /**
     * Hilfsmethode, die die POS-Tags der Rede in ein HashMap speichert
     * 
     * @param jCas JCas der Rede
     * @return HashMap mit den POS-Tags und ihrer Verteilung
     */
    private HashMap<String, Integer> getPOSDistribution(JCas jCas) {
        HashMap<String, Integer> pos = new HashMap<>();
        for (Token token : JCasUtil.select(jCas, Token.class)) {
            pos.put(token.getPosValue(), pos.getOrDefault(token.getPosValue(), 0) + 1);
        }
        return pos;
    }

    /**
     * Hilfsmethode, die die Sentiments der Rede in ein HashMap speichert
     * 
     * @param jCas JCas der Rede
     * @return HashMap mit den Sentiments und ihrer Verteilung
     */
    private HashMap<String, Double> getSentimentDistribution(JCas jCas) {
        HashMap<String, Double> sentiment = new HashMap<>();

        for (CategorizedSentiment pos : JCasUtil.select(jCas, CategorizedSentiment.class)) {
            sentiment.put("sentiment", pos.getSentiment());
            sentiment.put("pos", pos.getPos());
            sentiment.put("neu", pos.getNeu());
            sentiment.put("neg", pos.getNeg());
            break;
        }
        return sentiment;
    }

    /**
     * Hilfsmethode, die die Topics der Rede in ein HashMap speichert
     * 
     * @param jCas JCas der Rede
     * @return HashMap mit den Topics und ihrer Verteilung
     */
    private HashMap<String, Double> getTopicDistribution(JCas jCas) {
        HashMap<String, Double> topics = new HashMap<>();
        for (CategoryCoveredTagged topic : JCasUtil.select(jCas, CategoryCoveredTagged.class)) {
            double score;
            if (topics.containsKey(topic.getValue())) {
                score = (topics.get(topic.getValue()) + topic.getScore()) / 2;
            } else {
                score = topic.getScore();
            }
            topics.put(topic.getValue(), score);
        }
        return topics;
    }
}
