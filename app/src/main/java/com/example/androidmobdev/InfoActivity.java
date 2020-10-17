package com.example.androidmobdev;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InfoActivity extends AppCompatActivity {
    public static String TAG = "InfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);                                          //imposto il layout
        Log.d(TAG,"onCreate() called !");

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().add(R.id.container, new InfoFragment()).commit();    //retrocompatibilità, transazioni si gestiscono tramite fragment manager, inserisco il mio fragment nella transaction, poi commit
            //r.id.container è l'id del frame layout in cui inserire il fragment
        }

        setupToolBar();

    }

    private void setupToolBar(){
        //ToolBar and ActionBar Settings
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

}