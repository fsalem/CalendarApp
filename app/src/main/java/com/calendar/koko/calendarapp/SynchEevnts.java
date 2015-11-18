package com.calendar.koko.calendarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SynchEevnts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synch_eevnts);
    }

    //synch to google
    public void synchtoGoogle(View v){
        // TODO handle synch to google logic
    }

    //synch from google
    public void synchfromGoogle(View v){
        // TODO handle synch from google logic

        // TODO pass results to intent listView, created below ----- to show them in a list

        Intent listView = new Intent( this, ResultActivity.class);
        startActivity(listView);
    }
}
