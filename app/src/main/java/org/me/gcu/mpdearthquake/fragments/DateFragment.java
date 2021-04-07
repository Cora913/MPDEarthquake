package org.me.gcu.mpdearthquake.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.me.gcu.mpdearthquake.DateResultsViewModel;
import org.me.gcu.mpdearthquake.EarthquakeItem;
import org.me.gcu.mpdearthquake.EarthquakeListViewModel;
import org.me.gcu.mpdearthquake.R;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFragment extends Fragment implements View.OnClickListener {

    Date dateFrom;
    Date dateTo;
    private ArrayList<EarthquakeItem> earthquakes;

    EarthquakeListViewModel earthquakeListViewModel;
    DateResultsViewModel dateResultsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_date, container, false);

        earthquakeListViewModel = new ViewModelProvider(requireActivity()).get(EarthquakeListViewModel.class);
        earthquakeListViewModel.getEarthquakes().observe(getViewLifecycleOwner(), items -> {
            earthquakes = items;
        });

        rootView.findViewById(R.id.dateResult).setVisibility(View.GONE);

        dateResultsViewModel = new ViewModelProvider(requireActivity()).get(DateResultsViewModel.class);
        dateResultsViewModel.getEarthquakeResults().observe(getViewLifecycleOwner(), items -> {
            if (items.size() > 0) {
                EarthquakeItem mostNortherly = items.get(0);
                EarthquakeItem mostSoutherly = items.get(0);
                EarthquakeItem mostWesterly = items.get(0);
                EarthquakeItem mostEasterly = items.get(0);
                float largestMagnitude = 0.0f;
                float deepestEarthquake = (float) Double.NEGATIVE_INFINITY;
                float shallowestEarthquake = (float) Double.POSITIVE_INFINITY;
                for (EarthquakeItem earthquake : items) {
                    if (earthquake.getLon() < mostWesterly.getLon()) {
                        mostWesterly = earthquake;
                    }
                    if (earthquake.getLon() > mostEasterly.getLon()) {
                        mostEasterly = earthquake;
                    }
                    if (earthquake.getLat() < mostNortherly.getLat()) {
                        mostNortherly = earthquake;
                    }
                    if (earthquake.getLat() > mostSoutherly.getLat()) {
                        mostSoutherly = earthquake;
                    }

                    largestMagnitude = Math.max(earthquake.getMagnitude(), largestMagnitude);
                    deepestEarthquake = Math.max(earthquake.getDepth(), deepestEarthquake);
                    shallowestEarthquake = Math.min(earthquake.getDepth(), shallowestEarthquake);
                }
                TextView mostWesterlyText = rootView.findViewById(R.id.mostWesterly);
                TextView mostEasterlyText = rootView.findViewById(R.id.mostEasterly);
                TextView mostNortherlyText = rootView.findViewById(R.id.mostNortherly);
                TextView mostSoutherlyText = rootView.findViewById(R.id.mostSoutherly);
                TextView largestMagnitudeText = rootView.findViewById(R.id.largestMagnitude);
                TextView deepestEarthquakeText = rootView.findViewById(R.id.deepestEarthquake);
                TextView shallowestEarthquakeText = rootView.findViewById(R.id.shallowestEarthquake);

                mostWesterlyText.setText(getString(R.string.most_westerly, mostWesterly.getLocation()));
                mostEasterlyText.setText(getString(R.string.most_easterly, mostEasterly.getLocation()));
                mostNortherlyText.setText(getString(R.string.most_northerly, mostNortherly.getLocation()));
                mostSoutherlyText.setText(getString(R.string.most_southerly, mostSoutherly.getLocation()));
                largestMagnitudeText.setText(getString(R.string.largest_magnitude, largestMagnitude));
                deepestEarthquakeText.setText(getString(R.string.deepest_earthquake, deepestEarthquake));
                shallowestEarthquakeText.setText(getString(R.string.shallowest_earthquake, shallowestEarthquake));

                rootView.findViewById(R.id.dateResult).setVisibility(View.VISIBLE);
            }
            else {
                rootView.findViewById(R.id.dateResult).setVisibility(View.GONE);
                Log.e("DateFragment", "No earthquakes in that period");
            }
        });

        Button btnDateFrom = rootView.findViewById(R.id.btnDateFrom);
        btnDateFrom.setOnClickListener(this);
        Button btnDateTo = rootView.findViewById(R.id.btnDateTo);
        btnDateTo.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog picker = null;

        switch (v.getId()) {
            case R.id.btnDateFrom:
            case R.id.btnDateTo:
                picker = new DatePickerDialog(requireActivity(), (view, year1, monthOfYear, dayOfMonth) -> {
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault())
                            .parse(String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, (monthOfYear + 1), year1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date != null) {
                        Button button = (Button) v;
                        button.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault());
                        Log.e("DateFragment", dateFormat.format(date));
                    }

                    if (v.getId() == R.id.btnDateFrom)
                        dateFrom = date;
                    else if (v.getId() == R.id.btnDateTo)
                        dateTo = date;

                    if (dateFrom != null && dateTo != null) {
                        ArrayList<EarthquakeItem> earthquakeResults = new ArrayList<>();
                        for (EarthquakeItem earthquake : earthquakes) {
                           if(dateFrom.before(earthquake.getDate()) && dateTo.after(earthquake.getDate())){
                               earthquakeResults.add(earthquake);
                           }
                        }
                        dateResultsViewModel.setEarthquakeResults(earthquakeResults);
                    }
                }, year, month, day);
                break;
        }

        if (picker != null) {
            picker.show();
        }
    }
}