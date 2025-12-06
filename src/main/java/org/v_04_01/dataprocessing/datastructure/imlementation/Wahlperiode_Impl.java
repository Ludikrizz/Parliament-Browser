package org.v_04_01.dataprocessing.datastructure.imlementation;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.v_04_01.dataprocessing.datastructure.Mandat;
import org.v_04_01.dataprocessing.datastructure.Types;
import org.v_04_01.dataprocessing.datastructure.Wahlperiode;

public class Wahlperiode_Impl implements Wahlperiode {

    private int number;
    private Date startDate;
    private Date endDate;
    private HashSet<Mandat> mandate;

    /**
     * Erzeugt ein Objekt vom Typ Wahlperiode fuer das Einlesen aus einem File
     * 
     * @param number    die Nummer der Wahlperiode
     * @param startDate das Datum, an dem die Wahlperiode begonnen hat
     * @param endDate   das Datum, an dem die Wahlperiode geendet hat
     * 
     */
    public Wahlperiode_Impl(int number, Date startDate, Date endDate) {
        this.number = number;
        this.startDate = startDate;
        this.endDate = endDate;
        mandate = new HashSet<>();
    }

    /**
     * Erzeugt ein Objekt vom Typ Wahlperiode fuer das Einlesen der Reden vom Server
     * 
     * @param number die Nummer der Wahlperiode
     */
    public Wahlperiode_Impl(int number) {
        this.number = number;
    }

    /**
     * fügt der Wahlperiode ein Mandat hinzu
     * 
     * @param mandat das Mandat, das hinzugefügt werden soll
     */
    public void addMandat(Mandat mandat) {
        mandate.add(mandat);
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Date getEndeDate() {
        return endDate;
    }

    @Override
    public Set<Mandat> listMandate() {
        return mandate;
    }

    @Override
    public Set<Mandat> listMandate(Types.MANDAT mandatType) {
        HashSet<Mandat> mandateWithType = new HashSet<>();
        for (Mandat mandat : mandate) {
            if (mandat.getTyp() == mandatType) {
                mandateWithType.add(mandat);
            }
        }
        return mandateWithType;
    }

    @Override
    public int compareTo(Wahlperiode o) {
        return Integer.compare(number, o.getNumber());
    }

}
