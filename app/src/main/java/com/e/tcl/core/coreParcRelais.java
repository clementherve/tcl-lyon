package com.e.tcl.core;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class coreParcRelais {

    private String targetURL = "http://www.tcl.fr/Decouvrir-TCL/Tous-les-services-TCL/Les-parcs-relais";
    private String error_message = "";

    private List<parc_relai_struct> liste_parc_relai = new ArrayList<>();


    public coreParcRelais(){}


    public String getErrorMessage() {
        return error_message;
    }

    public boolean getData(){
        try{

            Connection.Response resp = Jsoup.connect(this.targetURL).ignoreContentType(true).execute();
            if(resp.statusCode() == 200){
                Document dom = resp.parse();
                List<Element> list_div = dom.getElementsByClass("PARC-infos");

                for(int i=0; i<list_div.size(); i++){
                    Element elt = list_div.get(i);

                    String name = elt.select("h3").text();
                    String nb_place_tot = elt.select("span").get(0).text();
                    String nb_place_handi = elt.select("span").get(1).text();
                    String nb_place_dispo = elt.select(".PARC-relais-dispo span").text();
                    String addr = elt.select(".PARC-relais-addr").text();

                    liste_parc_relai.add(new parc_relai_struct(name, nb_place_tot, nb_place_handi, nb_place_dispo, addr));

                }

                return true;
            }
            return false;
        } catch (IOException e){
            this.error_message = e.toString();
            return false;
        }
    }


    public List<parc_relai_struct> getParcsRelais() {
        return liste_parc_relai;
    }

    public class parc_relai_struct {

        private String name;
        private String nb_place_tot;
        private String nb_place_handi;
        private String nb_place_libre;
        private String addr;

        public parc_relai_struct(String name, String nb_place_tot, String  nb_place_handi, String nb_place_libre, String addr) {
            this.name = name;
            this.nb_place_tot = nb_place_tot;
            this.nb_place_handi = nb_place_handi;
            this.nb_place_libre = nb_place_libre;
            this.addr = addr;
        }

        public String getName() {
            return name;
        }

        public String getNb_place_tot() {
            return nb_place_tot;
        }

        public String getNb_place_handi() {
            return nb_place_handi;
        }

        public String getNb_place_libre() {
            return nb_place_libre.isEmpty()? "?":nb_place_libre;
        }

        public String getAddr() {
            return addr;
        }
    }
}