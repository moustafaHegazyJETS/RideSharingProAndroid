package com.example.rania.itigraduationproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.FabDesignFun;
import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.Interfaces.TripService;
import com.example.rania.itigraduationproject.SqliteDBTrip.DBconnection;
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

public class HomeUser extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView username;
    TextView useremail_header;
    SessionManager session;
    User user;
    private static Retrofit retrofit = null;
    SessionManager session_mangement;
    Service service;
    TripService tripService;
    ListView tripList;
    ArrayList<String>listNames;
    TextView notripText;


    @Override
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
        setContentView(R.layout.activity_home);

        //Objects
        final List<Trip> tripArray=new ArrayList<>();
        listNames=new ArrayList<>();
        session=new SessionManager(getApplicationContext());
        DBconnection dBconnection=new DBconnection(getApplicationContext());
        //---------------SharedPrefrences ----------
        session_mangement = new SessionManager(getApplicationContext());
        //Retrofit -----------------------------------------------
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);
        tripService=retrofit.create(TripService.class);
        //------------------------------------------------
        Intent intent=getIntent();
        user=(User) intent.getSerializableExtra("user");

      //Resources
        tripList=(ListView)findViewById(R.id.listViewtrip);
        notripText=(TextView)findViewById(R.id.notrip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        username=(TextView)findViewById(R.id.username_home);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if(user.getPending().equals("0")) {
            username.setText("Hello, "+user.getUserName()+" wait until Admin Accept");
            username.setTextColor(Color.parseColor("#00ff00"));
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
                    Toast.makeText(HomeUser.this, ""+response.body().getTripName(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Trip> call, Throwable t) {

                }
            });

            username.setText("Sorry you are Reject  you can Register  Again");
            username.setTextColor(Color.parseColor("#ff0000"));
            fab.setImageBitmap(FabDesignFun.textAsBitmap("Logout", 40, Color.WHITE));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logoutUser();
                }
            });
        }
        else {
            username.setText("hello "+user.getUserName());

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            service.getAllUserReserverdTrips(user).enqueue(new Callback<List<Trip>>() {

                @Override
                public void onResponse(Call<List<Trip>> call, final Response<List<Trip>> response) {
                    if (response.body()!=null)
                    {
                         if(response.body().size()>0)
                        {

                             for (int i =0 ; i<response.body().size() ; i++)
                                 {
                                    tripArray.add(i,response.body().get(i));
                                    listNames.add(response.body().get(i).getTripName().toString());
                                 }


                            ArrayAdapter<String> adapter =new ArrayAdapter<>(HomeUser.this, android.R.layout.simple_list_item_1,listNames);
                            tripList.setAdapter(adapter);

                             tripList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                         @Override
                          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                             Intent in = new Intent(HomeUser.this, UserShowReservedTripDetails.class);
                             in.putExtra("trip", (Serializable) tripArray.get(i));
                             startActivity(in);
                         }});
                       }else {
                             notripText.setText("no  trip  reserved up  till  now");
                         }
                    } else{
                        Toast.makeText(HomeUser.this, "Response body  null", Toast.LENGTH_SHORT).show();
                          }


                }

                @Override
                public void onFailure(Call<List<Trip>> call, Throwable t) {
                    Toast.makeText(HomeUser.this, "Failure", Toast.LENGTH_SHORT).show();

                }
            });


            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().getItem(0).setChecked(true);
            //Set Header Values
            View hView =  navigationView.getHeaderView(0);
            TextView userEmail=(TextView)hView.findViewById(R.id.usermail_header);
            TextView username=(TextView)findViewById(R.id.usernameheader);
            Log.i("email",user.getEmail());
            userEmail.setText(user.getEmail());
            userEmail.setText(user.getUserName());


            fab.setImageBitmap(FabDesignFun.textAsBitmap("Search", 40, Color.WHITE));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //here the code of search to joining trip
                    Intent intent_home = new Intent(getApplicationContext(), TripShowActivity.class);
                    intent_home.putExtra("user", (Serializable) user );
                    startActivity(intent_home);

                }
            });


        }
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

        getMenuInflater().inflate(R.menu.home, menu);


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


        if (id == R.id.userIncomingTrip) {

        } else if (id == R.id.userPastTrip) {
            Intent intent=new Intent(getApplicationContext(),UserPastTrip.class);
            intent.putExtra("user", (Serializable) user);
            startActivity(intent);

        } else if (id == R.id.user_profile) {
            Intent intent=new Intent(getApplicationContext(),UserProfile.class);
            intent.putExtra("user", (Serializable) user);
            startActivity(intent);

        } else if (id == R.id.userlogout) {
            logoutUser();

        } else if (id == R.id.user_searchTrip) {
            Intent intent_home = new Intent(getApplicationContext(), TripShowActivity.class);
            intent_home.putExtra("user", (Serializable) user );
            startActivity(intent_home);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        useremail_header=(TextView)findViewById(R.id.usermail_header);
        useremail_header.setText(user.getEmail());


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
