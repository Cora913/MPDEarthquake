package org.me.gcu.mpdearthquake.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import org.me.gcu.mpdearthquake.EarthquakeItem;
import org.me.gcu.mpdearthquake.EarthquakeListViewModel;
import org.me.gcu.mpdearthquake.R;

import java.util.Objects;

public class EarthquakeDetailsFragment extends Fragment {

    private MapView mapView;
    private EarthquakeItem selectedEarthquake;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_earthquake_details, container, false);

        int earthquakeId = getArguments().getInt("earthquakeId");

        Button viewDetailsBtn = rootView.findViewById(R.id.viewDetails);
        viewDetailsBtn.setVisibility(View.GONE);

        EarthquakeListViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(EarthquakeListViewModel.class);
        itemViewModel.getEarthquakes().observe(getViewLifecycleOwner(), items -> {
            selectedEarthquake = items.get(earthquakeId);

            TextView locationText = rootView.findViewById(R.id.locationText);
            TextView latText = rootView.findViewById(R.id.latText);
            TextView longText = rootView.findViewById(R.id.longText);
            TextView depthText = rootView.findViewById(R.id.depthText);
            TextView magnitudeText = rootView.findViewById(R.id.magnitudeText);
            TextView dateText = rootView.findViewById(R.id.dateText);
            TextView categoryText = rootView.findViewById(R.id.categoryText);

            locationText.setText(selectedEarthquake.getLocation());
            depthText.setText(requireActivity().getString(R.string.earthquake_depth, selectedEarthquake.getDepth()));
            magnitudeText.setText(requireActivity().getString(R.string.earthquake_magnitude, selectedEarthquake.getMagnitude()));
            latText.setText(requireActivity().getString(R.string.earthquake_lat, String.valueOf(selectedEarthquake.getLat())));
            longText.setText(requireActivity().getString(R.string.earthquake_long, String.valueOf(selectedEarthquake.getLon())));
            dateText.setText(requireActivity().getString(R.string.earthquake_date, selectedEarthquake.getDateToString()));
            categoryText.setText(requireActivity().getString(R.string.earthquake_category, selectedEarthquake.getCategory()));

            mapView = rootView.findViewById(R.id.earthquakeMapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(
                mapboxMap -> mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/coraaa/ckmja6izd233q18qvhw0ksnyf"),
                style -> {
                    // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                    style.addImage("red", Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_mapbox_marker_icon_red)));
                    style.addImage("green", Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_mapbox_marker_icon_green)));
                    style.addImage("amber", Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_mapbox_marker_icon_amber)));

                    SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
                    symbolManager.setIconAllowOverlap(true);
                    symbolManager.setIconIgnorePlacement(true);

                    String color;
                    if (selectedEarthquake.getMagnitude() < 1.0)
                        color = "green";
                    else if (selectedEarthquake.getMagnitude() >= 1.0 && selectedEarthquake.getMagnitude() < 3.0)
                        color = "amber";
                    else
                        color = "red";

                    LatLng coords = new LatLng(selectedEarthquake.getLat(), selectedEarthquake.getLon());
                    symbolManager.create(new SymbolOptions()
                        .withLatLng(coords)
                        .withIconImage(color)
                        .withIconSize(1.2f));
                    CameraPosition position = new CameraPosition.Builder()
                        .target(coords)
                        .zoom(6.5)
                        .build();
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 800);
                }
            ));
        });

        return rootView;
    }
}