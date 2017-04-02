package com.example.akhil.project2;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
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

import com.example.akhil.project2.fragments.Bus;
import com.example.akhil.project2.fragments.Dashboard;
import com.example.akhil.project2.fragments.Friend;
import com.example.akhil.project2.fragments.Help;
import com.example.akhil.project2.fragments.Pass;
import com.example.akhil.project2.fragments.Profile;
import com.example.akhil.project2.fragments.Search;
import com.example.akhil.project2.fragments.Trips;
import com.example.akhil.project2.fragments.Wallet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SELECTED_ITEM = "arg_selected_item";
    private int mSelectedItem;
    private BottomNavigationView mBottomNav;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "BusAppPref";
    private static final String IS_LOGIN = "IsLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        pref = this.getApplicationContext().getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();

        checkLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentticket();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);

    }

    @Override
    //bottom navi
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            intentmap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int NavIndex = 0;
        String txt = "";
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            fragment = new Profile();
            txt = "Profile";
            NavIndex = 0;
        } else if (id == R.id.nav_pass) {
            NavIndex =1;
            txt = "Bus Pass";
            fragment = new Pass();
        } else if (id == R.id.nav_wallet) {
            NavIndex =2;
            txt = "Wallet";
            fragment = new Wallet();
        } else if (id == R.id.nav_trips) {
            NavIndex = 3;
            txt = "Trips";
            fragment = new Trips();
        } else if (id == R.id.nav_help) {
            NavIndex = 4;
            txt = "Help";
            fragment = new Help();
        } else if (id == R.id.nav_log) {
            NavIndex = 5;
        } else if (id == R.id.nav_about) {
            NavIndex = 6;
            txt = "About";
        } else if (id == R.id.nav_share) {
            NavIndex = 7;
        }

        if(NavIndex < 5){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commitAllowingStateLoss();
        }
        if(NavIndex == 5){
            alert();
        }
        if(NavIndex == 6){
            Intent i = new Intent(this,About.class);
            startActivity(i);
            updateToolbarText("");
        }
        if(NavIndex == 7) {
            Share();
        }

        updateToolbarText(txt);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //bottomnav
    private void selectFragment(MenuItem item) {
        Fragment fragment = null;
        String txt = "";
        switch (item.getItemId()) {
            case R.id.action_search:
                fragment = new Search();
                txt = "Search Route";
                break;
            case R.id.action_bus:
                fragment = new Bus();
                txt = "Bus";
                break;
            case R.id.action_dashboard:
                fragment = new Dashboard();
                txt = "Dashboard";
                break;
            case R.id.action_friend:
                fragment = new Friend();
                txt = "Friend";
                break;
        }


        updateToolbarText(txt);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    // Intent for map
    public void intentmap(){
        Intent map =  new Intent(this,MapsActivity.class);
        map.putExtra("busid",9);
        startActivity(map);
    }
    public void intentticket() {
        Intent ticket = new Intent(this, FullscreenActivity.class);
        ticket.putExtra("tick","0");
        startActivity(ticket);
    }
    public void alert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure to signout?");
        alert.setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
               logoutUser();
            }
        });
        alert.setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                ///no action required
            }
        }).setTitle("LOGOUT").setIcon(R.drawable.ic_alert_circle_outline).show();

    }
    public void Share(){
        Intent SharingIntent = new Intent(Intent.ACTION_SEND);
        SharingIntent.setType("text/plain");
        SharingIntent.putExtra(Intent.EXTRA_TITLE,"TRACK IT!");
        SharingIntent.putExtra(Intent.EXTRA_TEXT,"Download the app to trackyour bus at your feet");
        SharingIntent.putExtra(Intent.EXTRA_TEXT,"http://www.iiitdm.ac.in");
        startActivity(Intent.createChooser(SharingIntent,"Share To"));
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

}


