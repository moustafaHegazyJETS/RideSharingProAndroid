package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.SqliteDBTrip.DBconnection;
import com.example.rania.itigraduationproject.model.DriverCarInfo;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;

import java.io.Serializable;
import java.util.ArrayList;

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

    Button joinTripBtn;
    Button driverDteailsBtn;
    private static Retrofit retrofit = null;
    Service service;
    DBconnection dBconnection;
    User driverUser;

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
                    Intent intent_home = new Intent(getApplicationContext(), DriverInfoShow.class);
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

        joinTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //1-check for available places over ws and done all ws work

                //2-done all local work
                if(driverUser.getIdUser()!=null)
                {
                dBconnection.insertIntoTrip(t.getTripName(),t.getFrom(),t.getTo(),null,t.getTime(),
                        null,null,null,null,"f",t.getDetails()
                        ,driverUser.getDriverCarInfo().getDriveCarID(),
                        driverUser.getUserName(),t.getCost(),driverUser.getDriverCarInfo().getDriverLicenseNum(),
                        driverUser.getDriverCarInfo().getCarColor(),driverUser.getDriverCarInfo().getCarBrand(),
                        driverUser.getDriverCarInfo().getCarModel(),driverUser.getIdUser(),t.getIdTrip());

                ArrayList<String> s = dBconnection.readFromTripRecent();
                Trip t = dBconnection.returnTrip(driverUser.getIdUser());

                Toast.makeText(TripDetailsActivity.this, "Values From Inside Sqlite :" +s.get(0).toString(), Toast.LENGTH_SHORT).show();
                    Log.i("Trip", t.getDetails());
                    Log.i("Trip", ""+t.getIdTrip());

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
