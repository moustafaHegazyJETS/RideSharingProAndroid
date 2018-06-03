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
import android.widget.Toast;

import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.DriverCarInfo;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    Service service;
    private static Retrofit retrofit = null;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //objects
        user = (User) getIntent().getExtras().get("user");
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);

        DriverCarInfo d = user.getDriverCarInfo();
        System.out.println(""+d.getCarModel());
//        System.out.println(""+d.user());
//        System.out.println(""+d.user().getDriverCarInfo().getCarModel());



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
                //*******************Here The Check Conditions For Null Objects *********************
                Trip trip = new Trip();
                trip.setTripName("aa");//tripNameTxt.getText().toString()
                trip.setDetails("aa");//tripDetailsTxt.getText().toString()
                trip.setTime("20:00");//tripTimeTxt.getText().toString()
                trip.setDay("01/01/2019");//tripDayTxt.getText().toString()
                System.out.println("******************************"+tripCostTxt.toString()+"   "+tripNumberOfSeatsTxt.toString());
                trip.setCost(Float.valueOf(20));
                trip.setNumberOfSeats(Integer.valueOf(2));
                trip.setTo("a");//tripToTxt.getText().toString()
                trip.setFrom("a");//tripFromTxt.getText().toString()
                System.out.println("s,slslslsls,cfsalfmkmfeokwr"+user.getDriverCarInfo().getDriveCarID());
                DriverCarInfo d = user.getDriverCarInfo();

                d.setUser(new User());
                d.user().setBirthDate(user.getBirthDate());
                d.user().setEmail(user.getEmail());
                d.user().setGender(user.getGender());
                d.user().setIdUser(user.getIdUser());
                d.user().setMobile(user.getMobile());
                d.user().setNationalid(user.getNationalid());
                d.user().setPassword(user.getPassword());
                d.user().setPending(user.getPending());
                d.user().setUserName(user.getUserName());
                d.user().setUserphoto(user.getUserphoto());
                trip.setDriverId(d);


                List<Trip> vals = new ArrayList<>(2);
                Trip t2 = new Trip();
                t2.setIdTrip(user.getIdUser());

                vals.add(0,t2);
                vals.add(1,trip);
                
                
                service.addTrip(vals).enqueue(new Callback<Trip>() {
                    @Override
                    public void onResponse(Call<Trip> call, Response<Trip> response) {
                        if(response.body()!=null)
                        {
                            if(response.body().getTo().equals("Done"))
                            {
                                Toast.makeText(CreateTrip.this, "Done Adding Trip", Toast.LENGTH_SHORT).show();
                                //Here to add local work


                            }else{
                                Toast.makeText(CreateTrip.this, "Error in Creating Trip", Toast.LENGTH_SHORT).show();
                            }
                            
                        }else
                        {
                            Toast.makeText(CreateTrip.this, "Request Null", Toast.LENGTH_SHORT).show();
                        }
                        
                        
                        
                    }

                    @Override
                    public void onFailure(Call<Trip> call, Throwable t) {
                        Toast.makeText(CreateTrip.this, "Error On Failure", Toast.LENGTH_SHORT).show();
                        System.out.println(t);

                    }
                });






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
