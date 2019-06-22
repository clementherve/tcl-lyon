package com.e.tcl.async;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.TextView;

import com.e.tcl.R;
import com.e.tcl.core.coreAlertes;

public class asyncAlertesDetail extends AsyncTask {

    private Context ctx;
    private coreAlertes _coreAlertes;
    private Dialog d;
    private String ligne;
    private coreAlertes.alert_holder details;

    public asyncAlertesDetail(Context ctx, Dialog d, String ligne){
        this.ctx = ctx;
        this.d = d;
        this.ligne = ligne;
    }


    @Override
    protected Object doInBackground(Object[] objects) {


        this._coreAlertes = new coreAlertes(this.ctx);
        this.details = this._coreAlertes.getAlertDetail(this.ligne);


        return null;
    }


    @Override
    protected void onPostExecute(Object o) {

        d.setContentView(R.layout.dialog_alert);

        WebView w = d.findViewById(R.id.alert_content);
        TextView t = d.findViewById(R.id.alert_title);

        if(this.details != null){
            t.setText(this.details.getNom_alert());
            w.loadData(this.details.getDetail_alert(), "text/html", "utf-8");
        } else {
            t.setText("");
            w.loadData(this._coreAlertes.getErrorMessage(), "text/html", "utf-8");
        }

        d.show();

    }
}
