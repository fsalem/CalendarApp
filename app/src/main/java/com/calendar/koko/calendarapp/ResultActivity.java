package com.calendar.koko.calendarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private ResultActivity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Get ListView object from xml
        mainListView= (ListView) findViewById(R.id.listViewrestultsevents);


        Intent myintent = getIntent();

        // TODO get  object from intent ... represents array of events ...this object contians results list - should be sent from events activity
        //MyObject obj =  (MyObject) myintent.getParcelableExtra("name_of_extra");

        // Defined Array values to show in ListView
        String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        // TODO you should have array of objects - events and each obeject's implments toString() method to return the title
        // This will be displayed in the listview

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data - TODO replace values with your array and comment the dummy data - values used for testing

        listAdapter = new ArrayAdapter<String>(this, R.layout.listrow, R.id.row_event_title, values);

        // Assign adapter to ListView
        mainListView.setAdapter(listAdapter);


        // ListView Item Click Listener
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) mainListView.getItemAtPosition(position);

                // Show Alert
               // Toast.makeText(getApplicationContext(), "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();

                // TODO get corrsponding eventobject based on the itemposition clicked
                // TODO pass this eventObject to the next activty - event details to should event information

                Intent eventdetailsview = new Intent( thisActivity , EventDetails.class);
                startActivity(eventdetailsview);


            }

        });


    }
}
