package com.e.tcl.core;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class coreHoraire {

    private Context ctx;
    private String targetURL = "http://app.tcl.fr/mob_app/appli_mobile/gethorairespoteau/android/REFRESH/";
    private JSONArray json;
    private String error_message = "";
    private List<ligneHolder> liste_lignes;
    private String liste_id_str  = "";


    public coreHoraire(Context ctx){
        this.ctx = ctx;
        coreAssetStorage assetStorage = new coreAssetStorage(this.ctx);
        this.json = assetStorage.getNomStationsLignes();
    }



    public void searchLignes(String search_input, boolean all){
        this.liste_lignes = new ArrayList<>();
        List<String> stations_ajoutee = new ArrayList<>();

        for(int i=0; i<json.length(); i++){

            try {
                JSONObject obj = json.getJSONObject(i);

                String nom_sta = obj.getString("stop_name");
                String nom_sta_lc = nom_sta.toLowerCase();
                search_input = search_input.toLowerCase();

                if(!stations_ajoutee.contains(nom_sta) && nom_sta_lc.contains(search_input.toLowerCase())){

                    String id_sta = obj.getString("stop_id");

                    String nom_ligne = obj.getString("line_name");
                    String nom_vehicule = obj.getString("display_name");
                    String id_ligne = obj.getString("line_id");
                    this.liste_id_str += id_sta + ",";

                    if(!all) stations_ajoutee.add(nom_sta);

                    String type = "Bus";
                    if(nom_vehicule.contains("T"))
                        type = "Tram";
                    if(nom_vehicule.contains("F"))
                        type = "Funiculaire";
                    if(nom_vehicule.contains("[A-D]"))
                        type = "Métro";


                    stationHolder station = new stationHolder(nom_sta, id_sta);
                    this.liste_lignes.add(new ligneHolder(station, nom_ligne, nom_vehicule, type, id_ligne, "-", "-", "-"));
                }


            } catch (JSONException e){
                this.error_message = e.toString();
            }
        }
    }



    public void getHoraire(String search_input){
        searchLignes(search_input, true);

        if(this.error_message.isEmpty() && this.liste_lignes.size() > 0){

            try{
                Connection.Response resp = Jsoup.connect(this.targetURL + this.liste_id_str).ignoreContentType(true).execute();

                if(resp.statusCode() == 200){


                    JSONObject obj = new JSONObject(resp.body());
                    obj = obj.getJSONObject("DATA");

                    for(int i=0; i<this.liste_lignes.size(); i++){
                        ligneHolder curr_ligne = this.liste_lignes.get(i);

                        if(obj.has(curr_ligne.station.id)){
                            JSONObject sta_obj = obj.getJSONObject(curr_ligne.station.id);

                            if(sta_obj.has(curr_ligne.ID)){
                                JSONObject veh_obj = sta_obj.getJSONArray(curr_ligne.ID).getJSONObject(0);
                                if(veh_obj.has("passage1")){
                                    curr_ligne.horaire1 = veh_obj.getString("passage1");
                                }
                                if(veh_obj.has("passage2")) {
                                    curr_ligne.horaire2 = veh_obj.getString("passage2");
                                }
                                if(veh_obj.has("destination")) {
                                    curr_ligne.destination = veh_obj.getString("destination");
                                }

                            }

                        }
                    }


                } else {
                    this.error_message = "status code: " + resp.statusCode();
                }
            } catch (IOException | JSONException e){
                this.error_message = e.toString();
                Log.i("toto7", this.error_message);
            }

        }

    }



    public List<ligneHolder> getListeLignes(){
        return liste_lignes;
    }


    public class stationHolder{
        public String id;
        public String nom;

        public stationHolder(String nom, String id) {
            this.nom = nom;
            this.id = id;
        }
    }


    public class ligneHolder{
        public stationHolder station;
        public String nom;
        public String nom_vehicule;
        public String type_vehicule;

        public String ID;

        public String destination;
        public String horaire1;
        public String horaire2;

        public ligneHolder(stationHolder station,
                           String nom,
                           String nom_vehicule,
                           String type_vehicule,
                           String ID,
                           String destination,
                           String horaire1,
                           String horaire2) {
            this.station = station;
            this.nom = nom;
            this.nom_vehicule = nom_vehicule;
            this.type_vehicule = type_vehicule;
            this.ID = ID;
            this.destination = destination;
            this.horaire1 = horaire1;
            this.horaire2 = horaire2;
        }
    }
}
