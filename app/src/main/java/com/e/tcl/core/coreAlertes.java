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





public class coreAlertes {
    private String targetURL = "http://app.tcl.fr/mob_app/perturbations";
    private String error_message = "";
//    private JSONArray json;
    private List<alert_holder> liste_alertes = new ArrayList<>();
    private coreAssetStorage _storage;
    private Context ctx;



    public coreAlertes(Context ctx) {
        this.ctx = ctx;
        this._storage = new coreAssetStorage(ctx);
//        this.json = this._storage.getNomStationsLignes();
    }


    public boolean getAlerts(){
        try {
            Connection.Response resp = Jsoup.connect(this.targetURL).ignoreContentType(true).execute();

            if(resp.statusCode() == 200){

                JSONObject json = new JSONObject(resp.body());

                JSONArray mtf = json.getJSONArray("mtf");
                JSONArray ligne_forte = json.getJSONArray("c");

                if(mtf != null && mtf.length() > 0){
                    for(int i=0; i<mtf.length(); i++){


                        String id = mtf.getString(i);
                        JSONObject data = this._storage.findByLigneID(id);
                        String type = "Métro";
                        String line = data.getString("display_name");
                        String endpoints = data.getString("line_name");

                        if(line.contains("T"))
                            type = "Tram";
                        if(line.contains("F"))
                            type = "Funiculaire";

                        this.liste_alertes.add(new alert_holder(id, line, endpoints, type, "", ""));
                    }
                }


                if(ligne_forte != null && ligne_forte.length() > 0){
                    for(int i=0; i<ligne_forte.length(); i++){
                        String id = ligne_forte.getString(i);

                        JSONObject data = this._storage.findByLigneID(id);
                        String type = "Bus";
                        String line = data.getString("display_name");
                        String endpoints = data.getString("line_name");
                        endpoints = endpoints.replace("\\", "");

                        this.liste_alertes.add(new alert_holder(id, line, endpoints, type, "", ""));
                    }
                }

            } else {
                this.error_message = "status code: " + resp.statusCode();
            }

        } catch (IOException | JSONException e){
            this.error_message = e.toString();
        }

        Log.i("toto", this.error_message);

        return false;
    }


    public alert_holder getAlertDetail(String ligneID){
        try {
            Connection.Response resp = Jsoup.connect(this.targetURL + "/" + ligneID).ignoreContentType(true).execute();

            if(resp.statusCode() == 200){

                JSONArray json = new JSONArray(resp.body());

                if(json.length() > 0){
                    String title = json.getJSONObject(0).getString("title");
                    String content = json.getJSONObject(0).getString("details");
                    return new alert_holder(ligneID, "", "", "", title, content);
                }

            } else {
                this.error_message = "status code: " + resp.statusCode();
            }

        } catch (IOException | JSONException e){
            this.error_message = e.toString();
        }

        return null;
    }


    public List<alert_holder> getListeAlertes(){
        return this.liste_alertes;
    }

    public String getErrorMessage(){
        return this.error_message;
    }

    public class alert_holder {
        public String id;
        public String ligne;
        public String endpoints;
        public String type;
        public String nom_alert;
        public String detail_alert;

        public alert_holder(String id, String ligne, String endpoints, String type, String nom_alert, String detail_alert) {
            this.id = id;
            this.ligne = ligne;
            this.endpoints = endpoints;
            this.type = type;
            this.nom_alert = nom_alert;
            this.detail_alert = detail_alert;
        }

        public String getID() {
            return id;
        }

        public String getLigne() {
            return ligne;
        }

        public String getEndpoints() {
            return endpoints;
        }

        public String getType() {
            return type;
        }

        public String getNom_alert() {
            return nom_alert;
        }

        public String getDetail_alert() {
            return detail_alert;
        }
    }
}
