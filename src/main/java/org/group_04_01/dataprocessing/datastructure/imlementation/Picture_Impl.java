package org.group_04_01.dataprocessing.datastructure.imlementation;

import org.group_04_01.dataprocessing.datastructure.Picture;
import org.group_04_01.dataprocessing.helper.Webscraper;

import java.io.File;
import java.io.IOException;

public class Picture_Impl implements Picture {
    private String localURL;
    private String remoteURL;
    private String location;
    private String date;
    private String photographer;
    private int priority;

    public Picture_Impl(String localURL, String remoteURL, String location, String date, String photographer) {
        this.localURL = localURL;
        this.remoteURL = remoteURL;
        this.location = location;
        this.date = date;
        this.photographer = photographer;
        priority = -1;
    }

    public Picture_Impl(String localURL, String remoteURL, String location, String date, String photographer,
            int priority) {
        this.localURL = localURL;
        this.remoteURL = remoteURL;
        this.location = location;
        this.date = date;
        this.photographer = photographer;
        this.priority = priority;
    }

    @Override
    public String getLocalURL() {
        File file = new File(localURL);
        if (!file.exists()) {
            String folderURL = localURL.substring(0, localURL.lastIndexOf("/"));
            File folder = new File(folderURL);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            try {
                new Webscraper().downloadPictureFromUrl(remoteURL, localURL);
            } catch (IOException e) {
                System.out.println(
                        "Error while downloading picture from " + remoteURL + " to " + localURL + " " + e.getMessage());
                return "data/pictures/placeholder.jpg";
            }
        }

        return localURL.substring(localURL.indexOf("data"));
    }

    @Override
    public String getLocalURLBackend() {
        return localURL;
    }

    @Override
    public String getRemoteURL() {
        return remoteURL;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getPhotographer() {
        return photographer;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Picture o) {
        int comparePriority = Integer.compare(priority, o.getPriority());
        if (comparePriority == 0) {
            return localURL.compareTo(o.getLocalURL());
        }
        return comparePriority;
    }
}
