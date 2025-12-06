package org.v_04_01.dataprocessing.helper;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Eine Hilfsklasse die aus einem String ein sql.Date erstellt
 * 
 * @author Kester Rumke
 */
public class DateConverter {

    private String dateFormat;

    /**
     * Erzeugt ein Objekt vom Typ DateConverter mit dem Standardformat "dd.MM.yyyy"
     */
    public DateConverter() {
        dateFormat = "dd.MM.yyyy";
    }

    /**
     * Erzeugt ein Objekt vom Typ DateConverter mit dem übergebenen Format
     * 
     * @param dateFormat ein Objekt vom Typ String, das das Format enthält
     */
    public DateConverter(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Die Methode wandelt den String in ein sql.Date um. Wenn die Eingabe falsch
     * ist, wird null zurueck gegeben
     * 
     * @param date ein Objekt vom Typ String, der das Datum enthaelt
     * @return gibt ein Objekt vom Typ sql.Date zurueck oder null
     */
    public Date convert(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            long milliseconds = sdf.parse(date).getTime();
            return new Date(milliseconds);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Die Methode wandelt den String in ein sql.Time um. Wenn die Eingabe falsch
     * ist, wird null zurueck gegeben
     * 
     * @param time ein Objekt vom Typ String, der die Uhrzeit enthaelt
     * @return gibt ein Objekt vom Typ sql.Time zurueck oder null
     */
    public Time convertTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        time = time.replace(".", ":");
        try {
            long milliseconds = sdf.parse(time).getTime();
            return new Time(milliseconds);
        } catch (ParseException e) {
            return null;
        }
    }
}
