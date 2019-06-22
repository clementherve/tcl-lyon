package com.e.tcl.async;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.e.tcl.R;
import com.e.tcl.core.coreAlertes;

import java.util.List;

public class asyncAlertes extends AsyncTask {

    private Context ctx;
    private Activity a;
    private coreAlertes _coreAlertes;

    public asyncAlertes(Context ctx, Activity a) {
        this.ctx = ctx;
        this.a = a;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        this._coreAlertes = new coreAlertes(this.ctx);
        this._coreAlertes.getAlerts();

        return null;
    }


    @Override
    protected void onPostExecute(Object o) {

        final List<coreAlertes.alert_holder> liste_alertes = this._coreAlertes.getListeAlertes();

        if(liste_alertes != null){
            ListView lv = this.a.findViewById(R.id.alert_list_view);
            lv.setAdapter(new adapter(this.ctx, R.layout.item_alert, liste_alertes));
            lv.setDivider(null);

            lv.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Dialog dialog = new Dialog(ctx);
                    asyncAlertesDetail _async = new asyncAlertesDetail(ctx, dialog, liste_alertes.get(position).getID());
                    _async.execute();

                }
            });
        }


        super.onPostExecute(o);
    }



    public class adapter extends ArrayAdapter{

        private int res;
        private Context ctx;
        private List<coreAlertes.alert_holder> liste_alertes;

        public adapter(Context context, int resource, List<coreAlertes.alert_holder> liste_alertes) {
            super(context, resource, liste_alertes);

            this.ctx = context;
            this.res = resource;
            this.liste_alertes = liste_alertes;

        }



        @Override
        public int getCount() {
            return this.liste_alertes.size();
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(this.ctx);
                convertView = inflater.inflate(this.res, parent, false);
            }

            coreAlertes.alert_holder curr_alert = this.liste_alertes.get(position);

            ((TextView) convertView.findViewById(R.id.nom_ligne_alert)).setText(curr_alert.getLigne());
            ((TextView) convertView.findViewById(R.id.endpoint_alert)).setText(curr_alert.getEndpoints());
            ((TextView) convertView.findViewById(R.id.type_ligne_alert)).setText(curr_alert.getType());

            return convertView;
        }
    }

}
