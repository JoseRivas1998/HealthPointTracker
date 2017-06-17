package com.tcg.healthpointtracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TCGCampaignManager campaignManager;

    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        campaignManager = new TCGCampaignManager(this);
        campaignManager.loadCampaigns(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addItemsNavigation();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.i(getClass().getName(), "" + id);

        FragmentManager fragmentManager = getFragmentManager();

        if(id == R.id.nav_add_campaign) {
            fragmentManager.
                    beginTransaction()
                    .replace(R.id.content_frame, new AddCampaignFragment())
                    .commit();
        } else if(id < campaignManager.size()) {
            CampaignFragment campaignFragment = createCampaignFragment(id);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, campaignFragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private CampaignFragment createCampaignFragment(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        CampaignFragment campaignFragment = new CampaignFragment();
        campaignFragment.setArguments(bundle);
        campaignFragment.setUpdateNavigationListener((item) -> {
            addItemsNavigation(item);
        });
        return campaignFragment;
    }

    private void addItemsNavigation(int id) {
        final Menu menu = navigationView.getMenu();

        FragmentManager fragmentManager = getFragmentManager();
        if(menu.size() > 1) {
            menu.removeGroup(R.id.main_group);
            addItemsNavigation(id);
        } else {
            if(campaignManager.size() > 0) {
                for (int i = 0; i < campaignManager.size(); i++) {
                    menu.add(R.id.main_group, i, i + 1, campaignManager.getCampaignTitle(i));
                }
                CampaignFragment campaignFragment = createCampaignFragment(id);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, campaignFragment)
                        .commit();

            } else {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, new AddCampaignFragment())
                        .commit();
            }
        }
    }

    private void addItemsNavigation() {
        addItemsNavigation(0);
    }
}
