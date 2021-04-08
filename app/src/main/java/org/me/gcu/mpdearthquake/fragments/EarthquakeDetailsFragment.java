/**
 * @author Andreea-Cora Tibuc-Boboc
 * @student_id S1703130
 */

package org.me.gcu.mpdearthquake.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import org.me.gcu.mpdearthquake.models.EarthquakeItem;
import org.me.gcu.mpdearthquake.ui.EarthquakeItemUI;
import org.me.gcu.mpdearthquake.viewmodels.EarthquakeListViewModel;
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

            EarthquakeItemUI.setLocationField(rootView, selectedEarthquake);
            EarthquakeItemUI.setLatitudeField(rootView, requireActivity(), selectedEarthquake);
            EarthquakeItemUI.setLongitudeField(rootView, requireActivity(), selectedEarthquake);
            EarthquakeItemUI.setDepthField(rootView, requireActivity(), selectedEarthquake);
            EarthquakeItemUI.setMagnitudeField(rootView, requireActivity(), selectedEarthquake);
            EarthquakeItemUI.setDateField(rootView, requireActivity(), selectedEarthquake);
            EarthquakeItemUI.setCategoryField(rootView, requireActivity(), selectedEarthquake);
            EarthquakeItemUI.setBackground(rootView, selectedEarthquake);

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