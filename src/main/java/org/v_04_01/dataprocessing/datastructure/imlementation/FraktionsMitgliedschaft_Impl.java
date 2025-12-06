package org.v_04_01.dataprocessing.datastructure.imlementation;

import java.sql.Date;

import org.v_04_01.dataprocessing.datastructure.Abgeordneter;
import org.v_04_01.dataprocessing.datastructure.Fraktion;
import org.v_04_01.dataprocessing.datastructure.FraktionsMitgliedschaft;
import org.v_04_01.dataprocessing.datastructure.Wahlperiode;

public class FraktionsMitgliedschaft_Impl implements FraktionsMitgliedschaft {

    private Abgeordneter abgeordneter;
    private Date fromDate;
    private Date toDate;
    private Fraktion fraktion;
    private Wahlperiode wahlperiode;

    /**
     * Erzeugt ein Objekt vom Typ FraktionsMitgliedschaft fuer das Einlesen aus
     * einem File
     * 
     * @param abgeordneter der Abgeordnete, der der Fraktion beigetreten ist
     * @param fromDate     das Datum, an dem der Abgeordnete der Fraktion
     *                     beigetreten ist
     * @param toDate       das Datum, an dem der Abgeordnete der Fraktion
     *                     ausgetreten ist
     * @param fraktion     die Fraktion, der der Abgeordnete beigetreten ist
     * @param wahlperiode  die Wahlperiode, in der der Abgeordnete der Fraktion
     *                     beigetreten ist
     */
    public FraktionsMitgliedschaft_Impl(Abgeordneter abgeordneter, Date fromDate,
            Date toDate, Fraktion_Impl fraktion, Wahlperiode_Impl wahlperiode) {
        this.abgeordneter = abgeordneter;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fraktion = fraktion;
        this.wahlperiode = wahlperiode;
    }

    @Override
    public Abgeordneter getAbgeordneter() {
        return abgeordneter;
    }

    @Override
    public Date fromDate() {
        return fromDate;
    }

    @Override
    public Date toDate() {
        return toDate;
    }

    @Override
    public Fraktion getFraktion() {
        return fraktion;
    }

    @Override
    public Wahlperiode getWahlperiode() {
        return wahlperiode;
    }

    @Override
    public int compareTo(FraktionsMitgliedschaft o) {
        int firstComparator = wahlperiode.compareTo(o.getWahlperiode());
        if (firstComparator == 0) {
            return fraktion.compareTo(o.getFraktion());
        }
        return firstComparator;
    }

}
