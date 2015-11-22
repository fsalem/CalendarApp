package com.calendar.koko.calendarapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SynchEevnts extends AppCompatActivity {

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synch_eevnts);
    }

    //synch to google
    public void synchtoGoogle(View v){
        // TODO handle synch to google logic

        /*

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {"koko.calendar.noreply@gmail.com", "com.google",
                "koko.calendar.noreply@gmail.com"};
        cur = cr.query(null, EVENT_PROJECTION, selection, selectionArgs, null);
        */

        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, 0);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);

    }

    //synch from google
    public void synchfromGoogle(View v){
        // TODO handle synch from google logic

        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
                .buildUpon();
        //ContentUris.appendId(eventsUriBuilder, timeNow);
        //ContentUris.appendId(eventsUriBuilder, endOfToday);
        Uri eventsUri = eventsUriBuilder.build();
        Cursor cursor = null;
        cursor = getContentResolver().query(eventsUri, EVENT_PROJECTION, null, null, CalendarContract.Instances.DTSTART + " ASC");
        while (cursor.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cursor.getLong(PROJECTION_ID_INDEX);
            displayName = cursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            System.out.println("Calendar Id = "+calID);
            System.out.println("Calendar name = " + displayName);
            System.out.println("Account name = " + accountName);
            System.out.println("Owner name = " + ownerName);
        }

        // TODO pass results to intent listView, created below ----- to show them in a list

        //Intent listView = new Intent( this, ResultActivity.class);
        //startActivity(listView);
    }
}
