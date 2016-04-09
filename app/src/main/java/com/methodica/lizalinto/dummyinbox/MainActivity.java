package com.methodica.lizalinto.dummyinbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsFragment.OnNewsFragmentInteractionListener {

    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState == null){
            navigationView.setCheckedItem(R.id.nav_news);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, NewsFragment.newInstance(),"news").commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof NewsFragment ) {
            mtoolbar.setTitle(R.string.nav_news_string);
        }else{
            mtoolbar.setTitle(R.string.nav_map_string);

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            mtoolbar.setTitle(R.string.nav_news_string);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, NewsFragment.newInstance(),"news").commit();

        } else if (id == R.id.nav_map) {
            mtoolbar.setTitle(R.string.nav_map_string);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MapFragment.newInstance(),"map").commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void OnNewsFragmentInteraction(NewsContent.NewsItem mItem) {
        Intent intent = new Intent(this,DetailNewsActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("detailLink", mItem.link);
        startActivity(intent, bundle);
    }
}
