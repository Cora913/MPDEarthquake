package org.me.gcu.mpdearthquake;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DateResultsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<EarthquakeItem>> earthquakeResults;

    public LiveData<ArrayList<EarthquakeItem>> getEarthquakeResults() {
        if (earthquakeResults == null) {
            earthquakeResults = new MutableLiveData<>();
        }
        return earthquakeResults;
    }

    public void setEarthquakeResults(ArrayList<EarthquakeItem> earthquakeResults) {
        this.earthquakeResults.setValue(earthquakeResults);
    }

}

