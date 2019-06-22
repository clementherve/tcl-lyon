package com.e.tcl.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.tcl.R;
import com.e.tcl.async.asyncAlertes;


public class alertes extends Fragment {

    public alertes(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        asyncAlertes _async = new asyncAlertes(getContext(), getActivity());
        _async.execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alert, container, false);
    }

}
