package org.me.gcu.mpdearthquake.fragments;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import org.me.gcu.mpdearthquake.EarthquakeItem;
import org.me.gcu.mpdearthquake.EarthquakeListViewModel;
import org.me.gcu.mpdearthquake.R;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class MapFragment extends Fragment {

    private MapView mapView;
    private ArrayList<EarthquakeItem> earthquakes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        EarthquakeListViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(EarthquakeListViewModel.class);
        itemViewModel.getEarthquakes().observe(getViewLifecycleOwner(), items -> {
            earthquakes = items;
        });

        mapView = rootView.findViewById(R.id.earthquakeMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(
            mapboxMap -> {
                mapboxMap.addOnMapClickListener(point -> {
                    rootView.findViewById(R.id.detailsPanel).setVisibility(View.GONE);
                    return false;
                });
                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/coraaa/ckmja6izd233q18qvhw0ksnyf"),
                    style -> {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        style.addImage("red", Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_mapbox_marker_icon_red)));
                        style.addImage("green", Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_mapbox_marker_icon_green)));
                        style.addImage("amber", Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_mapbox_marker_icon_amber)));

                        for (EarthquakeItem earthquake : earthquakes) {
                            SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
                            symbolManager.setIconAllowOverlap(true);
                            symbolManager.setIconIgnorePlacement(true);

                            String color;
                            if (earthquake.getMagnitude() < 1.0)
                                color = "green";
                            else if (earthquake.getMagnitude() >= 1.0 && earthquake.getMagnitude() < 3.0)
                                color = "amber";
                            else
                                color = "red";

                            symbolManager.create(new SymbolOptions()
                                    .withLatLng(new LatLng(earthquake.getLat(), earthquake.getLon()))
                                    .withIconImage(color)
                                    .withIconSize(1.2f));
                            symbolManager.addClickListener(symbol1 -> {
                                rootView.findViewById(R.id.detailsPanel).setVisibility(View.VISIBLE);
                                TextView magnitudeText = rootView.findViewById(R.id.selectedMagnitude);
                                TextView depthText = rootView.findViewById(R.id.selectedDepth);
                                TextView detailsText = rootView.findViewById(R.id.selectedDetails);
                                TextView locationText = rootView.findViewById(R.id.selectedLocation);

                                magnitudeText.setText(getString(R.string.earthquake_magnitude, earthquake.getMagnitude()));
                                depthText.setText(getString(R.string.earthquake_depth, earthquake.getDepth()));
                                detailsText.setText(getString(R.string.earthquake_panel_details, earthquake.getDateToString(), earthquake.getMagnitude(), earthquake.getDepth()));
                                locationText.setText(earthquake.getLocation());
                                return true;
                            });
                        }
                    });
            });

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }
}