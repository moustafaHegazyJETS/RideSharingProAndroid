package com.example.rania.itigraduationproject;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.SqliteDBTrip.DBconnection;
import com.example.rania.itigraduationproject.alarmPk.Alarm_receiver;
import com.example.rania.itigraduationproject.model.DriverCarInfo;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripDetailsActivity extends AppCompatActivity {
    //Declaration
    EditText tripName;
    EditText tripTime;
    EditText tripFrom;
    EditText tripTo;
    EditText numOfSeatsTrip;
    EditText cost;
    EditText day;
    Button joinTripBtn;
    Button driverDteailsBtn;
    private static Retrofit retrofit = null;
    Service service;
    DBconnection dBconnection;
    User driverUser;
    Context context;
    Calendar myCalendar = Calendar.getInstance();
    Calendar onTimeCalender= (Calendar) myCalendar.clone();
    AlarmManager alarmManage;
    PendingIntent pending_intent;
    Trip trip=new Trip();

    TripDetailsForDriver driverDetails=new TripDetailsForDriver();
    int numberOfSeats=driverDetails.getAllReservedUsers(trip);
    String num=numOfSeatsTrip.getText().toString();
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
        setContentView(R.layout.activity_trip_details);

        //Objects ----------------
        Intent intent=getIntent();
        final Trip t=(Trip) intent.getSerializableExtra("trip");
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);
        dBconnection = new DBconnection(this);
        alarmManage = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Intent alarm_intent = new Intent(this,Alarm_receiver.class);
        this.context=this;

        //Resourses
        tripName=(EditText)findViewById(R.id.TripNameEdt);
        tripName.setText(t.getTripName());
        tripTime=(EditText)findViewById(R.id.TimeTripEdt);
        tripTime.setText(t.getTime());
        tripFrom=(EditText)findViewById(R.id.SourceTripEdt);
        tripFrom.setText(t.getFrom());
        tripTo=(EditText)findViewById(R.id.destinationTripEdt);
        tripTo.setText(t.getTo());
        numOfSeatsTrip=(EditText)findViewById(R.id.numOfSeatsEdt);
        numOfSeatsTrip.setText(t.getNumberOfSeats().toString());
        cost=(EditText)findViewById(R.id.costEDT);
        cost.setText(""+t.getCost());
        day=(EditText)findViewById(R.id.dayEdt);
        day.setText(t.getDay());
        joinTripBtn=(Button)findViewById(R.id.JoinTrip);
        driverDteailsBtn=(Button)findViewById(R.id.driverDetails);

        //Actions

        //to retrive driver user values
        requestDriverInfo(t);


        driverDteailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(driverUser.getDriverCarInfo()!=null)
                {
                    Intent intent_home = new Intent(getApplicationContext(),DriverInfoShow.class);
                    System.out.println(driverUser.getMobile());
                    System.out.println(driverUser.getDriverCarInfo().getCarYear());
                    intent_home.putExtra("driverInfo", (Serializable)driverUser);
                    startActivity(intent_home);
                }
                else 
                {
                    Toast.makeText(TripDetailsActivity.this, "Error In Loadding", Toast.LENGTH_SHORT).show();
                    //to retrive driver car value
                    requestDriverCarInfo(driverUser);
                }
               
            }
        });

//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                // TODO Auto-generated method stub
//                System.out.println(year);
//                System.out.println(monthOfYear);
//                System.out.println(dayOfMonth);
//            }
//
//        };

        joinTripBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
