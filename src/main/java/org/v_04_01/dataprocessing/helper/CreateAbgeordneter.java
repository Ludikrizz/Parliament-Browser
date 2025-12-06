package org.v_04_01.dataprocessing.helper;

import org.dom4j.Element;
import org.dom4j.Node;
import org.v_04_01.dataprocessing.datastructure.Institution;
import org.v_04_01.dataprocessing.datastructure.Types;
import org.v_04_01.dataprocessing.datastructure.imlementation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;

public class CreateAbgeordneter {

    private HashSet<Abgeordneter_File_Impl> abgeordnete;
    private HashSet<Fraktion_Impl> fraktionen;
    private HashSet<Wahlperiode_Impl> wahlperioden;
    private HashSet<Partei_Impl> parteien;
    private HashSet<Mandat_Impl> mandate;
    private Webscraper ws;

    public CreateAbgeordneter(HashSet<Abgeordneter_File_Impl> abgeordnete) {
        this.abgeordnete = abgeordnete;
        fraktionen = new HashSet<>();
        wahlperioden = new HashSet<>();
        parteien = new HashSet<>();
        mandate = new HashSet<>();
        ws = new Webscraper();
    }

    /**
     * Erstellt ein Abgeordneten-Objekt aus einem XML-Element und fuegt dieses der
     * Liste der Abgeordneten hinzu.
     * 
     * @param element das XML-Element, aus dem der Abgeordnete erstellt werden soll
     */
    public void create(Element element) {
        List<Node> namen = element.selectNodes("NAMEN/NAME");
        Node biografischeAngaben = element.selectSingleNode("BIOGRAFISCHE_ANGABEN");
        List<Node> wahlperiodenXML = element.selectNodes("WAHLPERIODEN/WAHLPERIODE");

        Partei_Impl partei = getParteiXML(biografischeAngaben.valueOf("PARTEI_KURZ"));

        DateConverter dc = new DateConverter();

        if (namen.size() == 1) {
            Abgeordneter_File_Impl abgeordneter = new Abgeordneter_File_Impl(
                    element.numberValueOf("ID").intValue(),
                    namen.get(0).valueOf("NACHNAME"),
                    namen.get(0).valueOf("VORNAME"),
                    namen.get(0).valueOf("ORTSZUSATZ"),
                    namen.get(0).valueOf("ADEL"),
                    namen.get(0).valueOf("ANREDE_TITEL"),
                    namen.get(0).valueOf("AKAD_TITEL"),
                    dc.convert(biografischeAngaben.valueOf("GEBURTSDATUM")),
                    biografischeAngaben.valueOf("GEBURTSORT"),
                    dc.convert(biografischeAngaben.valueOf("STERBEDATUM")),
                    biografischeAngaben.valueOf("GESCHLECHT").equals("weiblich") ? Types.GESCHLECHT.WEIBLICH
                            : Types.GESCHLECHT.MAENNLICH,
                    biografischeAngaben.valueOf("RELIGION"),
                    biografischeAngaben.valueOf("BERUF"),
                    biografischeAngaben.valueOf("VITA_KURZ"),
                    partei);
            makeDataFromWahlperiodenXML(abgeordneter, wahlperiodenXML);

            if (partei != null) {
                partei.addMember(abgeordneter);
            }
        }

    }

