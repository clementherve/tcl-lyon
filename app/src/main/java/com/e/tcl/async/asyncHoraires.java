package com.e.tcl.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.e.tcl.R;
import com.e.tcl.core.coreHoraire;

import java.util.ArrayList;
import java.util.List;

public class asyncHoraires extends AsyncTask {

    private Context ctx;
    private Activity a;
    private String search_input;
    private String mode;
    private coreHoraire _coreHoraire;
    private List<coreHoraire.ligneHolder> liste_lignes = new ArrayList<>();

    public asyncHoraires(Context ctx, Activity a, String search_input,  String mode){
        this.ctx = ctx;
        this.a = a;
        this.search_input = search_input;
        this.mode = mode;
    }


    @Override
    protected Object doInBackground(Object[] objects){




        this._coreHoraire = new coreHoraire(this.ctx);
        if(this.mode == "search"  && this.search_input.length() > 3){
            this._coreHoraire.searchLignes(this.search_input, false);
        } else if(this.mode == "res"){
            this._coreHoraire.getHoraire(this.search_input);
        }

        this.liste_lignes = this._coreHoraire.getListeLignes();

        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        ListView lv = a.findViewById(R.id.horaire_listview);

        if(mode == "search" && this.search_input.length() > 3){

            adapterLigne _adapter = new adapterLigne(this.ctx, R.layout.item_horaire, this.liste_lignes);

            lv.setAdapter(_adapter);
            lv.setDivider(null);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EditText search = a.findViewById(R.id.search_station);
                    search.setText(liste_lignes.get(position).station.nom);
                    asyncHoraires _async = new asyncHoraires(ctx, a, liste_lignes.get(position).station.nom, "res");
                    _async.execute();

                }
            });

        } else if(mode == "res"){
            adapterHoraire _adapter = new adapterHoraire(this.ctx, R.layout.item_horaire, this.liste_lignes);
            lv.setAdapter(_adapter);
            lv.setDivider(null);
        }

    }




    private class adapterHoraire extends ArrayAdapter{


        private Context ctx;
        private int res;
        private List<coreHoraire.ligneHolder> liste_ligne;

        public adapterHoraire(Context context, int resource, List<coreHoraire.ligneHolder> liste_lignes) {
            super(context, resource, liste_lignes);
            this.ctx = context;
            this.res = resource;
            this.liste_ligne = liste_lignes;
        }

        @Override
        public int getCount() {
            return liste_ligne.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(this.ctx);
                convertView = inflater.inflate(this.res, parent, false);
            }

            coreHoraire.ligneHolder curr_ligne = this.liste_ligne.get(position);

            TextView nom_sta = convertView.findViewById(R.id.horaire_nom_station);
            TextView nom_ligne = convertView.findViewById(R.id.horaire_nom_ligne);
            TextView h1 = convertView.findViewById(R.id.horaire_1);
            TextView h2 = convertView.findViewById(R.id.horaire_2);

            nom_sta.setText(curr_ligne.station.nom);
            nom_ligne.setText(curr_ligne.type_vehicule + " " + curr_ligne.nom_vehicule + " vers " + curr_ligne.destination);
            h1.setText(curr_ligne.horaire1);
            h2.setText(curr_ligne.horaire2);


            return convertView;
        }




    }



    private class adapterLigne extends ArrayAdapter{

        private Context ctx;
        private int res;
        private List<coreHoraire.ligneHolder> liste_ligne;

        public adapterLigne(Context context, int resource, List<coreHoraire.ligneHolder> liste_stations) {
            super(context, resource, liste_stations);

            this.ctx = context;
            this.res = resource;
            this.liste_ligne = liste_stations;
        }

        @Override
        public int getCount() {
            return liste_ligne.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(this.ctx);
                convertView = inflater.inflate(this.res, parent, false);
            }

            coreHoraire.ligneHolder curr_ligne = this.liste_ligne.get(position);

            TextView nom_sta = convertView.findViewById(R.id.horaire_nom_station);
            TextView nom_ligne = convertView.findViewById(R.id.horaire_nom_ligne);
//            TextView h1 = convertView.findViewById(R.id.horaire_1);
//            TextView h2 = convertView.findViewById(R.id.horaire_2);

            nom_sta.setText(curr_ligne.station.nom);
            nom_ligne.setText(curr_ligne.type_vehicule + " " + curr_ligne.nom_vehicule + ", ligne " + curr_ligne.nom);
//            h1.setText(curr_sta.lignes.get(position).horaire1);
//            h2.setText(curr_sta.lignes.get(position).horaire2);


            return convertView;
        }

    }
}
