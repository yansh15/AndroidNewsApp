package com.java.group19;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;

import java.util.Vector;

import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Vector<News> newsList = new Vector<>();
    private NewsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Connector.getDatabase();

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set menu button
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }

        //set navigation
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_favorite);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_favorite:
                        //todo favorite
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_history:
                        //todo history
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_shield:
                        //todo shield
                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        SwitchCompat switchCompat = (SwitchCompat) navigationView.getMenu().getItem(3).getActionView().findViewById(R.id.theme_switch);
        switchCompat.setChecked(false);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MenuItem themeItem = navigationView.getMenu().getItem(3);
                if (b) {
                    themeItem.setIcon(R.drawable.ic_brightness_2_black);
                    //todo night
                } else {
                    themeItem.setIcon(R.drawable.ic_wb_sunny_black);
                    //todo day
                }
            }
        });
        switchCompat = (SwitchCompat) navigationView.getMenu().getItem(4).getActionView().findViewById(R.id.text_mode_switch);
        switchCompat.setChecked(false);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MenuItem menuItem = navigationView.getMenu().getItem(4);
                if (b) {
                    menuItem.setIcon(R.drawable.ic_title_black);
                    //todo
                } else {
                    menuItem.setIcon(R.drawable.ic_image_black);
                    //todo
                }
            }
        });

        //set RecycleView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(newsList, true); //todo false
        recyclerView.setAdapter(adapter);

        //set SwipeRefresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //todo refresh
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }
}
