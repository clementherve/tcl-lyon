package com.e.tcl.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.tcl.R;
import com.e.tcl.async.asyncParcRelais;


public class parcRelais extends Fragment {


    public parcRelais(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        asyncParcRelais _async =  new asyncParcRelais(getContext(), getActivity());
        _async.execute();

        return inflater.inflate(R.layout.fragment_parc_relai, container, false);
    }

}
