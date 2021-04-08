/**
 * @author Andreea-Cora Tibuc-Boboc
 * @student_id S1703130
 */

package org.me.gcu.mpdearthquake.models;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class EarthquakeItem {
    private String title;
    private String description;
    private String location;
    private float magnitude;
    private float depth;
    private String link;
    private Date date;
    private String category;
    private float lat;
    private float lon;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        String[] locationPart = location.toLowerCase().split(",");
        for (int i = 0; i < locationPart.length; i++) {
            String[] partWords = locationPart[i].split(" ");
            for (int j = 0; j < partWords.length; j++) {
                if (partWords[j].contains("and")) {
                    continue;
                }
                partWords[j] = partWords[j].substring(0, 1).toUpperCase() + partWords[j].substring(1);
            }
            locationPart[i] = TextUtils.join(" ", partWords);
        }
        this.location = TextUtils.join(", ", locationPart);

    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public String getDateToString() {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault());
        return dateFormat.format(this.date);
    }

    public void setDate(String date) throws ParseException {
        Date tmp = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault()).parse(date);
        this.date = tmp;
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

    @Override
    public String toString() {
        return "EarthquakeItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", magnitude=" + magnitude +
                ", depth=" + depth +
                ", link='" + link + '\'' +
                ", date=" + date +
                ", category='" + category + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
