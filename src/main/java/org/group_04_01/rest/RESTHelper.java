package org.group_04_01.rest;

import freemarker.template.Configuration;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.group_04_01.database.MongoDBHandler;
import org.group_04_01.dataprocessing.DataFactory;
import org.group_04_01.dataprocessing.datastructure.Group;
import org.group_04_01.dataprocessing.datastructure.Types;
import org.group_04_01.dataprocessing.datastructure.User;
import org.group_04_01.dataprocessing.datastructure.imlementation.*;
import org.group_04_01.dataprocessing.helper.DateConverter;
import org.group_04_01.dataprocessing.helper.NLPAnalyzer;
import org.group_04_01.dataprocessing.helper.Password;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static spark.Spark.staticFiles;

public class RESTHelper {

    public static Configuration cf = Configuration.getDefaultConfiguration();

    private static MongoDBHandler mDB = new MongoDBHandler();

    private static Password pwd = new Password();
    private static DataFactory df = new DataFactory();

    public static void init() throws IOException {

        MongoDBHandler mDB = new MongoDBHandler();

        Spark.port(4567);
        staticFiles.externalLocation("src/main/resources/public");
        Spark.init();

        cf.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));

        // @author Kimon Raschke
        Spark.post("/updateDataBubbleChart", (req, res) -> {
            String jsonString = req.queryParams("jsonData");

            // schreibt den json string in die datei
            try (FileWriter file = new FileWriter("src/main/resources/public/data/dataBubbleChart.json")) {
                file.write(jsonString);
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();

                return "Error";
            }

            return "updated successfully";
        });

        // @author Kimon Raschke
        Spark.post("/updateDataSunburst", (req, res) -> {
            String jsonString = req.queryParams("jsonData");

            // schreibt den json string in die datei
            try (FileWriter file = new FileWriter("src/main/resources/public/data/dataSunburst.json")) {
                file.write(jsonString);
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();

                return "Error";
            }

            return "updated successfully";
        });

        // @author Kimon Raschke
        Spark.post("/updateDataHorizontalBarChart", (req, res) -> {
            String jsonString = req.queryParams("jsonData");

            // schreibt den json string in die datei
            try (FileWriter file = new FileWriter("src/main/resources/public/data/dataHorizontalBarChart.json")) {
                file.write(jsonString);
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();

                return "Error";
            }

            return "updated successfully";
        });

        // @author Kimon Raschke
        Spark.post("/updateDataRadarChart", (req, res) -> {
            String jsonString = req.queryParams("jsonData");

            // schreibt den json string in die datei
            try (FileWriter file = new FileWriter("src/main/resources/public/data/dataRadarChart.json")) {
                file.write(jsonString);
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();

                return "Error";
            }

            return "updated successfully";
        });

        // @author Kimon Raschke
        Spark.post("/updateDataBarChart", (req, res) -> {
            String jsonString = req.queryParams("jsonData");

            // schreibt den json string in die datei
            try (FileWriter file = new FileWriter("src/main/resources/public/data/dataBarChart.json")) {
                file.write(jsonString);
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();

                return "Error";
            }

            return "updated successfully";
        });

        Spark.get("/abgeordnete", (req, res) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            Document session = getSessionIDRights(req.cookie("sessionID"));
            if (session != null) {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                extractRightsFromGroup(sessionGroup, attributes);
            }
            String name = req.queryParams("nachname");
            String vorname = req.queryParams("vorname");
            List<Abgeordneter_MongoDB_Impl> abgeordnete = new LinkedList<>();
            List<Document> parameter = new LinkedList<>();

            Document query = new Document();
            if (name != null && !name.isEmpty()) {
                query.append("nachname", name);
                parameter.add(new Document()
                        .append("name", "nachname")
                        .append("value", name));
            }
            if (vorname != null && !vorname.isEmpty()) {
                query.append("vorname", vorname);
                parameter.add(new Document()
                        .append("name", "vorname")
                        .append("value", vorname));
            }

            Document projection = new Document();
            projection.append("_id", 1.0);
            projection.append("nachname", 1.0);
            projection.append("vorname", 1.0);
            projection.append("partei", 1.0);
            projection.append("fraktionsmitgliedschaften", 1.0);

            Document sort = new Document();
            sort.append("partei", 1.0);
            sort.append("vorname", 1.0);
            sort.append("ortszusatz", 1.0);

            mDB.read(query, projection, sort, "abgeordnete")
                    .forEach(abgeordneter -> abgeordnete.add(new Abgeordneter_MongoDB_Impl(abgeordneter)));
            attributes.put("abgeordnete", abgeordnete);
            return new ModelAndView(attributes, "abgeordnete.ftl");
        }, new FreeMarkerEngine(cf));

        Spark.get("/abgeordnete/:id", (req, res) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            Document session = getSessionIDRights(req.cookie("sessionID"));
            if (session != null) {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                extractRightsFromGroup(sessionGroup, attributes);
            }
            Document abgeordneter = mDB.aggregate(Arrays.asList(
                    new Document()
                            .append("$match", new Document()
                                    .append("_id", Integer.parseInt(req.params(":id")))),
                    new Document()
                            .append("$lookup", new Document()
                                    .append("from", "reden")
                                    .append("localField", "reden_ids")
                                    .append("foreignField", "_id")
                                    .append("as", "reden_ids")),
                    new Document()
                            .append("$project", new Document()
                                    .append("reden_ids.text", 0.0)
                                    .append("reden_ids.nlp_text", 0.0)
                                    .append("reden_ids.pos", 0.0)
                                    .append("reden_ids.entities", 0.0)
                                    .append("reden_ids.topics", 0.0)
                                    .append("reden_ids.kommentare", 0.0)
                                    .append("reden_ids.sentiments", 0.0))),
                    "abgeordnete").get(0);
            attributes.put("abgeordneter", new Abgeordneter_MongoDB_Impl(abgeordneter));
            // attributes.put("test", new Rede_NLP_Impl());
            return new ModelAndView(attributes, "abgeordneter.ftl");
        }, new FreeMarkerEngine(cf));

        Spark.get("/", (req, res) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            Document session = getSessionIDRights(req.cookie("sessionID"));
            if (session != null) {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                extractRightsFromGroup(sessionGroup, attributes);
            }
            attributes.put("missingProtocols", df.missingProtocols());
            attributes.put("unprocessedSpeeches", df.missingProcessedSpeeches());
            return new ModelAndView(attributes, "home.ftl");
        }, new FreeMarkerEngine(cf));

        Spark.get("/reden", (req, res) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            Document session = getSessionIDRights(req.cookie("sessionID"));
            if (session != null) {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                extractRightsFromGroup(sessionGroup, attributes);
            }
            String text = req.queryParams("text");
            if (text != null) {
                LinkedList<Rede_MongoDB_Impl> reden = new LinkedList<>();
                mDB.aggregate(Arrays.asList(
                        new Document()
                                .append("$match", new Document()
                                        .append("$text", new Document()
                                                .append("$search", "\"" + text + "\""))),
                        new Document()
                                .append("$project", new Document()
                                        .append("score", new Document()
                                                .append("$meta", "textScore"))
                                        .append("abgeordneter", 1)
                                        .append("sitzung", 1)
                                        .append("agenda", 1)),
                        new Document()
                                .append("$lookup", new Document()
                                        .append("from", "abgeordnete")
                                        .append("localField", "abgeordneter")
                                        .append("foreignField", "_id")
                                        .append("as", "abgeordneter")),
                        new Document()
                                .append("$unwind", new Document()
                                        .append("path", "$abgeordneter")),
                        new Document()
                                .append("$sort", new Document()
                                        .append("score", -1))),
                        "reden").forEach(
                                rede -> reden.add(new Rede_MongoDB_Impl(rede)));
                attributes.put("reden", reden);
            } else {
                LinkedList<Sitzung_MongoDB_Impl> sitzungen = new LinkedList<>();
                mDB.aggregate(Arrays.asList(
                        new Document()
                                .append("$group", new Document()
                                        .append("_id", new Document()
                                                .append("field1", "$sitzung.wahlperiode")
                                                .append("field2", "$sitzung.sitzungsnummer"))
                                        .append("uniqueDocument", new Document()
                                                .append("$first", "$$ROOT.sitzung"))
                                        .append("agendas", new Document()
                                                .append("$addToSet", "$$ROOT.agenda"))),
                        new Document()
                                .append("$replaceRoot", new Document()
                                        .append("newRoot", new Document()
                                                .append("$mergeObjects", Arrays.asList(
                                                        "$uniqueDocument",
                                                        new Document()
                                                                .append("agendas", "$agendas")))))),
                        "reden").forEach(
                                sitzung -> sitzungen.add(new Sitzung_MongoDB_Impl(sitzung)));
                attributes.put("sitzungen", sitzungen);
            }
            return new ModelAndView(attributes, "reden.ftl");
        }, new FreeMarkerEngine(cf));

        Spark.get("/reden/:id", (req, res) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            Document session = getSessionIDRights(req.cookie("sessionID"));
            if (session != null) {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                extractRightsFromGroup(sessionGroup, attributes);
            }
            Rede_MongoDB_Impl rede = new Rede_MongoDB_Impl(mDB.aggregate(
                    Arrays.asList(
                            new Document()
                                    .append("$match", new Document()
                                            .append("_id", req.params(":id"))),
                            new Document()
                                    .append("$lookup", new Document()
                                            .append("from", "abgeordnete")
                                            .append("localField", "abgeordneter")
                                            .append("foreignField", "_id")
                                            .append("as", "abgeordneter")),
                            new Document()
                                    .append("$unwind", new Document()
                                            .append("path", "$abgeordneter"))),
                    "reden").get(0));
            attributes.put("rede", rede);
            return new ModelAndView(attributes, "rede.ftl");
        }, new FreeMarkerEngine(cf));

        Spark.get("/charts", (req, res) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            Document session = getSessionIDRights(req.cookie("sessionID"));
            if (session != null) {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                extractRightsFromGroup(sessionGroup, attributes);
            }
            return new ModelAndView(attributes, "charts.ftl");
        }, new FreeMarkerEngine(cf));

        Spark.get("/usersettings", (req, res) -> {
            Document session = getSessionIDRights(req.cookie("sessionID"));
            HashMap<String, Object> attributes = new HashMap<>();
            if (session == null) {
                res.status(401);
                res.redirect("/");
            } else {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                res.status(200);
                extractRightsFromGroup(sessionGroup, attributes);
                List<User> users = new LinkedList<>();
                mDB.read(new Document(), "users").forEach(
                        user -> users.add(new User_Impl(user)));
                attributes.put("users", users);
                List<Group> groups = new LinkedList<>();
                mDB.read(new Document(), "groups").forEach(
                        group -> groups.add(new Group_Impl(group)));
                attributes.put("groups", groups);
                attributes.put("allRights", Types.RIGHT.values());
            }

            return new ModelAndView(attributes, "settings.ftl");
        }, new FreeMarkerEngine(cf));

        Spark.get("/edit/abgeordneter", (req, res) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            Document session = getSessionIDRights(req.cookie("sessionID"));
            if (session == null) {
                res.status(401);
                res.redirect("/");
            } else {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                res.status(200);
                extractRightsFromGroup(sessionGroup, attributes);
                if (req.queryParams("id") != null) {
                    if (!sessionGroup.hasRight((Types.RIGHT.EDIT_REPRESENTATIVE))) {
                        res.status(403);
                        res.redirect("/abgeordnete");
                        return null;
                    }
                    List<Document> tempAB = mDB
                            .read(new Document().append("_id", Integer.parseInt(req.queryParams("id"))), "abgeordnete");
                    if (tempAB.isEmpty()) {
                        res.status(404);
                        res.redirect("/abgeordnete");
                        return null;
                    }
                    Abgeordneter_MongoDB_Impl abgeordneter = new Abgeordneter_MongoDB_Impl(tempAB.get(0));
                    attributes.put("abgeordneter", abgeordneter);
                } else {
                    if (!sessionGroup.hasRight((Types.RIGHT.CREATE_REPRESENTATIVE))) {
                        res.status(403);
                        res.redirect("/abgeordnete");
                        return null;
                    }
                }

            }
            return new ModelAndView(attributes, "editAbgeordneter.ftl");
        }, new FreeMarkerEngine(cf));

        Spark.get("/edit/rede", (req, res) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            Document session = getSessionIDRights(req.cookie("sessionID"));
            if (session == null) {
                res.status(401);
                res.redirect("/");
            } else {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                res.status(200);
                extractRightsFromGroup(sessionGroup, attributes);
                if (req.queryParams("id") != null) {
                    if (!sessionGroup.hasRight((Types.RIGHT.EDIT_SPEECH))) {
                        res.status(403);
                        res.redirect("/reden");
                        return null;
                    }
                    List<Document> tempRede = mDB.read(new Document().append("_id", req.queryParams("id")), "reden");
                    if (tempRede.isEmpty()) {
                        res.status(404);
                        res.redirect("/reden");
                        return null;
                    }
                    Rede_MongoDB_Impl rede = new Rede_MongoDB_Impl(tempRede.get(0));
                    attributes.put("rede", rede);
                } else {
                    if (!sessionGroup.hasRight((Types.RIGHT.CREATE_SPEECH))) {
                        res.status(403);
                        res.redirect("/reden");
                        return null;
                    }
                }
                HashSet<Abgeordneter_MongoDB_Impl> abgeordnete = new HashSet<>();
                mDB.read(new Document(),
                        new Document().append("_id", 1.0).append("nachname", 1.0).append("vorname", 1.0)
                                .append("geschlecht", 1.0),
                        "abgeordnete").forEach(
                                abgeordneter -> abgeordnete.add(new Abgeordneter_MongoDB_Impl(abgeordneter)));
                attributes.put("abgeordnete", abgeordnete);
            }
            return new ModelAndView(attributes, "editRede.ftl");
        }, new FreeMarkerEngine(cf));

        Spark.get("/edit/sitzung", (req, res) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            Document session = getSessionIDRights(req.cookie("sessionID"));
            if (session == null) {
                res.status(401);
                res.redirect("/");
            } else {
                Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
                res.status(200);
                extractRightsFromGroup(sessionGroup, attributes);
                if (req.queryParams("wp") != null || req.queryParams("snumber") != null) {
                    if (!sessionGroup.hasRight((Types.RIGHT.EDIT_SESSION))) {
                        res.status(403);
                        res.redirect("/reden");
                        return null;
                    }
                    List<Document> tempSitzung = mDB.aggregate(Arrays.asList(
                            new Document()
                                    .append("$match", new Document()
                                            .append("sitzung.wahlperiode", Integer.parseInt(req.queryParams("wp")))
                                            .append("sitzung.sitzungsnummer",
                                                    Integer.parseInt(req.queryParams("snumber")))),
                            new Document()
                                    .append("$group", new Document()
                                            .append("_id", new Document()
                                                    .append("field1", "$sitzung.wahlperiode")
                                                    .append("field2", "$sitzung.sitzungsnummer"))
                                            .append("uniqueDocument", new Document()
                                                    .append("$first", "$$ROOT.sitzung"))
                                            .append("agendas", new Document()
                                                    .append("$addToSet", "$$ROOT.agenda"))),
                            new Document()
                                    .append("$replaceRoot", new Document()
                                            .append("newRoot", new Document()
                                                    .append("$mergeObjects", Arrays.asList(
                                                            "$uniqueDocument",
                                                            new Document()
                                                                    .append("agendas", "$agendas")))))),
                            "reden");
                    if (tempSitzung.isEmpty()) {
                        res.status(404);
                        res.redirect("/reden");
                        return null;
                    }
                    Sitzung_MongoDB_Impl sitzung = new Sitzung_MongoDB_Impl(tempSitzung.get(0));
                    attributes.put("sitzung", sitzung);
                } else {
                    res.status(403);
                    res.redirect("/reden");
                    return null;
                }
            }
            return new ModelAndView(attributes, "editSitzung.ftl");
        }, new FreeMarkerEngine(cf));

        /*---------------------API---------------------*/

        Spark.get("/parliamentbrowser/api/data/representatives", (req, res) -> {
            String id = req.queryParams("id");
            String partei = req.queryParams("partei");
            String wahlperiode = req.queryParams("wahlperiode");
            res.type("application/json");

            Document query = new Document();
            if (id != null) {
                query.append("_id", Integer.parseInt(id));
            }
            if (partei != null) {
                query.append("partei", partei);
            }
            if (wahlperiode != null) {
                query.append("mandate.wahlperiode", Integer.parseInt(wahlperiode));
            }
            Document sort = new Document();
            sort.append("vorname", 1.0);
            sort.append("nachname", 1.0);

            return new JSONArray(mDB.read(query, new Document(), sort, "abgeordnete"));
        });

        /*
         * Dieser Endpunkt gibt alle Reden wieder.
         * Als Parameter können die Wahlperiode (wp), die Sitzungsnummer (sitzung) und
         * die Tagesordnungspunkt (top) übergeben werden.
         * Es wird empfohlen, die Parameter zu nutzen, um die Datenmenge zu reduzieren.
         * 
         */
        Spark.get("/parliamentbrowser/api/data/speeches", (req, res) -> {
            String top = req.queryParams("top");
            String sitzung = req.queryParams("sitzung");
            String wp = req.queryParams("wp");

            Document query = new Document();
            if (sitzung != null) {
                query.append("sitzung.sitzungsnummer", Integer.parseInt(sitzung));
            }
            if (wp != null) {
                query.append("sitzung.wahlperiode", Integer.parseInt(wp));
            }
            if (top != null) {
                query.append("agenda.agenda_nr", Integer.parseInt(top));
            }
            return new JSONArray(mDB.read(query, "reden"));
        });

        /*
         * Dieser Endpunkt gibt die Anzahl der Entities für alle Reden zurueck.
         * Als Parameter können: text, fromDate, toDate übergeben werden.
         * Wobei text ein Suchbegriff ist, der in den Reden gesucht wird.
         * 
         */
        Spark.get("/parliamentbrowser/api/data/speeches/entities", (req, res) -> {
            LinkedList<Bson> pipeline = new LinkedList<>();
            res.type("application/json");
            extractParamsFromURL(req, pipeline);
            pipeline.addAll(Arrays.asList(
                    new Document()
                            .append("$unwind", new Document()
                                    .append("path", "$entities")),
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", "$entities.label")
                                    .append("total_count", new Document()
                                            .append("$sum", "$entities.count"))),
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", new BsonNull())
                                    .append("entities", new Document()
                                            .append("$push", new Document()
                                                    .append("label", "$_id")
                                                    .append("count", "$total_count")))),
                    new Document()
                            .append("$project", new Document()
                                    .append("_id", 0))));
            return new JSONArray(mDB.aggregate(pipeline, "reden"));
        });

        /*
         * Dieser Endpunkt gibt die Anzahl der pos für alle Reden zurueck.
         * Als Parameter können: text, fromDate, toDate übergeben werden.
         * Wobei text ein Suchbegriff ist, der in den Reden gesucht wird.
         * 
         */
        Spark.get("/parliamentbrowser/api/data/speeches/pos", (req, res) -> {
            LinkedList<Bson> pipeline = new LinkedList<>();
            res.type("application/json");
            extractParamsFromURL(req, pipeline);
            pipeline.addAll(Arrays.asList(
                    new Document()
                            .append("$unwind", new Document()
                                    .append("path", "$pos")),
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", "$pos.label")
                                    .append("total_count", new Document()
                                            .append("$sum", "$pos.count"))),
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", new BsonNull())
                                    .append("pos", new Document()
                                            .append("$push", new Document()
                                                    .append("label", "$_id")
                                                    .append("count", "$total_count")))),
                    new Document()
                            .append("$project", new Document()
                                    .append("_id", 0))));
            return new JSONArray(mDB.aggregate(pipeline, "reden"));
        });

        /*
         * Dieser Endpunkt gibt den Average der Sentiments für alle Reden zurueck.
         * Als Parameter können: text, fromDate, toDate übergeben werden.
         * Wobei text ein Suchbegriff ist, der in den Reden gesucht wird.
         * 
         */
        Spark.get("/parliamentbrowser/api/data/speeches/sentiments", (req, res) -> {
            LinkedList<Bson> pipeline = new LinkedList<>();
            res.type("application/json");
            extractParamsFromURL(req, pipeline);
            pipeline.addAll(Arrays.asList(
                    new Document()
                            .append("$unwind", new Document()
                                    .append("path", "$sentiments")),
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", "$sentiments.label")
                                    .append("total_score", new Document()
                                            .append("$avg", "$sentiments.score"))),
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", new BsonNull())
                                    .append("sentiments", new Document()
                                            .append("$push", new Document()
                                                    .append("label", "$_id")
                                                    .append("score", "$total_score")))),
                    new Document()
                            .append("$project", new Document()
                                    .append("_id", 0))));
            return new JSONArray(mDB.aggregate(pipeline, "reden"));
        });

        /*
         * Dieser Endpunkt gibt den Average der topics für alle Reden zurueck.
         * Als Parameter können: text, fromDate, toDate übergeben werden.
         * Wobei text ein Suchbegriff ist, der in den Reden gesucht wird.
         * 
         */
        Spark.get("/parliamentbrowser/api/data/speeches/topics", (req, res) -> {
            LinkedList<Bson> pipeline = new LinkedList<>();
            res.type("application/json");
            extractParamsFromURL(req, pipeline);
            pipeline.addAll(Arrays.asList(
                    new Document()
                            .append("$unwind", new Document()
                                    .append("path", "$topics")),
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", "$topics.label")
                                    .append("total_score", new Document()
                                            .append("$avg", "$topics.score"))),
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", new BsonNull())
                                    .append("topics", new Document()
                                            .append("$push", new Document()
                                                    .append("label", "$_id")
                                                    .append("score", "$total_score")))),
                    new Document()
                            .append("$project", new Document()
                                    .append("_id", 0))));
            return new JSONArray(mDB.aggregate(pipeline, "reden"));
        });

        Spark.get("/parliamentbrowser/api/data/speakers/speech-distribution", (req, res) -> {
            HashSet<Abgeordneter_MongoDB_Impl> abgerodnete = new HashSet<>();
            LinkedList<Bson> pipeline = new LinkedList<>();
            extractParamsFromURL(req, pipeline);
            res.type("application/json");
            pipeline.addAll(Arrays.asList(
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", "$abgeordneter")),
                    new Document()
                            .append("$lookup", new Document()
                                    .append("from", "abgeordnete")
                                    .append("localField", "_id")
                                    .append("foreignField", "_id")
                                    .append("as", "abgeordneter")),
                    new Document()
                            .append("$unwind", new Document()
                                    .append("path", "$abgeordneter"))));
            mDB.aggregate(pipeline, "reden").forEach(
                    abgeordneter -> abgerodnete
                            .add(new Abgeordneter_MongoDB_Impl(abgeordneter.get("abgeordneter", Document.class))));
            JSONArray result = new JSONArray();
            abgerodnete.forEach(abgeordneter -> {
                JSONObject obj = new JSONObject();
                obj.put("id", abgeordneter.getID());
                obj.put("vorname", abgeordneter.getVorname());
                obj.put("nachname", abgeordneter.getName());
                obj.put("speechesCount", abgeordneter.listReden().size());
                obj.put("picture_url", abgeordneter.getPrimaryPicture().getLocalURL());
                result.put(obj);
            });
            return result;
        });

        Spark.post("/parliamentbrowser/user/login", (req, res) -> {
            System.out.println("login");
            String username = req.headers("username");
            String password = req.headers("password");
            List<Document> user = mDB.read(new Document().append("_id", username), "users");
            if (user.isEmpty()) {
                res.status(401);
                return new JSONObject().put("error", "User not found");
            }
            String storedPwd = user.get(0).getString("password");
            res.type("application/json");
            if (pwd.verify(password, storedPwd)) {
                UUID sessionID = UUID.randomUUID();
                mDB.add(sessionID, username);
                res.status(200);
                return new JSONObject().put("sessionID", sessionID);
            } else {
                res.status(401);
                return new JSONObject().put("error", "Wrong password");
            }
        });

        Spark.post("/parliamentbrowser/user/logout", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            mDB.update(sessionID);
            res.status(200);
            return new JSONObject().put("message", "Logged out");
        });

        Spark.post("/parliamentbrowser/user/changePwd", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.CHANGE_PASSWORD)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to change password");
            }
            String username = session.get("user", Document.class).getString("_id");
            String oldPwd = req.headers("oldPwd");
            String newPwd = req.headers("newPwd");
            List<Document> user = mDB.read(new Document().append("_id", username), "users");
            if (user.isEmpty()) {
                res.status(401);
                return new JSONObject().put("error", "User not found");
            }
            String storedPwd = user.get(0).getString("password");
            if (!pwd.verify(oldPwd, storedPwd)) {
                res.status(401);
                return new JSONObject().put("error", "Wrong password");
            }
            mDB.update(eq("_id", username), new Document("$set", new Document().append("password", pwd.hash(newPwd))),
                    "users");
            res.status(200);
            return new JSONObject().put("message", "Password changed");
        });

        Spark.delete("/parliamentbrowser/user/delete-user", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.DELETE_USER)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to delete user");
            }
            String username = req.headers("username");
            mDB.delete(eq("_id", username), "users");
            res.status(200);
            return new JSONObject().put("message", "User deleted");
        });

        Spark.post("/parliamentbrowser/user/save-user", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.EDIT_USER)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to add user");
            }
            String oldName = req.headers("oldName");
            String username = req.headers("newName");
            String password = req.headers("password");
            String group = req.headers("group");
            if (password.isEmpty()) {
                password = mDB.read(new Document().append("_id", oldName), "users").get(0).getString("password");
            } else {
                password = pwd.hash(password);
            }
            if (oldName.equals(username)) {
                mDB.update(eq("_id", oldName),
                        new Document("$set", new Document().append("password", password).append("group", group)),
                        "users");
                res.status(200);
                return new JSONObject().put("message", "User added");
            }
            try {
                mDB.add(new Document().append("_id", username).append("password", password).append("group", group),
                        "users");
                mDB.delete(eq("_id", oldName), "users");
            } catch (Exception e) {
                res.status(400);
                return new JSONObject().put("error", "Username already exists");
            }

            res.status(200);
            return new JSONObject().put("message", "User added");
        });

        Spark.post("/parliamentbrowser/user/add-user", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.ADD_USER)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to add user");
            }
            String username = req.headers("username");
            String password = req.headers("password");
            String group = req.headers("group");
            try {
                mDB.add(new Document().append("_id", username).append("password", pwd.hash(password)).append("group",
                        group), "users");
            } catch (Exception e) {
                res.status(400);
                return new JSONObject().put("error", "Username already exists");
            }
            res.status(200);
            return new JSONObject().put("message", "User added");
        });

        Spark.post("/parliamentbrowser/user/save-group", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.EDIT_GROUP)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to add group");
            }
            String oldName = req.headers("oldName");
            String name = req.headers("newName");
            String rights = req.headers("rights");
            String[] rightsArray = rights.split(",");
            if (oldName.equals(name)) {
                mDB.update(eq("_id", oldName),
                        new Document("$set", new Document().append("rights", Arrays.asList(rightsArray))), "groups");
                res.status(200);
                return new JSONObject().put("message", "Group added");
            }
            try {
                mDB.add(new Document().append("_id", name).append("rights", rightsArray), "groups");
                mDB.delete(eq("_id", oldName), "groups");
            } catch (Exception e) {
                res.status(400);
                return new JSONObject().put("error", "Group already exists");
            }

            res.status(200);
            return new JSONObject().put("message", "Group added");
        });

        Spark.delete("/parliamentbrowser/user/delete-group", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.DELETE_GROUP)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to delete group");
            }
            String name = req.headers("groupName");
            mDB.delete(eq("_id", name), "groups");
            mDB.updateMany(eq("group", name), new Document("$set", new Document("group", "user")), "users");
            res.status(200);
            return new JSONObject().put("message", "Group deleted");
        });

        Spark.post("/parliamentbrowser/user/add-group", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.ADD_GROUP)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to add group");
            }
            String name = req.headers("groupName");
            String rights = req.headers("rights");
            String[] rightsArray = rights.split(",");
            try {
                mDB.add(new Document().append("_id", name).append("rights", Arrays.asList(rightsArray)), "groups");
            } catch (Exception e) {
                res.status(400);
                return new JSONObject().put("error", "Group already exists");
            }
            res.status(200);
            return new JSONObject().put("message", "Group added");
        });

        Spark.post("/parliamentbrowser/api/data/representative/save", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            res.type("application/json");

            Document rep = Document.parse(req.body());
            String sID = rep.get("_id").toString();

            if (rep.containsKey("_id") && rep.get("_id") != null) {
                if (!sessionGroup.hasRight(Types.RIGHT.EDIT_REPRESENTATIVE)) {
                    res.status(403);
                    return new JSONObject().put("error", "No rights to edit representative");
                }
                int id = Integer.parseInt(rep.getString("_id"));
                rep.remove("_id");
                mDB.update(eq("_id", id), new Document("$set", rep), "abgeordnete");
            } else {
                if (!sessionGroup.hasRight(Types.RIGHT.CREATE_REPRESENTATIVE)) {
                    res.status(403);
                    return new JSONObject().put("error", "No rights to create representative");
                }
                List<Document> id = mDB.read(new Document()
                        .append("_id", new Document()
                                .append("$gte", 777700000)
                                .append("$lt", 888800000)),
                        new Document().append("_id", 1.0),
                        new Document("_id", -1.0),
                        1, "abgeordnete");
                if (id.isEmpty()) {
                    rep.append("_id", 777700000);
                } else {
                    rep.append("_id", id.get(0).getInteger("_id") + 1);
                }
                mDB.add(rep, "abgeordnete");
            }

            return new JSONObject().put("message", "Representative edited").put("id", sID);
        });

        Spark.delete("/parliamentbrowser/api/data/representative/delete", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.DELETE_REPRESENTATIVE)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to delete representative");
            }
            int id = Integer.parseInt(req.headers("id"));
            mDB.delete(eq("_id", id), "abgeordnete");
            return new JSONObject().put("message", "Representative deleted");
        });

        Spark.post("/parliamentbrowser/api/data/sitzung/save", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            Document sessionDoc = Document.parse(req.body());
            if (sessionDoc.containsKey("old_wahlperiode") && sessionDoc.containsKey("old_sitzungsnummer")) {
                if (!sessionGroup.hasRight(Types.RIGHT.EDIT_SESSION)) {
                    res.status(403);
                    return new JSONObject().put("error", "No rights to edit session");
                }
                int wp = sessionDoc.getInteger("old_wahlperiode");
                int sn = sessionDoc.getInteger("old_sitzungsnummer");
                sessionDoc.remove("old_wahlperiode");
                sessionDoc.remove("old_sitzungsnummer");
                List<Document> tops = sessionDoc.getList("tops", Document.class);
                sessionDoc.remove("tops");
                for (Document top : tops) {
                    int topID = top.getInteger("oldTOP");
                    top.remove("oldTOP");
                    mDB.updateMany(
                            and(eq("sitzung.wahlperiode", wp), eq("sitzung.sitzungsnummer", sn),
                                    eq("agenda.agenda_nr", topID)),
                            new Document("$set", new Document()
                                    .append("sitzung", sessionDoc)
                                    .append("agenda", top)),
                            "reden");
                }
            }
            return new JSONObject().put("message", "Session edited");
        });

        Spark.post("/parliamentbrowser/api/data/speech/save", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            res.type("application/json");
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            Document speech = Document.parse(req.body());
            if (speech.containsKey("_id")) {
                if (!sessionGroup.hasRight(Types.RIGHT.EDIT_SPEECH)) {
                    res.status(403);
                    return new JSONObject().put("error", "No rights to edit speech");
                }
                String id = speech.getString("_id");
                if (speech.containsKey("text") || speech.containsKey("kommentare")) {
                    mDB.update(new Rede_MongoDB_Impl(speech));
                } else {

                    speech.remove("_id");
                    mDB.update(eq("_id", id), new Document("$set", speech), "reden");
                }
                return new JSONObject().put("message", "Speech edited").put("id", id);

            } else {
                if (!sessionGroup.hasRight(Types.RIGHT.CREATE_SPEECH)) {
                    res.status(403);
                    return new JSONObject().put("error", "No rights to create speech");
                }
                speech.put("_id", UUID.randomUUID().toString());
                mDB.add(new Rede_MongoDB_Impl(speech));
            }
            return new JSONObject().put("message", "Speech added").put("id", speech.get("_id"));
        });

        Spark.delete("/parliamentbrowser/api/data/speech/delete", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                res.redirect("/");
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.DELETE_SPEECH)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to delete speech");
            }
            String id = req.headers("id");
            mDB.delete(eq("_id", id), "reden");
            return new JSONObject().put("message", "Speech deleted");
        });

        Spark.delete("/parliamentbrowser/api/data/sitzung/delete", (req, res) -> {
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.DELETE_SESSION)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to delete session");
            }
            int wp = Integer.parseInt(req.headers("wp"));
            int sn = Integer.parseInt(req.headers("sn"));
            mDB.deleteMany(and(eq("sitzung.wahlperiode", wp), eq("sitzung.sitzungsnummer", sn)), "reden");
            return new JSONObject().put("message", "Session deleted");
        });

        Spark.get("/parliamentbrowser/api/data/processing/progress", (req, res) -> {
            res.type("application/json");
            return new JSONObject().put("downloadProgress", df.getDownloadProgress()).put("processingProgress",
                    df.getProcessProgress());
        });

        Spark.post("/parliamentbrowser/api/data/processing/missingSpeeches", (req, res) -> {
            res.type("application/json");
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.NLP_PROCESSING)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to process speeches");
            }
            df.addUnprocessedSpeeches();
            res.status(200);
            return new JSONObject().put("success", "added missing speeches");
        });

        Spark.post("/parliamentbrowser/api/data/processing/missingProtocols", (req, res) -> {
            res.type("application/json");
            UUID sessionID = UUID.fromString(req.cookie("sessionID"));
            Document session = getSessionIDRights(sessionID.toString());
            if (session == null) {
                res.status(401);
                return new JSONObject().put("error", "Not logged in");
            }
            Group sessionGroup = new Group_Impl(session.get("rights", Document.class));
            if (!sessionGroup.hasRight(Types.RIGHT.NLP_PROCESSING)
                    || !sessionGroup.hasRight(Types.RIGHT.CREATE_SESSION)) {
                res.status(403);
                return new JSONObject().put("error", "No rights to process speeches");
            }
            df.addMissingPrototols();
            res.status(200);
            return new JSONObject().put("success", "added missing protocols");
        });
    }

    private static void extractParamsFromURL(Request req, LinkedList<Bson> pipeline) {
        String text = req.queryParams("text");
        String fromDate = req.queryParams("fromDate");
        String toDate = req.queryParams("toDate");
        if (text != null) {
            System.out.println(text);
            pipeline.add(new Document()
                    .append("$match", new Document()
                            .append("$text", new Document()
                                    .append("$search", "\"" + text + "\""))));
        }
        if (fromDate != null && toDate != null) {
            pipeline.add(new Document()
                    .append("$match", new Document()
                            .append("sitzung.date", new Document()
                                    .append("$gte", fromDate)
                                    .append("$lte", toDate))));
        }

    }

    private static Document getSessionIDRights(String sessionID) {
        if (sessionID == null) {
            return null;
        }
        UUID sessionUUID = UUID.fromString(sessionID);
        List<Document> user = mDB.aggregate(Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("_id", sessionUUID)
                                .append("deprecated", new Document()
                                        .append("$exists", false))),
                new Document()
                        .append("$lookup", new Document()
                                .append("from", "users")
                                .append("localField", "user")
                                .append("foreignField", "_id")
                                .append("as", "user")),
                new Document()
                        .append("$unwind", new Document()
                                .append("path", "$user")),
                new Document()
                        .append("$lookup", new Document()
                                .append("from", "groups")
                                .append("localField", "user.group")
                                .append("foreignField", "_id")
                                .append("as", "rights")),
                new Document()
                        .append("$unwind", new Document()
                                .append("path", "$rights"))),
                "sessions");
        if (user.isEmpty()) {
            return null;
        }
        return user.get(0);
    }

    private static void extractRightsFromGroup(Group sessionGroup, HashMap<String, Object> attributes) {
        if (sessionGroup.hasRight(Types.RIGHT.LOGOUT)) {
            attributes.put("loggedIn", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.CHANGE_PASSWORD)) {
            attributes.put("changePwd", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.ADD_USER)) {
            attributes.put("addUser", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.DELETE_USER)) {
            attributes.put("deleteUser", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.EDIT_USER)) {
            attributes.put("editUser", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.ADD_GROUP)) {
            attributes.put("addGroup", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.DELETE_GROUP)) {
            attributes.put("deleteGroup", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.EDIT_GROUP)) {
            attributes.put("editGroup", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.CREATE_REPRESENTATIVE)) {
            attributes.put("createRep", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.DELETE_REPRESENTATIVE)) {
            attributes.put("deleteRep", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.EDIT_REPRESENTATIVE)) {
            attributes.put("editRep", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.CREATE_SPEECH)) {
            attributes.put("createSpeech", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.DELETE_SPEECH)) {
            attributes.put("deleteSpeech", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.EDIT_SPEECH)) {
            attributes.put("editSpeech", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.CREATE_SESSION)) {
            attributes.put("createSession", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.DELETE_SESSION)) {
            attributes.put("deleteSession", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.EDIT_SESSION)) {
            attributes.put("editSession", true);
        }
        if (sessionGroup.hasRight(Types.RIGHT.NLP_PROCESSING)) {
            attributes.put("nlpProcessing", true);
        }
    }
}
