/**
 * @author Andreea-Cora Tibuc-Boboc
 * @student_id S1703130
 */

package org.me.gcu.mpdearthquake.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.me.gcu.mpdearthquake.models.EarthquakeItem;

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

