package com.calendar.koko.calendarapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class EventsActivity extends AppCompatActivity {
    private RetrieveEventsTask retrieveEventsTask;
    private DatePickerFragment fromDatePickerFragment;
    private TimePickerFragment fromTimePickerFragment;
    private DatePickerFragment toDatePickerFragment;
    private TimePickerFragment toTimePickerFragment;

    private TextView startDateLabel;
    private TextView startTimeLabel;
    private TextView endDateLabel;
    private TextView endTimeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getApplicationContext() == null || !(getApplicationContext() instanceof LoginCredentialsApplication) || ((LoginCredentialsApplication) getApplicationContext()).getEmail() == null){
            Intent home = new Intent(this,LoginActivity.class);
            startActivity(home);
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        startDateLabel = (TextView) findViewById(R.id.list_events_start_date_label);
        startTimeLabel = (TextView) findViewById(R.id.list_events_start_time_label);
        endDateLabel = (TextView) findViewById(R.id.list_events_end_date_label);
        endTimeLabel = (TextView) findViewById(R.id.list_events_end_time_label);
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        int hourOfDay, minute;
        TextView label;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            this.hourOfDay = hourOfDay;
            this.minute = minute;
            label.setText(hourOfDay + ":" + minute);
        }
    }

    //Show time Picker
    public void showStartTimePickerDialog(View v) {
        //Maybe you need to pass paramter to the constructor to handle logic in onTimeSet

        fromTimePickerFragment = new TimePickerFragment();
        fromTimePickerFragment.label = startTimeLabel;
        fromTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Show time Picker
    public void showEndTimePickerDialog(View v) {
        //Maybe you need to pass paramter to the constructor to handle logic in onTimeSet

        toTimePickerFragment = new TimePickerFragment();
        toTimePickerFragment.label = endTimeLabel;
        toTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        int year, month, day;
        TextView label;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
            label.setText(year + "/" + (month+1) + "/" + day);
        }
    }

    //show date picker
    public void showStartDatePickerDialog(View v) {
        //Maybe you need to pass paramter to the constructor to handle logic in onDateSet

        fromDatePickerFragment = new DatePickerFragment();
        fromDatePickerFragment.label = startDateLabel;
        fromDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
  }

    //show date picker
    public void showEndDatePickerDialog(View v) {
        //Maybe you need to pass paramter to the constructor to handle logic in onDateSet

        toDatePickerFragment = new DatePickerFragment();
        toDatePickerFragment.label = endDateLabel;
        toDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }



    //Create an event
    public void listAllEvents(View v){
        if (retrieveEventsTask != null){
            return;
        }
        LoginCredentialsApplication credentialsApplication = (LoginCredentialsApplication) getApplicationContext();

        SearchObject searchObject = new SearchObject(credentialsApplication.getEmail(),credentialsApplication.getPassword());
        if ((fromDatePickerFragment != null && fromDatePickerFragment.year != 0) &&
                (fromTimePickerFragment != null) &&
                (toDatePickerFragment != null && toDatePickerFragment.year != 0) &&
                (toTimePickerFragment != null)){

            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+2"));
            cal.set(fromDatePickerFragment.year, fromDatePickerFragment.month, fromDatePickerFragment.day, fromTimePickerFragment.hourOfDay, fromTimePickerFragment.minute, 0);
            searchObject.setStartDate(cal.getTime().getTime());

            cal.set(toDatePickerFragment.year, toDatePickerFragment.month, toDatePickerFragment.day, toTimePickerFragment.hourOfDay, toTimePickerFragment.minute, 0);
            searchObject.setEndDate(cal.getTime().getTime());
        }
        retrieveEventsTask = new RetrieveEventsTask(searchObject,this);
        retrieveEventsTask.execute((Void) null);
    }
    private void reset(){
        retrieveEventsTask = null;
        fromDatePickerFragment = null;
        fromTimePickerFragment = null;
        toDatePickerFragment = null;
        toTimePickerFragment = null;
    }

    public class RetrieveEventsTask extends AsyncTask<Void, Void, Boolean> {

        private final SearchObject searchObject;
        private final AppCompatActivity mActivity;
        private EventSearchObject events;
        private EventSearchPeriodObject periodEvents;
        RetrieveEventsTask(SearchObject searchObject,AppCompatActivity activity) {
            this.searchObject = searchObject;
            mActivity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (searchObject.getStartDate() != null && searchObject.getEndDate() != null){
                    periodEvents = Parser.retreiveEventsWithinPeriod(searchObject.getStartDate(),searchObject.getEndDate(),searchObject.getEmail(),searchObject.getPassword());
                    events = new EventSearchObject();
                    events.setSuccess(periodEvents.getSuccess());
                    events.setError(periodEvents.getError());
                    events.setResult(new ArrayList<EventObject>());
                    for (EventPeriodResultObject object:periodEvents.getResult()){
                        events.getResult().add(object.getEvents());
                    }

                }else{
                    events = Parser.retreiveAllEvents(searchObject.getEmail(),searchObject.getPassword());
                }
                //System.out.println("Result in EventsActivity from Parser = " + events.getResult().size());
                if (events != null && events.getSuccess() != null) {
                    return true ? events.getSuccess() == 1 : false;
                }
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
                reset();
            }else {
                Intent listView = new Intent(mActivity, ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("EVENT_LIST", events);
                listView.putExtras(bundle);
                startActivity(listView);
                finish();
                reset();
            }
        }

        @Override
        protected void onCancelled() {
            reset();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
