/**
 * @author Andreea-Cora Tibuc-Boboc
 * @student_id S1703130
 */

package org.me.gcu.mpdearthquake.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.me.gcu.mpdearthquake.lists.EarthquakeListAdapter;
import org.me.gcu.mpdearthquake.viewmodels.EarthquakeListViewModel;
import org.me.gcu.mpdearthquake.R;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        EarthquakeListViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(EarthquakeListViewModel.class);

        ListView earthquakeListView = rootView.findViewById(R.id.earthquakeListView);
        EarthquakeListAdapter customAdapter = new EarthquakeListAdapter(getActivity(), itemViewModel, getViewLifecycleOwner());
        earthquakeListView.setAdapter(customAdapter);

        return rootView;
    }
}