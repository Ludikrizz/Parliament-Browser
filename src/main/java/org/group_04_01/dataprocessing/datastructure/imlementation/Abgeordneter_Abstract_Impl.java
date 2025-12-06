package org.group_04_01.dataprocessing.datastructure.imlementation;

import org.group_04_01.dataprocessing.datastructure.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public abstract class Abgeordneter_Abstract_Impl implements Abgeordneter {

    protected int id;
    protected String name;
    protected String vorname;
    protected String ortszusatz;
    protected String adelssuffix;
    protected String anrede;
    protected String akadTitel;
    protected Date geburtsdatum;
    protected String geburtsort;
    protected Date sterbedatum;
    protected Types.GESCHLECHT geschlecht;
    protected String religion;
    protected String beruf;
    protected String vita;
    protected Set<Mandat> mandate;
    protected Partei partei;
    protected Set<Rede> reden;
    protected Set<FraktionsMitgliedschaft> fraktionsMitgliedschaften;
    protected TreeSet<Picture> pictures;
    protected HashSet<Institution> institutionen;

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVorname() {
        return vorname;
    }

    @Override
    public String getOrtszusatz() {
        return ortszusatz;
    }

    @Override
    public String getAdelssuffix() {
        return adelssuffix;
    }

    @Override
    public String getAnrede() {
        return anrede;
    }

    @Override
    public String getAkadTitel() {
        return akadTitel;
    }

    @Override
    public Date getGeburtsDatum() {
        return geburtsdatum;
    }

    @Override
    public String getGeburtsOrt() {
        return geburtsort;
    }

    @Override
    public Date getSterbeDatum() {
        return sterbedatum;
    }

    @Override
    public Types.GESCHLECHT getGeschlecht() {
        return geschlecht;
    }

    @Override
    public String getReligion() {
        return religion;
    }

    @Override
    public String getBeruf() {
        return beruf;
    }

    @Override
    public String getVita() {
        return vita;
    }

    @Override
    public Set<Mandat> listMandate() {
        return mandate;
    }

    @Override
    public Set<Mandat> listMandate(Wahlperiode wahlperiode) {
        HashSet<Mandat> mandatsInPeriod = new HashSet<>();
        for (Mandat mandat : mandate) {
            if (mandat.getWahlperiode() == wahlperiode) {
                mandatsInPeriod.add(mandat);
            }
        }
        return mandatsInPeriod;
    }

    @Override
    public boolean hasMandat(Wahlperiode wahlperiode) {
        for (Mandat mandat : mandate) {
            if (mandat.getWahlperiode() == wahlperiode) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Partei getPartei() {
        return partei;
    }

    @Override
    public Set<Rede> listReden() {
        return reden;
    }

    @Override
    public Set<FraktionsMitgliedschaft> listFraktionsmitgliedschaften() {
        return fraktionsMitgliedschaften;
    }

    @Override
    public Set<Picture> listPictures() {
        return pictures;
    }

    @Override
    public Picture getPrimaryPicture() {
        if (pictures.isEmpty()) {
            return new Picture_Impl("data/pictures/placeholder.jpg", "", "", "", "", 0);
        }
        return pictures.first();
    }

    @Override
    public HashSet<Institution> listInstitutionen() {
        return institutionen;
    }

    @Override
    public int compareTo(Abgeordneter o) {
        return Integer.compare(id, o.getID());
    }
}
