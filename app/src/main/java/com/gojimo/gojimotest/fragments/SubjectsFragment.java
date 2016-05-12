package com.gojimo.gojimotest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gojimo.gojimotest.R;

/**
 * Created by sergio on 12/05/16 for KelpApps.
 */
public class SubjectsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subjects, container, false);

        return rootView;
    }

}
