package com.e.tcl.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.e.tcl.R;
import com.e.tcl.async.asyncHoraires;


public class horaire extends Fragment {


    private String search_input;

    public horaire(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_horaire, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        final EditText search = getActivity().findViewById(R.id.search_station);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void afterTextChanged(Editable s){}



            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search_input = search.getText().toString();
                asyncHoraires _async = new asyncHoraires(getContext(), getActivity(), search_input, "search");
                _async.execute();
            }


        });

    }
}
