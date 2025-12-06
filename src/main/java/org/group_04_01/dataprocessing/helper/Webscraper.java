package org.group_04_01.dataprocessing.helper;

import org.group_04_01.dataprocessing.datastructure.Picture;
import org.group_04_01.dataprocessing.datastructure.imlementation.Picture_Impl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Webscraper {

    /**
     * Lädt die Stammdaten der Abgeordneten herunter.
     * 
     * @return InputStream der Stammdaten (ein XML-Dokument)
     * @throws IOException Fehler beim Verbinden mit der Website
     */
    public InputStream getBaseDataRepresentative() throws IOException {

        ZipInputStream zis = downloadZipFromUrl(
                "https://www.bundestag.de/resource/blob/472878/a4859899e44a7cab1a8233e5dd69f2f3/MdB-Stammdaten.zip");
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().equals("MDB_STAMMDATEN.XML")) {
                return zis;
            }
        }
        return null;
    }

    /**
     * Downloaded das Plenarprotokoll einer Sitzung mit der übergebenen Nummer aus
     * der Wahlperiode aus der Uebergebenen Wahlperiode.
     * 
     * @param wpNumber       Nummer der Wahlperiode
     * @param protocolNumber Nummer des Plenarprotokolls
     * @return InputStream des Plenarprotokolls (ein XML-Dokument)
     * @throws IOException Fehler beim Verbinden mit der Website
     */
    public InputStream getPlenaryProtocol(int wpNumber, int protocolNumber) throws IOException {
        String url;
        if (wpNumber == 20) {
            url = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354";
        } else {
            url = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410";
        }
        int plenaryProtocolCount = getPlenaryProtocolCount(wpNumber);
        // minus 4 because of weird sorting on the website
        int calculatedOffset = plenaryProtocolCount - protocolNumber - 4;
        url += "?offset=" + calculatedOffset;
        Document doc = Jsoup.connect(url).get();
        Element tableBody = doc.select("tbody").first();
        Elements tableRows = tableBody.select("tr");
        String downloadUrl = "https://www.bundestag.de";
        for (Element row : tableRows) {
            if (row.select("p").first().text().contains(protocolNumber + ". Sitzung")) {
                downloadUrl += row.select("a").first().attr("href");
                break;
            }
        }
        if (downloadUrl.length() < 25 || !downloadUrl.endsWith(".xml")) {
            return null;
        }
        System.out.println(downloadUrl);

        return new BufferedInputStream(new URL(downloadUrl).openStream());

    }

    /**
     * Gibt die Anzahl der Plenarprotokolle der Wahlperiode 20 zurück.
     * 
     * @param wpNumber Nummer der Wahlperiode
     * @return Anzahl der Plenarprotokolle
     * @throws IOException Fehler beim Verbinden mit der Website
     */
    public int getPlenaryProtocolCount(int wpNumber) throws IOException {
        String url;
        if (wpNumber == 20) {
            url = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354?offset=0";
        } else {
            url = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410?offset=0";
        }
        Document doc = Jsoup.connect(url).get();
        Element metaData = doc.getElementsByClass("meta-slider").first();
        return Integer.parseInt(metaData.attr("data-hits"));
    }

    /**
     * Lädt die Bilder von einem Abgeordneten herunter und gibt ein Set von
     * Picture-Objekten zurück.
     * 
     * @param nachname Nachname des Abgeordneten
     * @param vorname  Vorname des Abgeordneten
     * @param id       ID des Abgeordneten
     * @return Set von Picture-Objekten
     * @throws IOException Fehler beim Verbinden mit der Website
     */
    public HashSet<Picture> getPictureData(String nachname, String vorname, int id) throws IOException {
        String localPath = "src/main/resources/public/data/pictures/";
        File generalFolder = new File(localPath);
        HashSet<Picture> pictures = new HashSet<>();
        String url = "https://bilddatenbank.bundestag.de/search/picture-result?filterQuery%5Bname%5D%5B%5D=" + nachname
                + "%2C+" + vorname + "&filterQuery%5Bereignis%5D%5B%5D=Porträt%2FPortrait&sortVal=3#group-1";
        Document doc = Jsoup.connect(url).get();

        Elements picturesElements = doc.select(".rowGridContainer .item");

        if (!generalFolder.exists()) {
            generalFolder.mkdirs();
        }
        File folder = new File(localPath + id);

        int counter = 1;
        for (Element pictureElement : picturesElements) {
            String pictureUrl = "https://bilddatenbank.bundestag.de" + pictureElement.select("img").first().attr("src");
            String meta = pictureElement.select("a").first().attr("data-caption");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String pictureName = "representativeNr" + counter++ + ".jpg";

            downloadPictureFromUrl(pictureUrl, folder.getPath() + "/" + pictureName);
            String substring = meta.substring(meta.indexOf("Ort:") + 4);
            String location = substring.substring(0, substring.indexOf("<br>")).trim();
            substring = meta.substring(meta.indexOf("Aufgenommen:") + 12);
            String date = substring.substring(0, substring.indexOf("<br>")).trim();
            substring = meta.substring(meta.indexOf("Fotograf/in:") + 12);
            String photographer = substring.substring(0, substring.indexOf("<br>")).trim();
            Picture picture = new Picture_Impl(folder.getPath() + "/" + pictureName, pictureUrl, location, date,
                    photographer);
            pictures.add(picture);
        }
        return pictures;
    }

    /**
     * Lädt ein Bild von einer URL herunter und speichert es lokal.
     * 
     * @param pictureUrl URL des Bildes
     * @param localPath  Pfad, an dem das Bild lokal gespeichert werden soll
     * @throws IOException Fehler beim Verbinden mit der Website
     */
    public void downloadPictureFromUrl(String pictureUrl, String localPath) throws IOException {
        File pictureFile = new File(localPath);
        if (!pictureFile.exists()) {
            pictureFile.createNewFile();
            InputStream in = new URL(pictureUrl).openStream();
            java.nio.file.Files.copy(in, pictureFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            in.close();
        }
    }

    /**
     * Lädt ein Zip-File von einer URL herunter und gibt einen ZipInputStream
     * zurück.
     * 
     * @param zipUrl die URL des Zip-Files
     * @return ZipInputStream des Zip-Files
     * @throws IOException Fehler beim Verbinden mit der Website
     */
    public ZipInputStream downloadZipFromUrl(String zipUrl) throws IOException {
        URL url = new URL(zipUrl);
        InputStream is = new BufferedInputStream(url.openStream());
        return new ZipInputStream(is);
    }
}