    /**
     * Liest Daten aus den Wahlperioden-XML-Elementen und erstellt daraus Mandate,
     * Fraktionsmitgliedschaften und Institutionen.
     * 
     * @param abgeordneter der Abgeordnete, fuer den die Daten erstellt werden
     *                     sollen
     * @param wp           die Liste der Wahlperioden-XML-Elemente, aus denen die
     *                     Daten erstellt werden sollen
     */
    private void makeDataFromWahlperiodenXML(Abgeordneter_File_Impl abgeordneter, List<Node> wp) {
        boolean wasMemberInWp = false;
        for (Node nodeWp : wp) {
            if (nodeWp.numberValueOf("WP").intValue() == 20 || nodeWp.numberValueOf("WP").intValue() == 19) {
                wasMemberInWp = true;
                Wahlperiode_Impl wpCurrentlyActive = getWahlperiodeFromXML(nodeWp);
                Mandat_Impl mandat = getMandatFromXML(nodeWp, wpCurrentlyActive, abgeordneter);
                if (mandat != null) {
                    abgeordneter.addMandat(mandat);
                    wpCurrentlyActive.addMandat(mandat);
                    mandate.add(mandat);
                }
                HashSet<Institution> institutionenAbgeordneter = new HashSet<>();
                HashSet<FraktionsMitgliedschaft_Impl> mitgliedschaften = makeMitgliedschaftenFromInstitutionXML(
                        nodeWp.selectNodes("INSTITUTIONEN/INSTITUTION"), abgeordneter, wpCurrentlyActive,
                        institutionenAbgeordneter);
                if (!mitgliedschaften.isEmpty()) {
                    abgeordneter.addFraktionsMitgiedschaft(mitgliedschaften);
                }
                if (!institutionenAbgeordneter.isEmpty()) {
                    abgeordneter.addInstitutionen(institutionenAbgeordneter);
                }
            }

        }
        if (wasMemberInWp) {
            try {
                abgeordneter.addPictures(
                        ws.getPictureData(abgeordneter.getName(), abgeordneter.getVorname(), abgeordneter.getID()));
            } catch (IOException e) {
                System.out.println("Bild von" + abgeordneter.getName() + " " + abgeordneter.getVorname()
                        + "konnte nicht geladen werden" + e.getMessage());
            }
            abgeordnete.add(abgeordneter);
            System.out
                    .println("Abgeordneter :" + abgeordneter.getName() + " " + abgeordneter.getVorname() + " erstellt");
        }
    }

    /**
     * Gibt die Wahlperiode zurueck, die der uebergebenen Nummer entspricht.
     * Existiert diese noch nicht, wird sie neu erstellt und zur Liste der
     * Wahlperioden hinzugefuegt.
     * 
     * @param nodeWp das XML-Element, aus dem die Wahlperiode erstellt werden soll
     * @return ein Objekt vom Typ Wahlperiode
     */
    private Wahlperiode_Impl getWahlperiodeFromXML(Node nodeWp) {
        DateConverter dc = new DateConverter();
        for (Wahlperiode_Impl wahlperiode : wahlperioden) {
            if (wahlperiode.getNumber() == nodeWp.numberValueOf("WP").intValue()) {
                return wahlperiode;
            }
        }

        Wahlperiode_Impl wpCurrentlyActive = new Wahlperiode_Impl(
                nodeWp.numberValueOf("WP").intValue(),
                dc.convert(nodeWp.valueOf("MDBWP_VON")),
                dc.convert(nodeWp.valueOf("MDBWP_BIS")));
        wahlperioden.add(wpCurrentlyActive);
        return wpCurrentlyActive;
    }

    /**
     * Gibt das Mandat des Abgeordneten zurueck, das aus dem uebergebenen
     * XML-Element erstellt wurde.
     * 
     * @param nodeWp            das XML-Element, aus dem das Mandat erstellt werden
     *                          soll
     * @param wpCurrentlyActive die Wahlperiode, in der das Mandat erstellt wurde
     * @param abgeordneter      der Abgeordnete, fuer den das Mandat erstellt wird
     * @return ein Objekt vom Typ Mandat oder null, falls das Mandat nicht erstellt
     *         werden konnte
     */
    private Mandat_Impl getMandatFromXML(Node nodeWp, Wahlperiode_Impl wpCurrentlyActive,
            Abgeordneter_File_Impl abgeordneter) {
        if (nodeWp.valueOf("MANDATSART").equals("Direktwahl")) {
            return new Mandat_Impl(abgeordneter, Types.MANDAT.DIREKTWAHL, wpCurrentlyActive);
        } else if (nodeWp.valueOf("MANDATSART").equals("Landesliste")) {
            return new Mandat_Impl(abgeordneter, Types.MANDAT.LANDESLISTE, wpCurrentlyActive);
        }
        return null;
    }

