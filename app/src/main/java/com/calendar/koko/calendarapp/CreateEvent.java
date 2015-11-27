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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.calendar.koko.model.Parser;
import com.calendar.koko.model.objects.ConfirmObject;
import com.calendar.koko.model.objects.CreateEventObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CreateEvent extends AppCompatActivity {

    private CreateEventTask createEventTask = null;
    private EditText eventName;
    private EditText eventDesc;
    private EditText location;
    private EditText notify;
    private DatePickerFragment startDatePickerFragment;
    private TimePickerFragment startTimePickerFragment;
    private DatePickerFragment endDatePickerFragment;
    private TimePickerFragment endTimePickerFragment;
    private DatePickerFragment notifyDatePickerFragment;
    private TimePickerFragment notifyTimePickerFragment;


    private TextView startDateLabel;
    private TextView startTimeLabel;
    private TextView endDateLabel;
    private TextView endTimeLabel;
    private TextView notifyDateLabel;
    private TextView notifyTimeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getApplicationContext() == null || !(getApplicationContext() instanceof LoginCredentialsApplication) || ((LoginCredentialsApplication) getApplicationContext()).getEmail() == null){
            Intent home = new Intent(this,LoginActivity.class);
            startActivity(home);
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventName = (EditText) findViewById(R.id.u_eventname_text);
        eventDesc = (EditText) findViewById(R.id.u_event_desc_text);
        location = (EditText) findViewById(R.id.u_event_location_text);
        notify = (EditText) findViewById(R.id.u_notify_text);

        startDateLabel = (TextView) findViewById(R.id.c_s_date_label);
        startTimeLabel = (TextView) findViewById(R.id.c_s_time_label);
        endDateLabel = (TextView) findViewById(R.id.c_e_date_label);
        endTimeLabel = (TextView) findViewById(R.id.c_e_time_label);
        notifyDateLabel = (TextView) findViewById(R.id.c_n_date_label);
        notifyTimeLabel = (TextView) findViewById(R.id.c_n_time_label);

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
        startTimePickerFragment = new TimePickerFragment();
        startTimePickerFragment.label = startTimeLabel;
        startTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Show time Picker
    public void showEndTimePickerDialog(View v) {
        endTimePickerFragment = new TimePickerFragment();
        endTimePickerFragment.label = endTimeLabel;
        endTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Show time Picker
    public void showNotifyTimePickerDialog(View v) {
        notifyTimePickerFragment = new TimePickerFragment();
        notifyTimePickerFragment.label = notifyTimeLabel;
        notifyTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        int year,month,day;
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
        startDatePickerFragment= new DatePickerFragment();
        startDatePickerFragment.label = startDateLabel;
        startDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //show date picker
    public void showEndDatePickerDialog(View v) {
        endDatePickerFragment = new DatePickerFragment();
        endDatePickerFragment.label = endDateLabel;
        endDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //show date picker
    public void showNotifyDatePickerDialog(View v) {
        notifyDatePickerFragment = new DatePickerFragment();
        notifyDatePickerFragment.label = notifyDateLabel;
        notifyDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void reset(){
        createEventTask = null;
    }

    //Create an event
    public void createEvent(View v){
        if (createEventTask != null) {
            return;
        }
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+2"));

        LoginCredentialsApplication credentialsApplication = (LoginCredentialsApplication) getApplicationContext();

        CreateEventObject eventObject = new CreateEventObject();
        eventObject.setEmail(credentialsApplication.getEmail());
        eventObject.setPassword(credentialsApplication.getPassword());
        eventObject.setName(eventName.getText().toString());
        eventObject.setDesc(eventDesc.getText().toString());
        eventObject.setLocation(location.getText().toString());
        eventObject.setNotify(notify.getText().toString().equals("1") ? true : false);
        if (startDatePickerFragment != null && startDatePickerFragment.year != 0) {
            cal.set(startDatePickerFragment.year, startDatePickerFragment.month, startDatePickerFragment.day, startTimePickerFragment.hourOfDay, startTimePickerFragment.minute, 0);
            eventObject.setsDate(cal.getTime().getTime());
        }
        if (endDatePickerFragment != null && endDatePickerFragment.year != 0) {
            cal.set(endDatePickerFragment.year, endDatePickerFragment.month, endDatePickerFragment.day, endTimePickerFragment.hourOfDay, endTimePickerFragment.minute, 0);
            eventObject.seteDate(cal.getTime().getTime());
        }
        if (notifyDatePickerFragment != null && notifyDatePickerFragment.year != 0) {
            cal.set(notifyDatePickerFragment.year, notifyDatePickerFragment.month, notifyDatePickerFragment.day, notifyTimePickerFragment.hourOfDay, notifyTimePickerFragment.minute, 0);
            eventObject.setnDate(cal.getTime().getTime());
        }else{
            eventObject.setnDate(0L);
        }
        System.out.println("Dates=> "+eventObject.getsDate()+", "+eventObject.getsDate()+", "+eventObject.geteDate());
        createEventTask = new CreateEventTask(eventObject,this);
        createEventTask.execute((Void) null);
    }

    public class CreateEventTask extends AsyncTask<Void, Void, Boolean> {

        private final CreateEventObject createEventObject;
        private final AppCompatActivity mActivity;

        CreateEventTask(CreateEventObject createEventObject,AppCompatActivity activity) {
            this.createEventObject = createEventObject;
            mActivity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ConfirmObject confirmObject = Parser.createEvent(createEventObject);
                System.out.print("Result in createEvent from Parser = "+confirmObject);
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
            AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
            alertDialog.setTitle("Status");
            if (success) {
                alertDialog.setMessage(getResources().getString(R.string.event_creation_success));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(CreateEvent.this, Home.class);
                                startActivity(intent);
                                dialog.dismiss();
                                finish();
                            }
                        });
            } else {
                alertDialog.setMessage(getResources().getString(R.string.event_creation_failed));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
            alertDialog.show();
            reset();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

