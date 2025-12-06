package org.v_04_01.dataprocessing.datastructure.imlementation;

import java.sql.Date;

import org.v_04_01.dataprocessing.datastructure.Institution;

public class Institution_Impl implements Institution {

    private String instArt;
    private String titel;
    private String funktion;
    private Date startDate;

    public Institution_Impl(String instArt, String titel, String funktion, Date startDate) {
        this.instArt = instArt;
        this.titel = titel;
        this.funktion = funktion;
        this.startDate = startDate;
    }

    @Override
    public String getInstArt() {
        return instArt;
    }

    @Override
    public String getTitel() {
        return titel;
    }

    @Override
    public String getFunktion() {
        return funktion;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public int compareTo(Institution o) {
        return titel.compareTo(o.getTitel());
    }
}
