package com.example.rania.itigraduationproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.SqliteDBTrip.DBDriverConnection;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HomeDriver extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView helloTxt;
    ListView listViewDriver;

    SessionManager session;

    User user;
    User driverUser;
    DBDriverConnection dbDriverConnection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_driver);

        //objects
        session=new SessionManager(getApplicationContext());
        Intent i = getIntent();
        user = (User) i.getExtras().get("user");
        driverUser = user;
        System.out.println(user.getUserName());
        dbDriverConnection = new DBDriverConnection(this);



        //resources
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        helloTxt = findViewById(R.id.HelloTxt);
        helloTxt.setText("Hello Driver : "+user.getUserName());
        listViewDriver = findViewById(R.id.ListViewDriver);
        final ArrayList<Trip> tripArray = dbDriverConnection.readFromTripDriverRecent();

        List<String> tripNames = new ArrayList<>();
        for (int ii =0 ; ii<tripArray.size() ; ii++)
        {
            tripNames.add(tripArray.get(ii).getTripName().toString());
            Toast.makeText(this, ""+tripArray.get(ii).getTripName().toString(), Toast.LENGTH_SHORT).show();

        }

        listViewDriver.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tripNames));

        listViewDriver.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(HomeDriver.this, "Hi", Toast.LENGTH_SHORT).show();
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

        //actions

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent driver_i = new Intent(HomeDriver.this, CreateTrip.class);
                driver_i.putExtra("user", user);
                startActivity(driver_i);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
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
