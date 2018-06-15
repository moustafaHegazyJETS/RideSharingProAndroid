package com.example.rania.itigraduationproject;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.FabDesignFun;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.SqliteDBTrip.DBDriverConnection;
import com.example.rania.itigraduationproject.alarmPk.Alarm_receiver;
import com.example.rania.itigraduationproject.model.DriverCarInfo;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateTrip extends AppCompatActivity {
    TextView tripNameTxt;
    TextView tripDetailsTxt;
    TextView tripTimeTxt;
    EditText tripFromEditTxt;
    EditText tripToEditTxt;
    TextView tripDayTxt;
    TextView tripNumberOfSeatsTxt;
    TextView tripCostTxt;
    Calendar myCalendar = Calendar.getInstance();
    Calendar onTimeCalender= (Calendar) myCalendar.clone();
    Service service;
    private static Retrofit retrofit = null;
    User user;
    DBDriverConnection dbDriverConnection;
    AlarmManager alarmManage;
    PendingIntent pending_intent;
    Context context;
    public static final int PLACE_PICKER_REQUEST1 = 1;
    public static final int PLACE_PICKER_REQUEST2 = 2;
    double toLongtiude;
    double fromLatitude;
    double toLatitude;
    double fromLongtiude;
    String startPoint = "";
    String destination = "";


    protected void onStart() {
        super.onStart();
        if(!CheckInternetConnection.isNetworkAvailable(this))
        {
            CheckInternetConnection.bulidDuligo(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //objects
        user = (User) getIntent().getExtras().get("user");
        DriverCarInfo d = user.getDriverCarInfo();
        System.out.println(""+d.getCarModel());
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);
        dbDriverConnection = new DBDriverConnection(this);
        alarmManage = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Intent alarm_intent = new Intent(this,Alarm_receiver.class);
        this.context=this;

        //resources

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        tripNameTxt=findViewById(R.id.TripName);
        tripDetailsTxt=findViewById(R.id.TripDetails);
        tripTimeTxt=findViewById(R.id.TripTime);
        tripFromEditTxt=findViewById(R.id.TripFrom);
        tripToEditTxt=findViewById(R.id.TripTo);
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
       tripToEditTxt.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {
               action_From_To();
               return false;
           }
       });
        tripFromEditTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                action_From_To();
                return false;
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

       // tripToEditTxt.

        tripDayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateTrip.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        FabDesignFun.textAsBitmap("Save",40, Color.WHITE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ws Work For Adding Trip
                //*******************Here The Check Conditions For Null Objects *********************
                 if(validate())
                 {
                final Trip trip = new Trip();
                trip.setTripName(tripNameTxt.getText().toString());//tripNameTxt.getText().toString()
                trip.setDetails(tripDetailsTxt.getText().toString());//tripDetailsTxt.getText().toString()
                trip.setTime(tripTimeTxt.getText().toString());//tripTimeTxt.getText().toString()
                trip.setDay(tripDayTxt.getText().toString());//tripDayTxt.getText().toString()
                System.out.println("******************************"+tripCostTxt.toString()+"   "+tripNumberOfSeatsTxt.toString());
                trip.setCost(Float.valueOf(tripCostTxt.getText().toString()));
                trip.setNumberOfSeats(Integer.valueOf(tripNumberOfSeatsTxt.getText().toString()));
                trip.setTo(tripToEditTxt.getText().toString());//tripToTxt.getText().toString()
                trip.setFrom(tripFromEditTxt.getText().toString());//tripFromTxt.getText().toString()
//                     trip.setStartlatitude(toLatitude);
//                     trip.setStartlongtiude(toLongtiude);
//                     trip.setEndlatitude(fromLatitude);
//                     trip.setEndlongtiude(fromLongtiude);
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

                //if(myCalendar.compareTo(onTimeCalender)<=0) {
                  //  Toast.makeText(CreateTrip.this, "Check For Upcomming Time", Toast.LENGTH_SHORT).show();

                //} else {

                    service.addTrip(vals).enqueue(new Callback<Trip>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(Call<Trip> call, Response<Trip> response) {
                            if (response.body() != null) {
                                if (response.body().getTo().equals("Done")) {
                                    Toast.makeText(CreateTrip.this, "Done Adding Trip", Toast.LENGTH_SHORT).show();
                                    //Here to add local work.

                                    final int id = response.body().getIdTrip();

                                    //add to local Sqlite
                                    dbDriverConnection.insertIntoTrip(id, trip.getTripName());

                                    Intent intent = new Intent(CreateTrip.this, SplashScreen.class);
                                    //this id is uniqe for each trip so it uses for define pending alarm
                                    //Value Rag3a mn alweb Service


                                    alarm_intent.putExtra("Ex", "on");
                                    alarm_intent.putExtra("id", String.valueOf(id));
                                    alarm_intent.putExtra("who", "driver");

                                    pending_intent = pending_intent.getBroadcast(CreateTrip.this, id
                                            , alarm_intent, PendingIntent.FLAG_UPDATE_CURRENT);


                                    alarmManage.setExact(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), pending_intent);

                                    Toast toastk = Toast.makeText(CreateTrip.this, "done" + myCalendar.getTimeInMillis(), Toast.LENGTH_LONG);
                                    toastk.show();

                                    startActivity(intent);
                                    finish();


                                } else {
                                    Toast.makeText(CreateTrip.this, "Error in Creating Trip", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(CreateTrip.this, "Request Null", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<Trip> call, Throwable t) {
                            Toast.makeText(CreateTrip.this, "Error On Failure", Toast.LENGTH_SHORT).show();
                            System.out.println(t);

                        }
                    });

               // }
                     }







            }
        });
    }
    //Auto Complete Functions
    //Alert Function
    public void showSettingsAlerts()
    {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled . Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);

            }
        });
    }

    //Network Alert Setting
    public void showSettingsAlertsForNetwork() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Network is settings");
        alertDialog.setMessage("Network is not enabled . Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                startActivity(i);

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        alertDialog.show();
    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                if (place.equals(null)) {

                    Toast.makeText(this, "place is null", Toast.LENGTH_LONG).show();
                }

               // String toastMsg = String.format("Place that you need is : ", place.getAddress().toString());
                //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                tripFromEditTxt.setText(place.getAddress().toString());
                startPoint = place.getAddress().toString();
                fromLatitude = place.getLatLng().latitude;
                fromLongtiude = place.getLatLng().longitude;
            }
        }
        if (requestCode == PLACE_PICKER_REQUEST2) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                if (place.equals(null)) {

                    Toast.makeText(this, "place is null", Toast.LENGTH_LONG).show();
                }

               // String toastMsg = String.format("Place that you need is : ", place.getAddress().toString());
               // Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                destination = place.getAddress().toString();
                tripToEditTxt.setText(destination);
                toLatitude = place.getLatLng().latitude;
                toLongtiude = place.getLatLng().longitude;
            }
        }

    }


    //Function to Action when click  to ediText
    public void action_From_To()
    {
        if (ActivityCompat.checkSelfPermission(CreateTrip.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(CreateTrip.this, "You need to enable location first", Toast.LENGTH_SHORT).show();
            showSettingsAlerts();
            return;
        }
        if (ActivityCompat.checkSelfPermission(CreateTrip.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(CreateTrip.this, "You need to enable Network first", Toast.LENGTH_SHORT).show();
            showSettingsAlertsForNetwork();
            return;
        }

        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(CreateTrip.this), PLACE_PICKER_REQUEST1);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }



    private void updateTime() {
        tripTimeTxt.setText(myCalendar.get(Calendar.HOUR_OF_DAY)+":"+myCalendar.get(Calendar.MINUTE));
    }


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tripDayTxt.setText(sdf.format(myCalendar.getTime()));
    }

    //Validation ------------------------------------------------------
    public boolean validate() {
        boolean valid = true;
        String tripName=tripNameTxt.getText().toString();
        String tripFrom=tripFromEditTxt.getText().toString();
        String tripTo= tripToEditTxt.getText().toString();
        String details=tripDetailsTxt.getText().toString();
        String tripNumOfSeats=tripNumberOfSeatsTxt.getText().toString();
        String tripDay=tripDayTxt.getText().toString();
        String tripCost=tripCostTxt.getText().toString();
        String tripTime=tripTimeTxt.getText().toString();
        Pattern pattern = Pattern.compile(new String ("^[a-zA-Z\\s]*$"));
        Matcher matcher = pattern.matcher(tripName);
        if (tripName.isEmpty()||!matcher.matches()) {
            tripTimeTxt.setError("Enter Correct Name Character Only");
            tripTimeTxt.requestFocus();
            valid = false;
        } else {
            tripTimeTxt.setError(null);
            tripTimeTxt.requestFocus();
        }

        if (details.isEmpty()||!matcher.matches()) {
            tripDetailsTxt.setError("Please Enter Details");
            tripDetailsTxt.requestFocus();
            valid = false;
        } else {
            tripDetailsTxt.setError(null);
            tripDetailsTxt.requestFocus();
        }
         pattern = Pattern.compile(new String ("[+-]?([0-9]*[.])?[0-9]+"));
         matcher = pattern.matcher(tripCost);

        if (tripCost.isEmpty() || !matcher.matches()) {
            tripCostTxt.setError("Enter Cost Number only");
            tripCostTxt.requestFocus();
            valid = false;
        } else {
            tripCostTxt.setError(null);
            tripCostTxt.requestFocus();
        }

        if (tripDay.isEmpty()||tripDay.equals("day")) {
            tripDayTxt.setError("Enter Correct Day please");
            tripDayTxt.requestFocus();
            valid = false;
        } else {
            tripDayTxt.setError(null);
            tripDayTxt.requestFocus();
        }
        if (tripTime.isEmpty()||tripTime.equals("time")) {
            tripTimeTxt.setError("Enter Correct Time please");
            tripTimeTxt.requestFocus();
            valid = false;
        } else {
            tripTimeTxt.setError(null);
            tripTimeTxt.requestFocus();
        }
        if (tripNumOfSeats.isEmpty()) {
            tripNumberOfSeatsTxt.setError("Enter Number only");
            tripNumberOfSeatsTxt.requestFocus();
            valid = false;
        } else {
            tripNumberOfSeatsTxt.setError(null);
            tripNumberOfSeatsTxt.requestFocus();
        }
        if (tripTo.isEmpty()) {
            tripToEditTxt.setError("Enter Number only");
            tripToEditTxt.requestFocus();
            valid = false;
        } else {
            tripToEditTxt.setError(null);
            tripToEditTxt.requestFocus();
        }
        if (tripFrom.isEmpty()) {
            tripFromEditTxt.setError("Enter Number only");
            tripFromEditTxt.requestFocus();
            valid = false;
        } else {
            tripFromEditTxt.setError(null);
            tripFromEditTxt.requestFocus();
        }

        return valid;
    }

}
