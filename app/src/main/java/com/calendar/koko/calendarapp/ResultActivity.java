package com.calendar.koko.calendarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.calendar.koko.model.objects.EventObject;
import com.calendar.koko.model.objects.EventSearchObject;

public class ResultActivity extends AppCompatActivity {
    private ListView mainListView ;
    private ArrayAdapter<EventObject> listAdapter ;
    private ResultActivity thisActivity = this;
    private EventSearchObject eventSearchObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Get ListView object from xml
        mainListView= (ListView) findViewById(R.id.listViewrestultsevents);

        eventSearchObject = (EventSearchObject)getIntent().getSerializableExtra("EVENT_LIST");

        // Defined Array values to show in ListView
        // This will be displayed in the listview

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data -
        System.out.println("In ResultActivity, eventSearchObject.getResult() size = "+eventSearchObject.getResult().get(0).get_id());
        listAdapter = new ArrayAdapter<EventObject>(this, R.layout.listrow, R.id.row_event_title, eventSearchObject.getResult());

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
                EventObject itemValue = (EventObject) mainListView.getItemAtPosition(position);

                // Show Alert
               // Toast.makeText(getApplicationContext(), "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();
                Intent eventdetailsview = new Intent( thisActivity , EventDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("EVENT_DETAILS",itemValue);
                eventdetailsview.putExtras(bundle);
                startActivity(eventdetailsview);


            }

        });



    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
