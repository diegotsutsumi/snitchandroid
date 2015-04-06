package com.alieeen.snitch.walkthrougfrags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alieeen.snitch.R;

/**
 * Created by alinekborges on 26/03/15.
 */
public class Walkthrough02Fragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitiesFragment.
     */

    public static Walkthrough02Fragment newInstance() {
        Walkthrough02Fragment fragment = new Walkthrough02Fragment();
        return fragment;
    }

    public Walkthrough02Fragment() {
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
        View v = inflater.inflate(R.layout.walkthrough02, container, false);

        return v;
    }


}
