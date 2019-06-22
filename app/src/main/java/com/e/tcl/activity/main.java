package com.e.tcl.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.e.tcl.R;
import com.e.tcl.fragments.alertes;
import com.e.tcl.fragments.horaire;
import com.e.tcl.fragments.parcRelais;

public class main extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new horaire());
    }



    private void loadFragment(Fragment frag){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transac = manager.beginTransaction();
        transac.replace(R.id.fragment_container, frag).addToBackStack(null).commit();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_tram:
                    loadFragment(new horaire());
                    return true;
                case R.id.nav_park:
                    loadFragment(new parcRelais());
                    return true;
                case R.id.nav_alert:
                    loadFragment(new alertes());
                    return true;
            }
            return false;
        }
    };

}
