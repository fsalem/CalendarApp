package com.calendar.koko.calendarapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class EventDetails extends AppCompatActivity {
    private DatePickerFragment startDatePickerFragment;
    private TimePickerFragment startTimePickerFragment;
    private DatePickerFragment endDatePickerFragment;
    private TimePickerFragment endTimePickerFragment;
    private DatePickerFragment notifyDatePickerFragment;
    private TimePickerFragment notifyTimePickerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);



        Intent myintent = getIntent();

        // TODO get event object from intent ...this object represents an event and selected from the list in the previous activity
        //MyObject obj =  (MyObject) myintent.getParcelableExtra("name_of_extra");

        //TODO put data to corressponding view elment e.g. u_eventname_text
        // TODO put dates and times (start, end, notify dates and times) labels - total 6 labels


        //use methods for dates
        //use methods for button click
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        int hourOfDay, minute;
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
        }
    }

    //Show time Picker
    public void showStartTimePickerDialog(View v) {
        startTimePickerFragment = new TimePickerFragment();
        startTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Show time Picker
    public void showEndTimePickerDialog(View v) {
        endTimePickerFragment = new TimePickerFragment();
        endTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Show time Picker
    public void showNotifyTimePickerDialog(View v) {
        notifyTimePickerFragment = new TimePickerFragment();
        notifyTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        int year,month,day;
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
        }
    }

    //show date picker
    public void showStartDatePickerDialog(View v) {
        startDatePickerFragment= new DatePickerFragment();
        startDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    
    //show date picker
    public void showEndDatePickerDialog(View v) {
        endDatePickerFragment = new DatePickerFragment();
        endDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //show date picker
    public void showNotifyDatePickerDialog(View v) {
        notifyDatePickerFragment = new DatePickerFragment();
        notifyDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Create an event
    public void updateEvent(View v){
        // TODO handle event update logic
    }

    //Create an event
    public void removeEvent(View v) {
        // TODO handle event remove logic
    }

}
