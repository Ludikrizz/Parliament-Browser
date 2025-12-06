package org.v_04_01.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.v_04_01.dataprocessing.datastructure.*;
import org.v_04_01.dataprocessing.datastructure.imlementation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBHandler {

    private MongoDatabase database;

    /**
     * Erzeugt ein Objekt von MongoDBConnectionHandler, welches die Verbindung zur
     * MongoDB aufbaut.
     * Die Verbindungsdaten werden aus der Datei auth.txt gelesen.
     * 
     */
    public MongoDBHandler() {
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get("src/main/resources/auth.txt")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String connectionString = "mongodb://" + properties.getProperty("remote_user") + ":"
                + properties.getProperty("remote_password") + "@" +
                properties.getProperty("remote_host") + ":" + properties.getProperty("remote_port") + "/"
                + properties.getProperty("remote_database");
        String databaseName = properties.getProperty("remote_database");

        try {
            MongoClient mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);
        } catch (Exception e) {
            System.out.println("Could not connect to MongoDB:" + e.getMessage());
        }

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.WARNING);

    }

    /**
     * Löscht die Collection Abgeordnete aus der DB.
     * Sollte ausgeführt werden, bevor die Stammdaten eingelesen werden
     * 
     */
    public void initDBForNewRepresentatives() {
        database.getCollection("abgeordnete").drop();
    }

    /**
     * Fügt ein Objekt von Rede in die Collection reden ein.
     * 
     * @param rede ein Objekt von Rede, dass in die Datenbank eingefügt werden soll
     */
    public void add(Rede rede) {
        Document document = getDocumentFromRede(rede);
        database.getCollection("reden").insertOne(document);
    }

    /**
     * Fügt ein Objekt von Abgeordneter in die Collection abgeordnete ein.
     * 
     * @param abgeordneter ein Objekt von Abgeordneter, dass in die Datenbank
     *                     eingefügt werden soll
     */
    public void add(Abgeordneter abgeordneter) {
        Document document = getDocumentFromAbgeordneter(abgeordneter);
        database.getCollection("abgeordnete").insertOne(document);
    }

    public void add(UUID sessionID, String user) {
        database.getCollection("sessions").updateMany(eq("user", user),
                new Document("$set", new Document("deprecated", true)));
        Document document = new Document("_id", sessionID)
                .append("user", user)
                .append("date", new java.sql.Date(System.currentTimeMillis()));
        database.getCollection("sessions").insertOne(document);
    }

    public void add(Document document, String collection) {
        database.getCollection(collection).insertOne(document);
    }

    /**
     * Fügt eine Liste von Objekten von Abgeordneter in die Collection abgeordnete
     * ein.
     * 
     * @param abgeordnete ein HashSet von Objekten von Abgeordneter, dass in die
     *                    Datenbank eingefügt werden soll
     */
    public void addAbgeordnete(HashSet<Abgeordneter_File_Impl> abgeordnete) {
        List<Document> documents = new LinkedList<>();
        for (Abgeordneter abgeordneter : abgeordnete) {
            documents.add(getDocumentFromAbgeordneter(abgeordneter));
        }
        database.getCollection("abgeordnete").insertMany(documents);
    }

    /**
     * Fügt eine Liste von Objekten von Rede in die Collection reden ein.
     * 
     * @param reden ein HashSet von Objekten von Rede, dass in die Datenbank
     *              eingefügt werden soll
     */
    public void addReden(HashSet<Rede> reden) {
        List<Document> documents = new LinkedList<>();
        for (Rede rede : reden) {
            documents.add(getDocumentFromRede(rede));
        }
        database.getCollection("reden").insertMany(documents);
        database.getCollection("reden").createIndex(new Document("text", "text"));
    }

    /**
     * Löscht ein Objekt von Abgeordneter aus der Collection abgeordnete.
     * Die ID der Abgeordneten wird als Suchkriterium verwendet.
     * 
     * @param abgeordneter das Objekt von Abgeordneter, dass aus der Datenbank
     *                     gelöscht werden soll
     */
    public boolean delete(Abgeordneter abgeordneter) {
        return database.getCollection("abgeordnete")
                .deleteOne(eq("_id", abgeordneter.getID())).wasAcknowledged();
    }

    /**
     * Löscht ein Objekt von Rede aus der Collection reden.
     * Die ID der Rede wird als Suchkriterium verwendet.
     * 
     * @param rede das Objekt von Rede, dass aus der Datenbank gelöscht werden soll
     */
    public boolean delete(Rede rede) {
        return database.getCollection("reden")
                .deleteOne(eq("_id", rede.getID())).wasAcknowledged();
    }

    public boolean delete(Bson filter, String collection) {
        return database.getCollection(collection).deleteOne(filter).wasAcknowledged();
    }

    public boolean deleteMany(Bson filter, String collection) {
        return database.getCollection(collection).deleteMany(filter).wasAcknowledged();
    }

    /**
     * Aktualisiert ein Objekt von Abgeordneter in der Collection abgeordnete.
     * Dazu wird die ID des Abgeordneten als Suchkriterium verwendet und das Objekt
     * mit den neuen Werten ersetzt.
     * 
     * @param abgeordneter das Objekt von Abgeordneter, dass in der Datenbank
     *                     aktualisiert werden soll
     * @return true, wenn das Objekt aktualisiert wurde, false, wenn nicht
     */
    public boolean update(Abgeordneter abgeordneter) {
        return database.getCollection("abgeordnete")
                .replaceOne(eq("_id", abgeordneter.getID()), getDocumentFromAbgeordneter(abgeordneter))
                .wasAcknowledged();
    }

    /**
     * Aktualisiert ein Objekt von Rede in der Collection reden.
     * Dazu wird die ID der Rede als Suchkriterium verwendet und das Objekt mit den
     * neuen Werten ersetzt.
     * 
     * @param rede das Objekt von Rede, dass in der Datenbank aktualisiert werden
     *             soll
     * @return true, wenn das Objekt aktualisiert wurde, false, wenn nicht
     */
    public boolean update(Rede rede) {
        return database.getCollection("reden")
                .replaceOne(eq("_id", rede.getID()), getDocumentFromRede(rede)).wasAcknowledged();
    }

    public boolean update(Abgeordneter_File_Impl abgeordneter) {
        if (!database.getCollection("abgeordnete")
                .updateOne(eq("_id", abgeordneter.getID()),
                        new Document("$push", getRedenIDFromAbgeordneter(abgeordneter)))
                .wasAcknowledged()) {
            database.getCollection("abgeordnete").insertOne(createDummyAbgeordneter(abgeordneter));
        }
        return true;
    }

    /**
     * Fuegt einer Rede in der Datenbank ein Document hinzu.
     * Wird verwendet, um die NLP-Analyse Daten zu speichern
     * 
     * @param document das Document, um das die Rede ergänzt werden soll
     * @param id       die ID der Rede, die ergänzt werden soll
     * @return true, wenn das Document hinzugefügt wurde, false, wenn nicht
     */
    public boolean update(Document document, String id) {
        return database.getCollection("reden")
                .updateOne(eq("_id", id), new Document("$set", document)).wasAcknowledged();
    }

    /**
     * Eine generelle Methode zum updaten von Dokumenten in einer Collection
     * 
     * @param filter     das Filterobjekt
     * @param document   das Dokument, dass eingefügt werden soll
     * @param collection der Name der Collection, in der das Dokument eingefügt
     *                   werden soll
     * @return true, wenn das Dokument eingefügt wurde, false, wenn nicht
     */
    public boolean update(Bson filter, Document document, String collection) {
        return database.getCollection(collection).updateOne(filter, document).wasAcknowledged();
    }

    public boolean update(UUID sessionID) {
        return database.getCollection("sessions")
                .updateOne(eq("_id", sessionID), new Document("$set", new Document("deprecated", true)))
                .wasAcknowledged();
    }

    public boolean updateMany(Bson filter, Document document, String collection) {
        return database.getCollection(collection).updateMany(filter, document).wasAcknowledged();
    }

    public boolean replace(Bson filter, Document document, String collection) {
        return database.getCollection(collection).replaceOne(filter, document).wasAcknowledged();
    }

    /**
     * Liest alle Objekte aus einer Collection, die den Suchkriterien entsprechen.
     * 
     * @param query      ein Objekt von Document, dass die Suchkriterien enthält
     * @param projection ein Objekt von Document, dass die Projektion enthält
     * @param sort       ein Objekt von Document, dass die Sortierung enthält
     * @param collection der Name der Collection, aus der die Objekte gelesen werden
     *                   sollen
     * @return eine Liste von Objekten vom Typ Document, die den Suchkriterien
     *         entsprechen
     */
    public List<Document> read(Document query, Document projection, Document sort, String collection) {
        return database.getCollection(collection).find(query).projection(projection).sort(sort)
                .into(new LinkedList<>());
    }

    /**
     * Liest alle Objekte aus einer Collection, die den Suchkriterien entsprechen.
     * 
     * @param query      ein Objekt von Document, dass die Suchkriterien enthält
     * @param projection ein Objekt von Document, dass die Projektion enthält
     * @param sort       ein Objekt von Document, dass die Sortierung enthält
     * @param limit      das Limit der Dokumente, die gelesen werden sollen
     * @param collection der Name der Collection, aus der die Objekte gelesen werden
     *                   sollen
     * @return eine Liste von Objekten vom Typ Document, die den Suchkriterien
     *         entsprechen
     */
    public List<Document> read(Document query, Document projection, Document sort, int limit, String collection) {
        return database.getCollection(collection).find(query).limit(limit).projection(projection).sort(sort)
                .into(new LinkedList<>());
    }

    /**
     * Liest alle Objekte aus einer Collection, die den Suchkriterien entsprechen.
     * 
     * @param query      ein Objekt von Document, dass die Suchkriterien enthält
     * @param projection ein Objekt von Document, dass die Projektion enthält
     * @param sort       ein Objekt von Document, dass die Sortierung enthält
     * @param skip       die Anzahl der Dokumente, die übersprungen werden sollen
     * @param limit      das Limit der Dokumente, die gelesen werden sollen
     * @param collection der Name der Collection, aus der die Objekte gelesen werden
     *                   sollen
     * @return eine Liste von Objekten vom Typ Document, die den Suchkriterien
     *         entsprechen
     */
    public List<Document> read(Document query, Document projection, Document sort, int skip, int limit,
            String collection) {
        return database.getCollection(collection).find(query).skip(skip).limit(limit).projection(projection).sort(sort)
                .into(new LinkedList<>());
    }

    /**
     * Liest alle Objekte aus einer Collection, die den Suchkriterien entsprechen.
     * 
     * @param query      ein Objekt von Document, dass die Suchkriterien enthält
     * @param projection ein Objekt von Document, dass die Projektion enthält
     * @param collection der Name der Collection, aus der die Objekte gelesen werden
     *                   sollen
     * @return eine Liste von Objekten vom Typ Document, die den Suchkriterien
     *         entsprechen
     */
    public List<Document> read(Document query, Document projection, String collection) {
        return database.getCollection(collection).find(query).projection(projection).into(new LinkedList<>());
    }

    /**
     * Liest alle Objekte aus einer Collection, die den Suchkriterien entsprechen.
     * 
     * @param query      ein Objekt von Document, dass die Suchkriterien enthält
     * @param collection der Name der Collection, aus der die Objekte gelesen werden
     *                   sollen
     * @return eine Liste von Objekten vom Typ Document, die den Suchkriterien
     *         entsprechen
     */
    public List<Document> read(Document query, String collection) {
        return database.getCollection(collection).find(query).into(new LinkedList<>());
    }

    /**
     * Liest alle Objekte aus einer Collection, die den Suchkriterien entsprechen.
     * 
     * @param query      ein Objekt von Document, dass die Suchkriterien enthält
     * @param limit      das Limit der Dokumente, die gelesen werden sollen
     * @param collection der Name der Collection, aus der die Objekte gelesen werden
     *                   sollen
     * @return eine Liste von Objekten vom Typ Document, die den Suchkriterien
     *         entsprechen
     */
    public List<Document> read(Document query, int limit, String collection) {
        return database.getCollection(collection).find(query).limit(limit).into(new LinkedList<>());
    }

    /**
     * Liest alle Objekte aus einer Collection, die den Suchkriterien entsprechen.
     * 
     * @param query      ein Objekt von Document, dass die Suchkriterien enthält
     * @param skip       die Anzahl der Dokumente, die übersprungen werden sollen
     * @param limit      das Limit der Dokumente, die gelesen werden sollen
     * @param collection der Name der Collection, aus der die Objekte gelesen werden
     *                   sollen
     * @return eine Liste von Objekten vom Typ Document, die den Suchkriterien
     *         entsprechen
     */
    public List<Document> read(Document query, int skip, int limit, String collection) {
        return database.getCollection(collection).find(query).skip(skip).limit(limit).into(new LinkedList<>());
    }

    /**
     * Liest alle Objekte aus einer Collection, die den Suchkriterien entsprechen
     * und gibt die Anzahl der Dokumente an
     * 
     * @param query      ein Objekt von Document, dass die Suchkriterien enthält
     * @param collection der Name der Collection, aus der die Objekte gelesen werden
     *                   sollen
     * @return die Anzahl der Dokumente, die den Suchkriterien entsprechen als long
     */
    public long count(Document query, String collection) {
        return database.getCollection(collection).countDocuments(query);
    }

    /**
     * Liest alle Objekte aus der Datenbank, die die aggregation erfüllen.
     * 
     * @param pipeline   ein Objekt von Document, dass die pipeline der aggregation
     *                   enthält
     * @param collection der Name der Collection, aus der die Objekte gelesen werden
     *                   sollen
     * @return eine Liste von Objekten vom Typ Document, die den Suchkriterien
     *         entsprechen
     */
    public List<Document> aggregate(List<Bson> pipeline, String collection) {
        return database.getCollection(collection).aggregate(pipeline).into(new LinkedList<>());
    }

    /**
     * Wandelt ein Objekt von Abgeordneter in ein Objekt von Document um.
     * Private Hilfsmethode, die von den add und update Methoden verwendet wird.
     * 
     * @param abgeordneter das Objekt von Abgeordneter, dass in ein Objekt von
     *                     Document umgewandelt werden soll
     * @return ein Objekt von Document, dass das Objekt von Abgeordneter
     *         repräsentiert
     */
    private Document getDocumentFromAbgeordneter(Abgeordneter abgeordneter) {
        return new Document("_id", abgeordneter.getID())
                .append("nachname", abgeordneter.getName())
                .append("vorname", abgeordneter.getVorname())
                .append("ortszusatz", abgeordneter.getOrtszusatz())
                .append("adelssuffix", abgeordneter.getAdelssuffix())
                .append("anrede", abgeordneter.getAnrede())
                .append("akademischer_titel", abgeordneter.getAkadTitel())
                .append("geburtsdatum", abgeordneter.getGeburtsDatum().toString())
                .append("geburtsort", abgeordneter.getGeburtsOrt())
                .append("sterbedatum",
                        abgeordneter.getSterbeDatum() != null ? abgeordneter.getSterbeDatum().toString() : "")
                .append("geschlecht", abgeordneter.getGeschlecht().toString())
                .append("religion", abgeordneter.getReligion())
                .append("beruf", abgeordneter.getBeruf())
                .append("vita", abgeordneter.getVita())
                .append("partei", abgeordneter.getPartei() != null ? abgeordneter.getPartei().getName() : "")
                .append("mandate", abgeordneter.listMandate().stream().map(
                        mandat -> new Document("wahlperiode", mandat.getWahlperiode().getNumber())
                                .append("mandatsart", mandat.getTyp().toString())
                                .append("start_date", mandat.getWahlperiode().getStartDate().toString())
                                .append("end_date",
                                        mandat.getWahlperiode().getEndeDate() != null
                                                ? mandat.getWahlperiode().getEndeDate().toString()
                                                : ""))
                        .collect(Collectors.toList()))
                .append("fraktionsmitgliedschaften", abgeordneter.listFraktionsmitgliedschaften().stream().map(
                        fraktionsMitgliedschaft -> new Document("fraktion",
                                fraktionsMitgliedschaft.getFraktion().getName())
                                .append("start_date", fraktionsMitgliedschaft.fromDate().toString())
                                .append("end_date",
                                        fraktionsMitgliedschaft.toDate() != null
                                                ? fraktionsMitgliedschaft.toDate().toString()
                                                : "")
                                .append("wahlperiode", fraktionsMitgliedschaft.getWahlperiode().getNumber()))
                        .collect(Collectors.toList()))
                .append("pictures", abgeordneter.listPictures().stream().map(
                        picture -> new Document("local_url", picture.getLocalURLBackend())
                                .append("remote_url", picture.getRemoteURL())
                                .append("location", picture.getLocation())
                                .append("date", picture.getDate())
                                .append("photographer", picture.getPhotographer())
                                .append("priority", picture.getPriority()))
                        .collect(Collectors.toList()))
                .append("institutionen", abgeordneter.listInstitutionen().stream().map(
                        institution -> new Document("inst_art", institution.getInstArt())
                                .append("titel", institution.getTitel())
                                .append("funktion", institution.getFunktion())
                                .append("start_date",
                                        institution.getStartDate() != null ? institution.getStartDate().toString()
                                                : ""))
                        .collect(Collectors.toList()));
    }

    /**
     * Wandelt ein Objekt von Rede in ein Objekt von Document um.
     * Private Hilfsmethode, die von den add und update Methoden verwendet wird.
     * 
     * @param rede das Objekt von Rede, dass in ein Objekt von Document umgewandelt
     *             werden soll
     * @return ein Objekt von Document, dass das Objekt von Rede repräsentiert
     */
    private Document getDocumentFromRede(Rede rede) {
        return new Document("_id", rede.getID())
                .append("abgeordneter", rede.getAbgeordneter().getID())
                .append("text", rede.getText())
                .append("laenge", rede.getLaenge())
                .append("sitzung", new Document("sitzungsnummer", rede.getSitzung().getSitzungsnummer())
                        .append("date", rede.getSitzung().getDate().toString())
                        .append("sitzungsbeginn", rede.getSitzung().getSitzungsbeginn().toString())
                        .append("sitzungsende", rede.getSitzung().getSitzungsende().toString())
                        .append("dauer", rede.getSitzung().getDauer())
                        .append("wahlperiode", rede.getSitzung().getWahlperiode().getNumber()))
                .append("agenda", new Document("agenda_nr", rede.getTagesordnung().getTagesordnungspunkt())
                        .append("titel", rede.getTagesordnung().getTitel()))
                .append("kommentare", rede.getKommentare().stream().map(
                        kommentar -> new Document("text", kommentar.getText())
                                .append("pos", kommentar.getPos())
                                .append("fraktionen", kommentar.getFraktionen().stream().map(
                                        Fraktion::getName).collect(Collectors.toList()))
                                .append("abgeordneter",
                                        kommentar.getAbgeordneter() != null ? kommentar.getAbgeordneter().getID() : ""))
                        .collect(Collectors.toList()));
    }

    private Document getRedenIDFromAbgeordneter(Abgeordneter abgeordneter) {
        return new Document("reden_ids", new Document("$each", abgeordneter.listReden().stream().map(
                Rede::getID).collect(Collectors.toList())));
    }

    private Document createDummyAbgeordneter(Abgeordneter abgeordneter) {
        return new Document("_id", abgeordneter.getID())
                .append("nachname", abgeordneter.getName())
                .append("vorname", abgeordneter.getVorname())
                .append("vita",
                        "Dieser Abgeordnete wurde angelegt, da er in einem Protokoll eine Rede hielt, aber nicht in den Stammdaten vorhanden ist. "
                                +
                                "Womöglich wurde eine Falsche ID verwendet oder es handelt sich um einen Gastredner")
                .append("reden_ids", abgeordneter.listReden().stream().map(
                        Rede::getID).collect(Collectors.toList()));
    }
}
