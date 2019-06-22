package com.e.tcl.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.e.tcl.R;
import com.e.tcl.core.coreParcRelais;

import java.util.ArrayList;
import java.util.List;

public class asyncParcRelais extends AsyncTask {

    private Context ctx;
    private Activity a;
    private coreParcRelais _parc_relai;
    private String error_message = "";
    private List<coreParcRelais.parc_relai_struct> liste_parc_relai = new ArrayList<>();

    public asyncParcRelais(Context ctx, Activity a){
        this.ctx = ctx;
        this.a = a;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        this._parc_relai = new coreParcRelais();

        if(this._parc_relai.getData()){
            this.liste_parc_relai = this._parc_relai.getParcsRelais();
        }

        this.error_message = this._parc_relai.getErrorMessage();

        return null;
    }


    @Override
    protected void onPostExecute(Object o){

        if(this.error_message.isEmpty()){
            adapter _adapter = new adapter(this.ctx, R.layout.item_parc_relai, this.liste_parc_relai);
            ListView lv = this.a.findViewById(R.id.parc_relai_listview);
            lv.setAdapter(_adapter);
            lv.setDivider(null);

        } else {
            Log.i("toto", this.error_message);
        }

    }


    private class adapter extends ArrayAdapter {

        private List<coreParcRelais.parc_relai_struct> liste_parc_relai = new ArrayList<>();
        private int item_layout;


        public adapter(Context context, int resource, List<coreParcRelais.parc_relai_struct> liste_parc_relai) {
            super(context, resource, liste_parc_relai);
            this.item_layout = resource;
            this.liste_parc_relai = liste_parc_relai;
        }

        @Override
        public int getCount() {
            return this.liste_parc_relai.size();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(ctx);
                convertView = inflater.inflate(this.item_layout, parent, false);
            }

            coreParcRelais.parc_relai_struct curr_parc = this.liste_parc_relai.get(position);

            ((TextView) convertView.findViewById(R.id.nom_parc_relai)).setText(curr_parc.getName());
            ((TextView) convertView.findViewById(R.id.addr_parc_relai)).setText(curr_parc.getAddr());
            ((TextView) convertView.findViewById(R.id.places_dispo_parc_relai)).setText(curr_parc.getNb_place_libre());

            return convertView;
        }
    }

}
