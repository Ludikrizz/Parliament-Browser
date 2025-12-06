package org.v_04_01.dataprocessing.datastructure.imlementation;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.hucompute.textimager.uima.type.CategorizedSentiment;
import org.hucompute.textimager.uima.type.CategorizedSentiment_Type;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.v_04_01.dataprocessing.datastructure.Kommentar;
import org.v_04_01.dataprocessing.datastructure.Rede;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

public class Rede_NLP_Impl extends Rede_Abstract_Impl implements Rede {

    private JCas jCas;

    private int laenge;

    public Rede_NLP_Impl(JCas jcas, Rede rede) {
        this.jCas = jcas;
        this.id = rede.getID();
        this.text = rede.getText();
        this.laenge = rede.getLaenge();
        this.abgeordneter = rede.getAbgeordneter();
        this.sitzung = rede.getSitzung();
        this.kommentare = rede.getKommentare();
        this.tagesordnung = rede.getTagesordnung();
    }

    public JCas getJCas() {
        return jCas;
    }

    @Override
    public int getLaenge() {
        return laenge;
    }

}
