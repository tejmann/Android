package com.example.android.newsreport20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checks the network connection
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(!(networkInfo!=null&&networkInfo.isConnectedOrConnecting())){
            TextView textView=findViewById(R.id.text);
            textView.setText("NO INTERNET CONNECTION :(");
            textView.setVisibility(View.VISIBLE);
            FrameLayout frameLayout=findViewById(R.id.fragment);
            frameLayout.setVisibility(View.GONE);
            DrawerLayout drawerLayout1=findViewById(R.id.drawer_layout);
            drawerLayout1.setVisibility(View.GONE);
            return;
        }
        setTitle(R.string.latest);
        All fragmentall= new All();
        //begins fragment transaction
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragmentall).commit();
        drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //navigation drawer working
                //Can go from one fragment to other by using navigation drawer.
                Fragment currentFragment=getSupportFragmentManager().findFragmentById(R.id.fragment);
                menuItem.setChecked(true);
                String title = (String) menuItem.getTitle();


                if (title.equals(getString(R.string.latest))){
                    setTitle(R.string.latest);
                    if(currentFragment instanceof All){drawerLayout.closeDrawers();}
                    else{
                        All fragmentall = new All();
                        FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.fragment,fragmentall).commit();
                        drawerLayout.closeDrawers();}

                } else if (title.equals(getString(R.string.business))) {
                    setTitle(R.string.business);
                    if(currentFragment instanceof Business1){drawerLayout.closeDrawers();}
                    else{
                        Business1 fragmentbusiness = new Business1();
                        FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.fragment,fragmentbusiness).commit();
                        drawerLayout.closeDrawers();}

                } else if (title.equals(getString(R.string.politics))) {
                    setTitle(R.string.politics);
                    if(currentFragment instanceof Politics){drawerLayout.closeDrawers();}
                    else{
                        Politics fragmentpolitics = new Politics();
                        FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.fragment,fragmentpolitics).commit();
                        drawerLayout.closeDrawers();}

                } else if (title.equals(getString(R.string.sports))) {
                    setTitle(R.string.sports);
                    if(currentFragment instanceof Sports){drawerLayout.closeDrawers();}
                    else{
                        Sports fragmentsports = new Sports();
                        FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.fragment,fragmentsports).commit();
                        drawerLayout.closeDrawers();}

                }

                return true;


            }
        });



    }
}
