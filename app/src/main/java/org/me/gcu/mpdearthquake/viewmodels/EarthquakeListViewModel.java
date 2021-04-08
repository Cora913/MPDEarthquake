/**
 * @author Andreea-Cora Tibuc-Boboc
 * @student_id S1703130
 */

package org.me.gcu.mpdearthquake.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.me.gcu.mpdearthquake.models.EarthquakeItem;
import org.me.gcu.mpdearthquake.xml.XMLParser;

import java.util.ArrayList;

public class EarthquakeListViewModel extends ViewModel {
    private MutableLiveData<ArrayList<EarthquakeItem>> earthquakes;
    private final static String SEISMOLOGY_URL = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    public LiveData<ArrayList<EarthquakeItem>> getEarthquakes() {
        if (earthquakes == null) {
            earthquakes = new MutableLiveData<>();
            loadEarthquakes();
        }
        return earthquakes;
    }

    private void loadEarthquakes() {
        try {
            XMLParser parser = new XMLParser();
            String xmlString = parser.readURL(SEISMOLOGY_URL).get();
            ArrayList<EarthquakeItem> items = parser.parseXML(xmlString).get();
            earthquakes.setValue(items);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