//                new DatePickerDialog(context, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


                if(driverUser!=null && driverUser.getDriverCarInfo()!=null)
                {
                //1-check for available places over ws and done all ws work
                    List<Integer> values = new ArrayList<>();
                    values.add(0,driverUser.getIdUser());
                    values.add(1,t.getIdTrip());
                    values.add(2,driverUser.getDriverCarInfo().getDriveCarID());

                    service.registerWithTrip(values).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            if(response.body()!=null) {
                                if(numberOfSeats <= Integer.parseInt(num)){
                                    if(response.body().getEmail().equals("t"))
                                    {
                                        Toast.makeText(context, "Done Reservation", Toast.LENGTH_SHORT).show();
                                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                                        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd
                                        Date timeZ;
                                        Date dayZ;
                                        try {
                                            //2-done all local work
                                            //here to add calender and alarm
                                            timeZ = formatter.parse(t.getTime());
                                            Log.i("time", "Day "+t.getDay());
                                            dayZ = formatter2.parse(t.getDay());
                                            Log.i("time", "DayAfter "+dayZ);
                                            Log.i("time", "H "+timeZ.getHours());
                                            Log.i("time", "M "+timeZ.getMinutes());
                                            Log.i("time", "D "+dayZ.getDate());
                                            Log.i("time", "M "+(dayZ.getMonth()));
                                            Log.i("time", "Y "+((dayZ.getYear()-100)+2000));

                                            myCalendar.set(Calendar.YEAR,((dayZ.getYear()-100)+2000));
                                            myCalendar.set(Calendar.MONTH, dayZ.getMonth());
                                            myCalendar.set(Calendar.DAY_OF_MONTH, dayZ.getDate());
                                            myCalendar.set(Calendar.HOUR_OF_DAY,timeZ.getHours());
                                            myCalendar.set(Calendar.MINUTE,timeZ.getMinutes());
                                            myCalendar.set(Calendar.SECOND,0);

                                            //here to set callender

//                                      //adding values inside Sqlite DB
                                            dBconnection.insertIntoTrip(t.getTripName(),t.getFrom(),t.getTo(),t.getDay()/*day*/,t.getTime()/*time*/,
                                                    null,null,null,null,"f",t.getDetails()
                                                    ,driverUser.getDriverCarInfo().getDriveCarID(),
                                                    driverUser.getUserName(),t.getCost(),driverUser.getDriverCarInfo().getDriverLicenseNum(),
                                                    driverUser.getDriverCarInfo().getCarColor(),driverUser.getDriverCarInfo().getCarBrand(),
                                                    driverUser.getDriverCarInfo().getCarModel(),driverUser.getIdUser(),t.getIdTrip());

                                            Intent intent = new Intent(TripDetailsActivity.this, SplashScreen.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //this id is uniqe for each trip so it uses for define pending alarm
                                            int id = t.getIdTrip();

                                            alarm_intent.putExtra("Ex", "on");
                                            alarm_intent.putExtra("id", String.valueOf(id));
                                            alarm_intent.putExtra("who","user");

                                            pending_intent = pending_intent.getBroadcast(TripDetailsActivity.this, id
                                                    , alarm_intent, PendingIntent.FLAG_UPDATE_CURRENT);


                                            alarmManage.setExact(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), pending_intent);

                                            Toast toastk = Toast.makeText(TripDetailsActivity.this, "done"+myCalendar.getTimeInMillis(),Toast.LENGTH_LONG);
                                            toastk.show();

                                            startActivity(intent);
                                            finish();
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }



                                    }else if(response.body().getEmail().equals("f"))
                                    {
                                        Toast.makeText(context, "Error in Reservation", Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(context, response.body().getEmail(), Toast.LENGTH_SHORT).show();

                                    }
                                }else
                                {
                                    Toast.makeText(context, "Response is NULL", Toast.LENGTH_SHORT).show();
                                }
                                }
                           else{
                                Toast.makeText(context, "This Trip has been completed", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(context, "something wrong"+t, Toast.LENGTH_SHORT).show();
                            Log.i("Error",""+t);
                        }
                    });

                }
                else
                {
                    Toast.makeText(TripDetailsActivity.this, "Error In Loadding", Toast.LENGTH_SHORT).show();
                    //to retrive driver car value
                    requestDriverCarInfo(driverUser);
                }


            }
        });



    }


    public void requestDriverInfo(Trip trip) {
        service.getDriverInfo(trip).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Toast.makeText(TripDetailsActivity.this, ""+response.body().getUserName(), Toast.LENGTH_SHORT).show();
                driverUser = response.body();
                //to retrive driver car value
                requestDriverCarInfo(driverUser);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(TripDetailsActivity.this, "onFail"+t, Toast.LENGTH_SHORT).show();


            }
        });

    }

    public void requestDriverCarInfo(User user) {
        service.getDriverCarInfo(user).enqueue(new Callback<DriverCarInfo>() {
            @Override
            public void onResponse(Call<DriverCarInfo> call, Response<DriverCarInfo> response) {
                Toast.makeText(TripDetailsActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                driverUser.setDriverCarInfo(response.body());
            }

            @Override
            public void onFailure(Call<DriverCarInfo> call, Throwable t) {
                Toast.makeText(TripDetailsActivity.this, "onFail"+t, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
