package com.example.rania.itigraduationproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.rania.itigraduationproject.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateTrip extends AppCompatActivity {


    TextView tripNameTxt;
    TextView tripDetailsTxt;
    TextView tripTimeTxt;
    TextView tripFromTxt;
    TextView tripToTxt;
    TextView tripDayTxt;
    TextView tripNumberOfSeatsTxt;
    TextView tripCostTxt;
    Calendar myCalendar = Calendar.getInstance();
    Calendar onTimeCalender= (Calendar) myCalendar.clone();

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //objects
        user = (User) getIntent().getExtras().get("user");



        //resources

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        tripNameTxt=findViewById(R.id.TripName);
        tripDetailsTxt=findViewById(R.id.TripDetails);
        tripTimeTxt=findViewById(R.id.TripTime);
        tripFromTxt=findViewById(R.id.TripFrom);
        tripToTxt=findViewById(R.id.TripTo);
        tripDayTxt=findViewById(R.id.TripDay);
        tripNumberOfSeatsTxt=findViewById(R.id.numberOfSeats);
        tripCostTxt=findViewById(R.id.TripCost);



        //actions
        //set time ll event
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY,hour);
                myCalendar.set(Calendar.MINUTE,minute);
                myCalendar.set(Calendar.SECOND,0);
                updateTime();
            }
        };

        tripTimeTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(CreateTrip.this, time, myCalendar
                        .get(Calendar.MINUTE), myCalendar.get(Calendar.HOUR_OF_DAY),true
                ).show();
            }
        });



            //for date time in start time
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        tripDayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateTrip.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ws Work For Adding Trip



            }
        });
    }
    private void updateTime() {
        tripTimeTxt.setText(myCalendar.get(Calendar.HOUR_OF_DAY)+":"+myCalendar.get(Calendar.MINUTE));
    }


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tripDayTxt.setText(sdf.format(myCalendar.getTime()));
    }

}
