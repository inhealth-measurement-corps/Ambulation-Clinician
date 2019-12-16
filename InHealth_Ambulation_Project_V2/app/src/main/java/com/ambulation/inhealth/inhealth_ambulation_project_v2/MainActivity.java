package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AsyncResponse{

    private DrawerLayout mDrawerLayout;
    private SharedViewModel model;
    private List<Patient> patient_list;
    private HashMap<String, Integer> titleToIndex;
    private String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date d = new Date();
        today = (String)DateFormat.format("yyyy-MM-dd", d.getTime());
        Log.d("today", today);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        patient_list = new ArrayList<>();
        titleToIndex = new HashMap<>();
        // set toolbar as the actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        tapOnDrawer();

        // load data and set data in model
        this.model = ViewModelProviders.of(this).get(SharedViewModel.class);


        new PatientListHelper(this, this).execute(today);

        // default: home fragment
        Fragment fragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.home_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void processFinish(Object o) {
        patient_list = (List<Patient>)o;
        if(patient_list != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("all_patient", MODE_PRIVATE);
            sharedPreferences.edit().putInt("length", patient_list.size());

            this.model.setPatientList(patient_list);
            populateMenu();
        }
    }

    private void populateMenu() {
        NavigationView navView = findViewById(R.id.nav_view);
        SubMenu roomMenu = navView.getMenu().addSubMenu("Room List");
        for(int i = 0; i < patient_list.size(); i++) {
            titleToIndex.put("Room #"+patient_list.get(i).getRoom(), i);
            roomMenu.add("Room #"+patient_list.get(i).getRoom()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.d("menu", (String)item.getTitle());
                    goToDetails((String) item.getTitle());
                    return true;
                }
            });
        }

        this.model.getFav_room().observe(this, room -> {
            SubMenu favMenu = navView.getMenu().addSubMenu("Fav Room");
            favMenu.add("Room #"+room).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.d("menu", (String)item.getTitle());
                    goToDetails((String) item.getTitle());
                    return false;
                }
            });
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void goToHome() {
        Fragment fragment = new HomeFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.home_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void goToDetails(String title) {
        int index = titleToIndex.get(title);
        Log.d("index", Integer.toString(index));
       Fragment fragment = new DetailFragment();
       Bundle args = new Bundle();
       args.putInt("index", index);
       args.putInt("length", patient_list.size());
       fragment.setArguments(args);
       FragmentManager manager = getSupportFragmentManager();
       FragmentTransaction transaction = manager.beginTransaction();
       transaction.replace(R.id.home_fragment, fragment);
       transaction.addToBackStack(null);
       transaction.commit();
    }

    private void goToList() {
        Fragment fragment = new UnitListFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.home_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void tapOnDrawer() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Log.d("menuId", Integer.toString(menuItem.getItemId()));

                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.unit_detail:
                                goToList();
                                return true;
                            case R.id.unit_map:
                                model.setCurrentState("Unit");
                                goToHome();
                                return true;

                        }
                        return true;

                    }
                }
        );
    }
}
