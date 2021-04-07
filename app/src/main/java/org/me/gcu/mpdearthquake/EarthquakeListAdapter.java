package org.me.gcu.mpdearthquake;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import java.util.ArrayList;

public class EarthquakeListAdapter extends BaseAdapter {
    Context context;
    private ArrayList<EarthquakeItem> earthquakes;

    public EarthquakeListAdapter(Context context, EarthquakeListViewModel vm, LifecycleOwner viewOwner) {
        this.context = context;
        this.earthquakes = new ArrayList<>();

        vm.getEarthquakes().observe(viewOwner, items -> {
            earthquakes = items;
        });
    }

    @Override
    public int getCount() {
        return earthquakes.size();
    }

    @Override
    public EarthquakeItem getItem(int position) {
        return earthquakes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater v;
            v = LayoutInflater.from(context);
            view = v.inflate(R.layout.earthquakelistview, null);
        }

        EarthquakeItem earthquake = getItem(i);

        if (earthquake != null) {
            int red = 0x88ff6347;
            int yellow = 0x88ffcc00;
            int green = 0x8833cc33;

            TextView locationText = view.findViewById(R.id.locationText);
            TextView latText = view.findViewById(R.id.latText);
            TextView longText = view.findViewById(R.id.longText);
            TextView depthText = view.findViewById(R.id.depthText);
            TextView magnitudeText = view.findViewById(R.id.magnitudeText);
            TextView dateText = view.findViewById(R.id.dateText);
            TextView categoryText = view.findViewById(R.id.categoryText);

            locationText.setText(earthquake.getLocation());
            depthText.setText(context.getString(R.string.earthquake_depth, earthquake.getDepth()));
            magnitudeText.setText(context.getString(R.string.earthquake_magnitude, earthquake.getMagnitude()));
            latText.setText(context.getString(R.string.earthquake_lat, String.valueOf(earthquake.getLat())));
            longText.setText(context.getString(R.string.earthquake_long, String.valueOf(earthquake.getLon())));
            dateText.setText(context.getString(R.string.earthquake_date, earthquake.getDateToString()));
            categoryText.setText(context.getString(R.string.earthquake_category, earthquake.getCategory()));

            LinearLayout container = view.findViewById(R.id.earthquakeItem);
            if (earthquake.getMagnitude() < 1.0)
                container.setBackgroundColor(green);
            else if (earthquake.getMagnitude() >= 1.0 && earthquake.getMagnitude() < 3.0)
                container.setBackgroundColor(yellow);
            else
                container.setBackgroundColor(red);

            Button button = view.findViewById(R.id.viewDetails);
            button.setOnClickListener(v -> {
               Bundle bundle = new Bundle();
               bundle.putInt("earthquakeId", i);
               Navigation.findNavController((Activity) context, R.id.nav_host_fragment).navigate(R.id.action_show_earthquake_details, bundle);
            });
        }
        return view;
    }
}
