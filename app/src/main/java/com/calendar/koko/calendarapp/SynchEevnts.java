package com.calendar.koko.calendarapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.calendar.koko.model.Parser;
import com.calendar.koko.model.objects.ConfirmObject;
import com.calendar.koko.model.objects.CreateEventObject;
import com.calendar.koko.model.objects.EventObject;
import com.calendar.koko.model.objects.EventPeriodResultObject;
import com.calendar.koko.model.objects.EventSearchObject;
import com.calendar.koko.model.objects.EventSearchPeriodObject;
import com.calendar.koko.model.objects.SearchObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

@TargetApi(23)
public class SynchEevnts extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Integer MAX_TO_EXPORT = 10;
    private final Integer MAX_TO_IMPORT = 10;
    private List<CreateEventObject> events;
    private Integer addedEventsToSystem = 0;
    private Integer processedEventsToSystem = 0;
    private Integer allEventsToSystem = 0;
    private View syncProgressView;
    private Button syncToGoogleBtn;
    private Button syncFromGoogleBtn;
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Events._ID,                           // 0
            CalendarContract.Events.TITLE,                  // 1
            CalendarContract.Events.DESCRIPTION,                  // 2
            CalendarContract.Events.EVENT_LOCATION,         // 3
            CalendarContract.Events.DTSTART,                  // 4
            CalendarContract.Events.DTEND,                  // 5
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_EVENT_TITLE_INDEX = 1;
    private static final int PROJECTION_EVENT_DESC_INDEX = 2;
    private static final int PROJECTION_EVENT_LOCATION_INDEX = 3;
    private static final int PROJECTION_EVENT_DTSTART_INDEX = 4;
    private static final int PROJECTION_EVENT_DTEND_INDEX = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synch_eevnts);

        syncProgressView = findViewById(R.id.sync_progress);
        syncFromGoogleBtn= (Button) findViewById(R.id.c_synchfromgo_);
        syncToGoogleBtn= (Button) findViewById(R.id.c_synchtogo);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            syncFromGoogleBtn.setVisibility(show ? View.GONE : View.VISIBLE);
            syncFromGoogleBtn.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    syncFromGoogleBtn.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            syncToGoogleBtn.setVisibility(show ? View.GONE : View.VISIBLE);
            syncToGoogleBtn.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    syncToGoogleBtn.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            syncProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            syncProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    syncProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            syncProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            syncToGoogleBtn.setVisibility(show ? View.VISIBLE : View.GONE);
            syncFromGoogleBtn.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }
    //synch to google
    public void synchtoGoogle(View v){

        showProgress(true);
        LoginCredentialsApplication loginCredentialsApplication = (LoginCredentialsApplication) getApplicationContext();
        SearchObject searchObject = new SearchObject(loginCredentialsApplication.getEmail(),loginCredentialsApplication.getPassword());
        RetrieveAllEventsTask retrieveAllEventsTask = new RetrieveAllEventsTask(searchObject,this);
        retrieveAllEventsTask.execute((Void) null);
    }

    //synch from google
    public void synchfromGoogle(View v){
        showProgress(true);
        Uri.Builder eventsUriBuilder = CalendarContract.Events.CONTENT_URI
                .buildUpon();
        //ContentUris.appendId(eventsUriBuilder, timeNow);
        //ContentUris.appendId(eventsUriBuilder, endOfToday);
        Uri eventsUri = eventsUriBuilder.build();
        Cursor cursor = null;
        cursor = getContentResolver().query(eventsUri, EVENT_PROJECTION, null, null, CalendarContract.Instances.DTSTART + " ASC");
        events = new ArrayList<>();
        while (cursor.moveToNext()) {
            if (events.size() >= MAX_TO_IMPORT)break;
            Long eventID = cursor.getLong(PROJECTION_ID_INDEX);
            String title = cursor.getString(PROJECTION_EVENT_TITLE_INDEX);
            String description = cursor.getString(PROJECTION_EVENT_DESC_INDEX);
            String location = cursor.getString(PROJECTION_EVENT_LOCATION_INDEX);
            Long startDate = cursor.getLong(PROJECTION_EVENT_DTSTART_INDEX);
            Long endDate = cursor.getLong(PROJECTION_EVENT_DTEND_INDEX);


            LoginCredentialsApplication credentialsApplication = (LoginCredentialsApplication) getApplicationContext();

            CreateEventObject eventObject = new CreateEventObject();
            eventObject.setEmail(credentialsApplication.getEmail());
            eventObject.setPassword(credentialsApplication.getPassword());
            eventObject.setName(title);
            eventObject.setDesc(description);
            eventObject.setLocation(location);
            eventObject.setNotify(false);
            eventObject.setnDate(0L);
            eventObject.setsDate(startDate);
            eventObject.seteDate(endDate);

            events.add(eventObject);


            //System.out.println("Event Id = " + eventID);
            //System.out.println("Event Title = " + title);
            //System.out.println("Event Desc = " + description);
            //System.out.println("Event Location = " + location);
            //System.out.println("Event StartDate = " + new Date(startDate).toString());
            //System.out.println("Event endDate = " + new Date(endDate).toString());

        }
        allEventsToSystem = events.size();
        processNextEvent();

    }

    private synchronized void processNextEvent(){
        if (events == null || events.isEmpty()){
            if (allEventsToSystem == processedEventsToSystem) {
                System.out.println("ProcessedEvents = " + processedEventsToSystem + " & addedEvents = " + addedEventsToSystem);
                showProgress(false);
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Status");
                alertDialog.setMessage("# of processedEvents = " + processedEventsToSystem + " & # of addedEvents = " + addedEventsToSystem + ".\n\n Go to 'Get Events' to retreieve them");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SynchEevnts.this, EventsActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();
            }
        }else{
            //System.out.println("Processing Event: "+events.get(0).getName());
            CreateMobileEventsTask createMobileEventsTask = new CreateMobileEventsTask(events.get(0),this);
            createMobileEventsTask.execute((Void) null);
            events.remove(0);
        }
    }

    private long getCalendarId() {
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection =
                        CalendarContract.Calendars.ACCOUNT_TYPE +
                        " = ? ";
        // use the same values as above:
        String[] selArgs =
                new String[]{
                        CalendarContract.ACCOUNT_TYPE_LOCAL};
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)
        {
            Cursor cursor =
                    getContentResolver().
                            query(
                                    CalendarContract.Calendars.CONTENT_URI,
                                    projection,
                                    selection,
                                    selArgs,
                                    null);
            if (cursor.moveToFirst()) {
                return cursor.getLong(0);
            }
        }
        return -1;
    }

    private void addToGoogle(EventSearchObject events){
        Uri.Builder eventsUriBuilder = CalendarContract.Events.CONTENT_URI
                .buildUpon();
        //ContentUris.appendId(eventsUriBuilder, timeNow);
        //ContentUris.appendId(eventsUriBuilder, endOfToday);
        Uri eventsUri = eventsUriBuilder.build();
        ContentValues[] values;
        if (MAX_TO_EXPORT >= events.getResult().size()){
            values = new ContentValues[events.getResult().size()];
        }else{
            values = new ContentValues[MAX_TO_EXPORT];
        }

        long calendar_id = getCalendarId();
        int i=0;
        for (EventObject eventObject:events.getResult()){
            if (i >= MAX_TO_EXPORT)break;
            ContentValues value = new ContentValues();
            TimeZone timeZone = TimeZone.getDefault();
            if (eventObject.getTitle() != null) {
                value.put(CalendarContract.Events.TITLE, eventObject.getTitle());
            }
            if (eventObject.getDescription() != null) {
                value.put(CalendarContract.Events.DESCRIPTION, eventObject.getDescription());
            }
            if (eventObject.getStart() != null) {
                value.put(CalendarContract.Events.DTSTART, eventObject.getStart());
            }
            if (eventObject.getEnd() != null) {
                value.put(CalendarContract.Events.DTEND, eventObject.getEnd());
            }
            if (timeZone.getID() != null) {
                value.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
            }
            if (eventObject.getLocation() != null) {
                value.put(CalendarContract.Events.EVENT_LOCATION, eventObject.getLocation());
            }
            value.put(CalendarContract.Events.CALENDAR_ID, calendar_id);

            System.out.println("Event title = " + eventObject.getTitle());
            System.out.println("Event title = " + new Date(eventObject.getStart()).toString());

            values[i++] = value;
        }
        int result = getContentResolver().bulkInsert(eventsUri,values);

        System.out.println("Result is "+result);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Status");
        alertDialog.setMessage(getResources().getString(R.string.event_synToGoogle_success));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        showProgress(false);

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class CreateMobileEventsTask extends AsyncTask<Void, Void, Boolean> {

        private final CreateEventObject event;
        private final AppCompatActivity mActivity;

        CreateMobileEventsTask(CreateEventObject event,AppCompatActivity activity) {
            this.event = event;
            mActivity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //System.out.println("CreateMobileEventsTask: creating creating Google event "+event.getName());
                ConfirmObject confirmObject = Parser.createEvent(event);

                //System.out.println("Status of creating Google event "+event.getName()+" is "+confirmObject.getSuccess());
                return true?confirmObject.getSuccess() == 1:false;
            }catch (IllegalAccessException illegalException){
                illegalException.printStackTrace();
            }catch (InvocationTargetException exception){
                exception.printStackTrace();
            }
            return Boolean.FALSE;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //System.out.println("In CreateMobileEvent.onPostExecute: DONE");
            if (success)++addedEventsToSystem;
            ++processedEventsToSystem;
            processNextEvent();
        }
    }

    public class RetrieveAllEventsTask extends AsyncTask<Void, Void, Boolean> {

        private final SearchObject searchObject;
        private final AppCompatActivity mActivity;
        private EventSearchObject events;
        RetrieveAllEventsTask(SearchObject searchObject,AppCompatActivity activity) {
            this.searchObject = searchObject;
            mActivity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                events = Parser.retreiveAllEvents(searchObject.getEmail(),searchObject.getPassword());
                return true?events.getSuccess() == 1:false;
            }catch (IllegalAccessException illegalException){
                illegalException.printStackTrace();
            }catch (InvocationTargetException exception){
                exception.printStackTrace();
            }
            return Boolean.FALSE;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (events == null || events.getResult() == null || events.getResult().isEmpty()){
                AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
                alertDialog.setTitle("Status");
                alertDialog.setMessage(getResources().getString(R.string.event_retrieve_empty));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                showProgress(false);
            }else {
                addToGoogle(events);
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