    /**
     * Erstellt Fraktionsmitgliedschaften und Institutionen aus den uebergebenen
     * XML-Elementen und gibt diese zurueck.
     * 
     * @param institutionen             die Liste der XML-Elemente, aus denen die
     *                                  Fraktionsmitgliedschaften und Institutionen
     *                                  erstellt werden sollen
     * @param abgeordneter              der Abgeordnete, fuer den die
     *                                  Fraktionsmitgliedschaften und Institutionen
     *                                  erstellt werden
     * @param wpCurrentlyActive         die Wahlperiode, in der die
     *                                  Fraktionsmitgliedschaften und Institutionen
     *                                  erstellt wurden
     * @param institutionenAbgeordneter die Liste der Institutionen, die dem
     *                                  Abgeordneten zugeordnet werden sollen
     * @return eine Liste von Fraktionsmitgliedschaften, die aus den uebergebenen
     *         XML-Elementen erstellt wurden
     */
    private HashSet<FraktionsMitgliedschaft_Impl> makeMitgliedschaftenFromInstitutionXML(List<Node> institutionen,
            Abgeordneter_File_Impl abgeordneter,
            Wahlperiode_Impl wpCurrentlyActive,
            HashSet<Institution> institutionenAbgeordneter) {
        HashSet<FraktionsMitgliedschaft_Impl> mitgliedschaften = new HashSet<>();
        DateConverter dc = new DateConverter();
        for (Node institution : institutionen) {
            Fraktion_Impl fratkion;
            if (institution.valueOf("INSART_LANG").contains("Fraktion")) {
                fratkion = getFraktionFromName(institution.valueOf("INS_LANG"));
                fratkion.addMember(abgeordneter);

                Date startDate = dc.convert(institution.valueOf("FKTINS_VON"));
                if (startDate == null) {
                    startDate = wpCurrentlyActive.getStartDate();
                }
                Date endDate = dc.convert(institution.valueOf("FKTINS_BIS"));
                if (endDate == null) {
                    endDate = wpCurrentlyActive.getEndeDate();
                }

                mitgliedschaften.add(new FraktionsMitgliedschaft_Impl(
                        abgeordneter,
                        startDate,
                        endDate,
                        fratkion,
                        wpCurrentlyActive));
            } else {
                institutionenAbgeordneter.add(new Institution_Impl(
                        institution.valueOf("INSART_LANG"),
                        institution.valueOf("INS_LANG"),
                        institution.valueOf("FKT_LANG"),
                        dc.convert(institution.valueOf("MDBINS_VON"))));
            }
        }
        return mitgliedschaften;
    }

    /**
     * Gibt die Fraktion zurueck, die den uebergebenen Namen hat. Existiert diese
     * noch nicht, wird sie neu erstellt und zur Liste der Fraktionen hinzugefuegt.
     * 
     * @param name der Name der Fraktion
     * @return ein Objekt vom Typ Fraktion oder null, falls die Fraktion nicht
     *         erstellt werden konnte
     */
    private Fraktion_Impl getFraktionFromName(String name) {
        for (Fraktion_Impl fraktion : fraktionen) {
            if (fraktion.getName().equals(name)) {
                return fraktion;
            }
        }
        Fraktion_Impl fraktion = new Fraktion_Impl(name);
        fraktionen.add(fraktion);
        return fraktion;
    }

    /**
     * Gibt die Partei des Abgeordneten zurueck anhand des uebergebenen Kuerzels,
     * sofern diese existiert. Tut Sie das nicht, wird diese neu erstellt und zu der
     * Liste der Parteien hinzugefuegt.
     * 
     * @param partyShort das Kuerzel aus der XML-Datei
     * @return ein Objekt vom Typ Partei oder null falls der Abgeordnete zu keiner
     *         Partei gehoert
     */
    private Partei_Impl getParteiXML(String partyShort) {
        if (partyShort.isEmpty()) {
            return null;
        }
        for (Partei_Impl partei : parteien) {
            if (partei.getName().equals(partyShort)) {
                return partei;
            }
        }

        Partei_Impl partei = new Partei_Impl(partyShort);
        parteien.add(partei);
        return partei;
    }

}
