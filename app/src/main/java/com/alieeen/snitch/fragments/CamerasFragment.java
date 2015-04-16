package com.alieeen.snitch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alieeen.snitch.R;
import com.alieeen.snitch.adapter.EventsAdapter;
import com.alieeen.snitch.model.Event;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by alinekborges on 09/04/15.
 */
public class CamerasFragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitiesFragment.
     */

    public static CamerasFragment newInstance() {
        CamerasFragment fragment = new CamerasFragment();
        return fragment;
    }

    public CamerasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cameras, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        EventsAdapter adapter = new EventsAdapter(getActivity(), createMockList(3));
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);
        alphaAdapter.setFirstOnly(false);
        recyclerView.setAdapter(alphaAdapter);
        //recyclerView.setAdapter(new EventsAdapter(getActivity(), createMockList(10)));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return v;
    }

    private List<Event> createMockList(int size) {
        List<Event> objects = new ArrayList<>();
        for (int i = 0 ; i < size ; i++) {
            objects.add(new Event());
        }
        return objects;

    }

}

