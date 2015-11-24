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
import com.calendar.koko.model.objects.EventObject;
import com.calendar.koko.model.objects.NameValuePair;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class EventDetails extends AppCompatActivity {
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

    private EditText eventName;
    private EditText eventDesc;
    private EditText location;
    private EditText notify;

    private EventObject eventObject;

    private UpdateEventTask updateEventTask;
    private DeleteEventTask deleteEventTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        startDateLabel = (TextView) findViewById(R.id.u_s_date_label);
        startTimeLabel = (TextView) findViewById(R.id.u_s_time_label);
        endDateLabel = (TextView) findViewById(R.id.u_e_date_label);
        endTimeLabel = (TextView) findViewById(R.id.u_e_time_label);
        notifyDateLabel = (TextView) findViewById(R.id.u_n_date_label);
        notifyTimeLabel = (TextView) findViewById(R.id.u_n_time_label);

        eventName = (EditText)findViewById(R.id.u_eventname_text);
        eventDesc = (EditText)findViewById(R.id.u_event_desc_text);
        location = (EditText)findViewById(R.id.u_event_location_text);
        notify = (EditText)findViewById(R.id.u_notify_text);

        eventObject = (EventObject)getIntent().getSerializableExtra("EVENT_DETAILS");
        Calendar startDate = getDate(eventObject.getStart());
        Calendar endDate = getDate(eventObject.getEnd());
        Calendar notDate = getDate(eventObject.getNotificationDate());

        startDatePickerFragment= new DatePickerFragment();
        startDatePickerFragment.label = startDateLabel;
        startDatePickerFragment.year = startDate.get(Calendar.YEAR);
        startDatePickerFragment.month = startDate.get(Calendar.MONTH)+1;
        startDatePickerFragment.day = startDate.get(Calendar.DAY_OF_MONTH);
        startTimePickerFragment = new TimePickerFragment();
        startTimePickerFragment.label = startTimeLabel;
        startTimePickerFragment.hourOfDay = startDate.get(Calendar.HOUR_OF_DAY);
        startTimePickerFragment.minute = startDate.get(Calendar.MINUTE);


        endDatePickerFragment = new DatePickerFragment();
        endDatePickerFragment.label = endDateLabel;
        endDatePickerFragment.year = endDate.get(Calendar.YEAR);
        endDatePickerFragment.month = endDate.get(Calendar.MONTH)+1;
        endDatePickerFragment.day = endDate.get(Calendar.DAY_OF_MONTH);
        endTimePickerFragment = new TimePickerFragment();
        endTimePickerFragment.label = endTimeLabel;
        endTimePickerFragment.hourOfDay = endDate.get(Calendar.HOUR_OF_DAY);
        endTimePickerFragment.minute = endDate.get(Calendar.MINUTE);

        notifyDatePickerFragment = new DatePickerFragment();
        notifyDatePickerFragment.label = notifyDateLabel;
        notifyDatePickerFragment.year = notDate.get(Calendar.YEAR);
        notifyDatePickerFragment.month = notDate.get(Calendar.MONTH)+1;
        notifyDatePickerFragment.day = notDate.get(Calendar.DAY_OF_MONTH);
        notifyTimePickerFragment = new TimePickerFragment();
        notifyTimePickerFragment.label = notifyTimeLabel;
        notifyTimePickerFragment.hourOfDay = notDate.get(Calendar.HOUR_OF_DAY);
        notifyTimePickerFragment.minute = notDate.get(Calendar.MINUTE);


        eventName.setText(eventObject.getTitle());
        eventDesc.setText(eventObject.getDescription());
        updateDateLabel(startDateLabel, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.DAY_OF_MONTH));
        updateTimeLabel(startTimeLabel, startDate.get(Calendar.HOUR_OF_DAY), startDate.get(Calendar.MINUTE));
        updateDateLabel(endDateLabel, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH)+1, endDate.get(Calendar.DAY_OF_MONTH));
        updateTimeLabel(endTimeLabel, endDate.get(Calendar.HOUR_OF_DAY), endDate.get(Calendar.MINUTE));
        location.setText(eventObject.getLocation());
        notify.setText(eventObject.getNotify().toString());
        updateDateLabel(notifyDateLabel, notDate.get(Calendar.YEAR), notDate.get(Calendar.MONTH)+1, notDate.get(Calendar.DAY_OF_MONTH));
        updateTimeLabel(notifyTimeLabel, notDate.get(Calendar.HOUR_OF_DAY), notDate.get(Calendar.MINUTE));

        //use methods for dates
        //use methods for button click
    }

    private static Calendar getDate(long milliSeconds)
    {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar;
    }

    void updateDateLabel(TextView label,int year, int month, int day){
        label.setText(year + "/" + month + "/" + day);
    }
    private void updateTimeLabel(TextView label,int hours, int minutes){
        label.setText(hours + ":" + minutes);
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
        startTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Show time Picker
    public void showEndTimePickerDialog(View v) {
        endTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Show time Picker
    public void showNotifyTimePickerDialog(View v) {
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
        startDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    
    //show date picker
    public void showEndDatePickerDialog(View v) {
        endDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //show date picker
    public void showNotifyDatePickerDialog(View v) {
        notifyDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Create an event
    public void updateEvent(View v){
        if (updateEventTask != null){
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
        eventObject.setNotify(notify.getText().toString().equals("1") || notify.getText().toString().equals("true") ? true : false);
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
        }
        System.out.println("Dates=> "+eventObject.getsDate()+", "+eventObject.getsDate()+", "+eventObject.geteDate());
        updateEventTask = new UpdateEventTask(this.eventObject.get_id(),eventObject,this);
        updateEventTask.execute((Void) null);
    }

    //Create an event
    public void removeEvent(View v) {
        if (deleteEventTask != null){
            return;
        }
        LoginCredentialsApplication credentialsApplication = (LoginCredentialsApplication) getApplicationContext();
        deleteEventTask = new DeleteEventTask(this.eventObject.get_id(),new NameValuePair(credentialsApplication.getEmail(),credentialsApplication.getPassword()),this);
        deleteEventTask.execute((Void) null);
    }

    public class UpdateEventTask extends AsyncTask<Void, Void, Boolean> {

        private final String eventId;
        private final CreateEventObject createEventObject;
        private final AppCompatActivity mActivity;

        UpdateEventTask(String eventId, CreateEventObject createEventObject,AppCompatActivity activity) {
            this.eventId = eventId;
            this.createEventObject = createEventObject;
            mActivity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ConfirmObject confirmObject = Parser.updateEvent(createEventObject,eventId);
                System.out.print("Result in updateEvent from Parser = "+confirmObject);
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
                alertDialog.setMessage(getResources().getString(R.string.event_update_success));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mActivity, Home.class);
                                startActivity(intent);
                                dialog.dismiss();
                                finish();
                            }
                        });
            } else {
                alertDialog.setMessage(getResources().getString(R.string.event_update_failed));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
            alertDialog.show();
        }

        @Override
        protected void onCancelled() {
            updateEventTask = null;
            finish();
        }
    }

    public class DeleteEventTask extends AsyncTask<Void, Void, Boolean> {

        private final String eventId;
        private final NameValuePair credintial;
        private final AppCompatActivity mActivity;

        DeleteEventTask(String eventId, NameValuePair credintial,AppCompatActivity activity) {
            this.eventId = eventId;
            this.credintial = credintial;
            mActivity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ConfirmObject confirmObject = Parser.deleteEvent(credintial.getName(),credintial.getValue(),eventId);
                System.out.print("Result in deleteEvent from Parser = "+confirmObject);
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
                alertDialog.setMessage(getResources().getString(R.string.event_delete_success));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mActivity, Home.class);
                                startActivity(intent);
                                dialog.dismiss();
                                finish();
                            }
                        });
            } else {
                alertDialog.setMessage(getResources().getString(R.string.event_delete_failed));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
            alertDialog.show();
        }

        @Override
        protected void onCancelled() {
            deleteEventTask = null;
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
