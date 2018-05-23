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
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView username;
    TextView useremail_header;
    SessionManager session;

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
            session=new SessionManager(getApplicationContext());



        Intent intent=getIntent();
        User user=(User) intent.getSerializableExtra("user");
        Toast.makeText(this, "Username:"+user.getUserName(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Email:"+user.getEmail(), Toast.LENGTH_SHORT).show();

//        User user = intent
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        username=(TextView)findViewById(R.id.username_home);
        username.setText(user.getUserName());



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        useremail_header=(TextView)findViewById(R.id.usermail_header);
       // useremail_header.setText("kkkkkkkkkkkkkk");
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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


        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_camera) {

        } else if (id == R.id.user_profile) {

        } else if (id == R.id.logout) {

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
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        useremail_header=(TextView)findViewById(R.id.usermail_header);
        useremail_header.setText("kkkkkkkkkkkkkk");


        drawer.closeDrawer(GravityCompat.START);


        return true;
    }
}
