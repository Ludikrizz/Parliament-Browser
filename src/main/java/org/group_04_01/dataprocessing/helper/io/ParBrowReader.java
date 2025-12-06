package org.group_04_01.dataprocessing.helper.io;

import de.tudarmstadt.ukp.dkpro.core.api.io.ProgressMeter;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.util.CasCopier;
import org.group_04_01.dataprocessing.datastructure.Rede;
import org.group_04_01.dataprocessing.datastructure.imlementation.Rede_NLP_Impl;
import org.texttechnologylab.DockerUnifiedUIMAInterface.io.DUUICollectionReader;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Liest Reden aus einer Queue und gibt sie an den UIMA-Reader weiter
 */
public class ParBrowReader implements DUUICollectionReader {

    private ConcurrentLinkedQueue<Rede> reden;
    private ConcurrentLinkedQueue<Rede_NLP_Impl> loadedSpeeches;
    private ProgressMeter progressMeter;
    private long size;
    private AtomicInteger processedSpeeches;

    /**
     * Erzeugt ein Objekt vom Typ ParBrowReader.
     * Lädt die Reden in eine Parallele Queue und erzeugt fuer die Reden schon das
     * JCas
     * 
     * @param redenList Liste von Reden
     */
    public ParBrowReader(LinkedList<Rede> redenList) {
        loadedSpeeches = new ConcurrentLinkedQueue<>();
        this.reden = new ConcurrentLinkedQueue<>(redenList);
        this.size = redenList.size();
        progressMeter = new ProgressMeter(size);
        processedSpeeches = new AtomicInteger(0);

        Runnable r = () -> {
            while (!reden.isEmpty()) {
                System.out.println("Reden in Queue: " + reden.size());
                if (loadedSpeeches.size() < 20) {
                    try {
                        Rede rede = reden.poll();
                        loadedSpeeches.add(new Rede_NLP_Impl(rede.getJCas(), rede));
                    } catch (UIMAException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    /**
     * Gibt den ProgressMeter zurück
     * 
     * @return gibt den ProgressMeter zurück
     */
    @Override
    public ProgressMeter getProgress() {
        return progressMeter;
    }

    /**
     * Holt das nächtste JCas aus der Queue. Setzt die Metadaten und zählt den
     * ProgressMeter hoch
     * 
     * @param jCas JCas, in die die Rede geladen wird
     */
    @Override
    public void getNextCas(JCas jCas) {
        jCas.reset();
        Rede_NLP_Impl loadedSpeech = loadedSpeeches.poll();

        if (loadedSpeech == null && !reden.isEmpty()) {
            try {
                Rede tempRede = this.reden.poll();
                loadedSpeech = new Rede_NLP_Impl(tempRede.getJCas(), tempRede);
            } catch (UIMAException e) {
                throw new RuntimeException(e);
            }
        }

        int currentNum = processedSpeeches.addAndGet(1);

        CasCopier.copyCas(loadedSpeech.getJCas().getCas(), jCas.getCas(), true, false);
        DocumentMetaData dmd = DocumentMetaData.create(jCas);

        dmd.setDocumentId(loadedSpeech.getID());
        dmd.setDocumentBaseUri(
                "src/main/resources/data/speeches/" + loadedSpeech.getSitzung().getWahlperiode().getNumber()
                        + loadedSpeech.getSitzung().getSitzungsnummer());
        dmd.setDocumentUri(dmd.getDocumentBaseUri() + "/" + loadedSpeech.getID());

        progressMeter.setDone(currentNum);
        progressMeter.setLeft(size - currentNum);

        System.out.println("Rede " + loadedSpeech.getID() + " wird analysiert");
    }

    /**
     * Gibt an, ob noch Reden in der Queue sind
     * 
     * @return gibt true zurück, wenn noch Reden in der Queue sind, sonst false
     */
    @Override
    public boolean hasNext() {
        return !reden.isEmpty();
    }

    /**
     * Gibt die Größe der Queue zurück
     * 
     * @return gibt die Größe der Queue zurück
     */
    @Override
    public long getSize() {
        return size;
    }

    /**
     * Gibt die Anzahl der bereits verarbeiteten Reden zurück
     * 
     * @return gibt die Anzahl der bereits verarbeiteten Reden zurück
     */
    @Override
    public long getDone() {
        return processedSpeeches.get();
    }
}
