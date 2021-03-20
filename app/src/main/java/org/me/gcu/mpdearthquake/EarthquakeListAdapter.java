package org.me.gcu.mpdearthquake;


import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
            view = v.inflate(R.layout.activity_earthquakelistview, null);
        }

        EarthquakeItem earthquake = getItem(i);

        if (earthquake != null) {
            int red = 0x88ff6347;
            int yellow = 0x88ffcc00;
            int green = 0x8833cc33;

            TextView locationText = (TextView) view.findViewById(R.id.locationText);
            TextView latText = (TextView) view.findViewById(R.id.latText);
            TextView longText = (TextView) view.findViewById(R.id.longText);
            TextView depthText = (TextView) view.findViewById(R.id.depthText);
            TextView magnitudeText = (TextView) view.findViewById(R.id.magnitudeText);
            TextView dateText = (TextView) view.findViewById(R.id.dateText);
            TextView categoryText = (TextView) view.findViewById(R.id.categoryText);

            String[] parsedDescription = earthquake.getDescription().split(" ; ");
            Log.e("EarthquakeListAdapter", Arrays.toString(parsedDescription));
            Map<String, String> kv = new HashMap<>();
            for (String item : parsedDescription) {
                String[] parsedItem = item.split(":");
                kv.put(parsedItem[0], parsedItem[1].trim());
            }
            locationText.setText(kv.get("Location"));
            depthText.setText(context.getString(R.string.earthquake_depth, kv.get("Depth")));
            magnitudeText.setText(context.getString(R.string.earthquake_magnitude, kv.get("Magnitude")));
            latText.setText(context.getString(R.string.earthquake_lat, String.valueOf(earthquake.getLat())));
            longText.setText(context.getString(R.string.earthquake_long, String.valueOf(earthquake.getLon())));
            dateText.setText(context.getString(R.string.earthquake_date, earthquake.getDate()));
            categoryText.setText(context.getString(R.string.earthquake_category, earthquake.getCategory()));

            String[] parsedMagnitude = kv.get("Magnitude").trim().split(" ");
            LinearLayout container = (LinearLayout) view.findViewById(R.id.earthquakeItem);
            float magnitudeVal = Float.parseFloat(parsedMagnitude[0]);
            if (magnitudeVal < 1.0){
                container.setBackgroundColor(green);
            }
            else if (magnitudeVal >= 1.0 && magnitudeVal < 3.0) {
                container.setBackgroundColor(yellow);
            }
            else {
                container.setBackgroundColor(red);
            }

            Button button = (Button) view.findViewById(R.id.viewDetails);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   Log.e("EarthquakeListAdapter", "I clicked a button");
                }
            });
        }
        return view;
    }
}
