package org.group_04_01.dataprocessing.datastructure.imlementation;

import org.group_04_01.dataprocessing.datastructure.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.TreeSet;

public class Abgeordneter_File_Impl extends Abgeordneter_Abstract_Impl implements Abgeordneter {

    /**
     * Erzeugt ein Objekt vom Typ Abgeordneter fuer das Einlesen aus einem File
     * 
     * @param id           die ID des Abgeordneten
     * @param name         der Name des Abgeordneten
     * @param vorname      der Vorname des Abgeordneten
     * @param ortszusatz   der Ortszusatz des Abgeordneten
     * @param adelssuffix  das Adelssuffix des Abgeordneten
     * @param anrede       die Anrede des Abgeordneten
     * @param akadTitel    der akademische Titel des Abgeordneten
     * @param geburtsdatum das Geburtsdatum des Abgeordneten
     * @param geburtsort   der Geburtsort des Abgeordneten
     * @param sterbedatum  das Sterbedatum des Abgeordneten
     * @param geschlecht   das Geschlecht des Abgeordneten
     * @param religion     die Religion des Abgeordneten
     * @param beruf        der Beruf des Abgeordneten
     * @param vita         die Vita des Abgeordneten
     * @param partei       die Partei des Abgeordneten
     */
    public Abgeordneter_File_Impl(int id, String name, String vorname, String ortszusatz, String adelssuffix,
            String anrede,
            String akadTitel, Date geburtsdatum, String geburtsort, Date sterbedatum, Types.GESCHLECHT geschlecht,
            String religion, String beruf, String vita, Partei_Impl partei) {
        this.id = id;
        this.name = name;
        this.vorname = vorname;
        this.ortszusatz = ortszusatz;
        this.adelssuffix = adelssuffix;
        this.anrede = anrede;
        this.akadTitel = akadTitel;
        this.geburtsdatum = geburtsdatum;
        this.geburtsort = geburtsort;
        this.sterbedatum = sterbedatum;
        this.geschlecht = geschlecht;
        this.religion = religion;
        this.beruf = beruf;
        this.vita = vita;
        this.partei = partei;
        mandate = new HashSet<>();
        reden = new HashSet<>();
        fraktionsMitgliedschaften = new HashSet<>();
        pictures = new TreeSet<>();
        institutionen = new HashSet<>();
    }

    /**
     * Erzeugt ein Objekt vom Typ Abgeordneter fuer das Einlesen der Reden vom
     * Server
     * 
     * @param id die ID des Abgeordneten
     * @author Kester Rumke
     */
    public Abgeordneter_File_Impl(int id, String name, String vorname) {
        this.id = id;
        this.name = name;
        this.vorname = vorname;
        reden = new HashSet<>();
    }

    /**
     * Fügt dem Abgeordneten ein Mandat hinzu
     * 
     * @param mandat das Mandat, das hinzugefügt werden soll
     */
    public void addMandat(Mandat mandat) {
        mandate.add(mandat);
    }

    /**
     * Fügt dem Abgeordneten eine Rede hinzu
     * 
     * @param rede die Rede, die hinzugefügt werden soll
     */
    public void addRede(Rede rede) {
        reden.add(rede);
    }

    /**
     * Fügt dem Abgeordneten eine Fraktionsmitgliedschaft hinzu
     * 
     * @param fm die Fraktionsmitgliedschaft, die hinzugefügt werden soll
     */
    public void addFraktionsMitgiedschaft(HashSet<FraktionsMitgliedschaft_Impl> fm) {
        fraktionsMitgliedschaften.addAll(fm);
    }

    /**
     * Fügt dem Abgeordneten ein Bild hinzu
     * 
     * @param picture das Bild, das hinzugefügt werden soll
     * @author Kester Rumke
     */
    public void addPicture(Picture picture) {
        pictures.add(picture);
    }

    /**
     * Fügt dem Abgeordneten eine Menge von Bildern hinzu
     * 
     * @param pictures die Menge von Bildern, die hinzugefügt werden soll
     * @author Kester Rumke
     */
    public void addPictures(HashSet<Picture> pictures) {
        this.pictures.addAll(pictures);
    }

    /**
     * Fügt dem Abgeordneten eine Menge von Institutionen hinzu
     * 
     * @param institutionen die Menge von Institutionen, die hinzugefügt werden soll
     */
    public void addInstitutionen(HashSet<Institution> institutionen) {
        this.institutionen.addAll(institutionen);
    }
}
