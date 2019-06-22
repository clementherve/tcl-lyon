package com.e.tcl.core;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;



public class coreAssetStorage {

    private Context ctx;
    private JSONArray json;
    private String error_message = "";

    public coreAssetStorage(Context ctx){

        this.ctx = ctx;
        this.json = getNomStationsLignes();

    }


    public JSONArray getNomStationsLignes(){
        try{
            return new JSONArray(getFileContent("noms_stations_lignes.json"));
        } catch (JSONException e){
            return null;
        }
    }



    public JSONObject findByLigneID(String line_id){
        JSONObject resp = null;
        try{
            for(int i=0; i<this.json.length(); i++){
                if(this.json.getJSONObject(i).getString("line_id").equals(line_id)){
                    resp = this.json.getJSONObject(i); break;
                }
            }
        } catch (JSONException e){
            this.error_message = e.toString();
        }
        return resp;
    }


    public JSONObject findByStationID(String station_id){
        JSONObject resp = null;
        try{
            for(int i=0; i<this.json.length(); i++){
                if(this.json.getJSONObject(i).getString("stop_id").equals(station_id)){
                    resp = this.json.getJSONObject(i); break;
                }
            }
        } catch (JSONException e){
            this.error_message = e.toString();
        }
        return resp;
    }




    private String getFileContent(String fname) {
        try {
            InputStream is = ctx.getResources().getAssets().open(fname);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new String(buffer);

        } catch (IOException e) {
            return "";
        }
    }

}
