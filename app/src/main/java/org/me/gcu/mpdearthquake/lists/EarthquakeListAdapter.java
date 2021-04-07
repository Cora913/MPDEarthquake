package org.me.gcu.mpdearthquake.lists;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import org.me.gcu.mpdearthquake.models.EarthquakeItem;
import org.me.gcu.mpdearthquake.ui.EarthquakeItemUI;
import org.me.gcu.mpdearthquake.R;
import org.me.gcu.mpdearthquake.viewmodels.EarthquakeListViewModel;

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

    @BindingAdapter({"label", "value"})
    public static void setLabelData(View view, String label, String value) {
        ((TextView) view.findViewById(R.id.labelText)).setText(label);
        ((TextView) view.findViewById(R.id.valueText)).setText(value);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater v;
            v = LayoutInflater.from(context);
            view = v.inflate(R.layout.earthquake_listview, null);
        }

        EarthquakeItem earthquake = getItem(i);

        if (earthquake != null) {
            EarthquakeItemUI.setLocationField(view, earthquake);
            EarthquakeItemUI.setLatitudeField(view, context, earthquake);
            EarthquakeItemUI.setLongitudeField(view, context, earthquake);
            EarthquakeItemUI.setDepthField(view, context, earthquake);
            EarthquakeItemUI.setMagnitudeField(view, context, earthquake);
            EarthquakeItemUI.setDateField(view, context, earthquake);
            EarthquakeItemUI.setCategoryField(view, context, earthquake);
            EarthquakeItemUI.setBackground(view, earthquake);

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
