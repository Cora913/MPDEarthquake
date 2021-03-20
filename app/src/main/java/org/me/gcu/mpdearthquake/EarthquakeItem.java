package org.me.gcu.mpdearthquake;

import java.text.SimpleDateFormat;

public class EarthquakeItem {
    private String title;
    private String description;
    private String link;
    private String date;
    private String category;
    private float lat;
    private float lon;

    public EarthquakeItem() {

    }

    public EarthquakeItem(String title, String description, String link, String date, String category, float lat, float lon) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.category = category;
        this.lat = lat;
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }


}
