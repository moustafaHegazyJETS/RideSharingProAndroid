package com.example.rania.itigraduationproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import com.example.rania.itigraduationproject.Controllers.FabDesignFun;
import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.SqliteDBTrip.DBDriverConnection;
import com.example.rania.itigraduationproject.firebeasePushNotifications.FirebeaseIncstanceIdDevice;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeDriver extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView helloTxt;
    TextView driverEmail;
    ListView listViewDriver;
    SessionManager session;
    User user;
    User driverUser;
    DBDriverConnection dbDriverConnection;
    private static Retrofit retrofit = null;
    Service service;


    protected void onStart() {
        super.onStart();
        if(!CheckInternetConnection.isNetworkAvailable(this))
        {
            CheckInternetConnection.bulidDuligo(this);
        }
        else
        {
            final ArrayList<Trip> tripArray = dbDriverConnection.readFromTripDriverRecent();

            List<String> tripNames = new ArrayList<>();
            for (int ii =0 ; ii<tripArray.size() ; ii++)
            {

                tripNames.add(tripArray.get(ii).getTripName().toString());

            }

            listViewDriver.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,tripNames));
            listViewDriver.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(HomeDriver.this, "", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            listViewDriver.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent in =new Intent(HomeDriver.this,TripDetailsForDriver.class);
                    in.putExtra("tripVal",(Serializable) tripArray.get(i) );
                    in.putExtra("driverUser",(Serializable) driverUser );
                    startActivity(in);

                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_driver);

        //objects
        session=new SessionManager(getApplicationContext());
        Intent i = getIntent();
        user = (User) i.getExtras().get("user");
        driverUser = user;
        dbDriverConnection = new DBDriverConnection(this);
        //Retrofit -----------------------------------------------
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);

        //resources
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // Set Header values
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        driverEmail=(TextView)hView.findViewById(R.id.driveremailtext);
        TextView drivername=(TextView)findViewById(R.id.drivernametext);
        Log.i("email",user.getEmail());
        driverEmail.setText(user.getEmail());

       // Log.i("usernamed",user.getUserName());
       // drivername.setText(user.getUserName());

        helloTxt = findViewById(R.id.HelloTxt);
       // helloTxt.setText("Hello Driver : "+user.getUserName());
        listViewDriver = findViewById(R.id.ListViewDriver);
        if(user.getPending().equals("0")) {
            helloTxt .setText("Hello, "+user.getUserName()+" wait until Admin Accept");
            helloTxt .setTextColor(Color.parseColor("#00ff00"));
            fab.setImageBitmap(FabDesignFun.textAsBitmap("Logout", 40, Color.WHITE));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logoutUser();
                }
            });


        }
        else if(user.getPending().equals("-1"))
        {
            service.getHello().enqueue(new Callback<Trip>() {
                @Override
                public void onResponse(Call<Trip> call, Response<Trip> response) {
                    Toast.makeText(HomeDriver.this, ""+response.body().getTripName(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Trip> call, Throwable t) {

                }
            });

            helloTxt .setText("Sorry you are Reject  you can Register  Again");
            helloTxt .setTextColor(Color.parseColor("#ff0000"));
            fab.setImageBitmap(FabDesignFun.textAsBitmap("Logout", 40, Color.WHITE));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logoutUser();
                }
            });
        }
        else {
            helloTxt .setText("Hello "+user.getUserName());
        //actions
        FabDesignFun.textAsBitmap("Trip",40, Color.WHITE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent driver_i = new Intent(HomeDriver.this, CreateTrip.class);
                driver_i.putExtra("user", user);
                driver_i.putExtra("driverUser",(Serializable) driverUser );
                startActivity(driver_i);
            }
        });}

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_driver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.driverincomingTrip) {


        } else if (id == R.id.deriverPastTrip) {
            Intent intent=new Intent(HomeDriver.this,DriverpastTrip.class);
            intent.putExtra("user", user);
            intent.putExtra("driverUser",(Serializable) driverUser );
            startActivity(intent);

        } else if (id == R.id.deriverprofile) {
            Intent intent=new Intent(HomeDriver.this,DriverProfile.class);
            intent.putExtra("user", user);
            intent.putExtra("driverUser",(Serializable) driverUser );
            startActivity(intent);

        } else if (id == R.id.deriverlogout) {
            logoutUser();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void logoutUser()
    {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you really want to Close?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        session.logoutUser();
                        finish();


                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
