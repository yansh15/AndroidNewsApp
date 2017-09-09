package com.java.group19;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.List;
import java.util.Vector;

import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Vector<News> newsList = new Vector<>();
    private NewsAdapter adapter;
    private String mLastQuery;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Connector.getDatabase();

        /*//set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set menu button
        
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLastQuery = "";

        //set floatingsearchview
        setFloatingSearchView();

        //set navigation
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_favorite);
        final SwitchCompat themeSwitch = (SwitchCompat) navigationView.getMenu().getItem(3).getActionView().findViewById(R.id.theme_switch);
        themeSwitch.setChecked(DatabaseHelper.isDarkTheme());
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        final SwitchCompat modeSwitch = (SwitchCompat) navigationView.getMenu().getItem(4).getActionView().findViewById(R.id.text_mode_switch);
        modeSwitch.setChecked(false);
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MenuItem menuItem = navigationView.getMenu().getItem(4);
                adapter.setTextMode(b);
                if (b)
                    menuItem.setIcon(R.drawable.ic_title_black);
                else
                    menuItem.setIcon(R.drawable.ic_image_black);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_favorite:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                        break;
                    case R.id.nav_history:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, VisitedActivity.class));
                        break;
                    case R.id.nav_shield:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, ForbiddenActivity.class));
                        break;
                    case R.id.nav_theme:
                        themeSwitch.toggle();
                        break;
                    case R.id.nav_text_mode:
                        modeSwitch.toggle();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //set RecycleView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(newsList, DatabaseHelper.isTextMode());
        recyclerView.setAdapter(adapter);
        //getLatestNews();

        //set SwipeRefresh
        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //todo refresh
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;*/
            default:
                break;
        }
        return true;
    }

    private void getLatestNews() {
        HttpHelper.askLatestNews(0, new CallBack() {
            @Override
            public void onFinishNewsList(List<News> newsList) {
                Log.d(TAG, "onFinishNewsList: " + newsList.size());
                adapter.addNewsList(newsList);
            }

            @Override
            public void onFinishDetail(List<Bitmap> bitmaps) {

            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: " + e);
            }
        });
    }
    
    private void setFloatingSearchView() {
        final FloatingSearchView searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
        /*searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    default:
                        break;
                }
            }
        });*/
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Log.d(TAG, "onSearchTextChanged: enter");
                List<SearchRecordSuggestion> suggestions = new Vector<>();
                List<String> tmps = DatabaseHelper.getLatestSearchRecords(newQuery, 10);
                for (String s : tmps)
                    suggestions.add(new SearchRecordSuggestion(s));
                Log.d(TAG, "onSearchTextChanged: size of suggest " + suggestions.size());
                searchView.swapSuggestions(suggestions);
                Log.d(TAG, "onSearchTextChanged: end");
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Log.d(TAG, "onSuggestionClicked: ");
                mLastQuery = searchSuggestion.getBody();
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", mLastQuery);
                startActivity(intent);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                Log.d(TAG, "onSearchAction: ");
                mLastQuery = currentQuery;
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", mLastQuery);
                startActivity(intent);
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                Log.d(TAG, "onFocus: ");
                List<SearchRecordSuggestion> suggestions = new Vector<>();
                List<String> tmps = DatabaseHelper.getLatestSearchRecords("", 10);
                for (String s : tmps)
                    suggestions.add(new SearchRecordSuggestion(s));
                Log.d(TAG, "onFocus: size " + suggestions.size());
                searchView.swapSuggestions(suggestions);
            }

            @Override
            public void onFocusCleared() {
                Log.d(TAG, "onFocusCleared: ");
                searchView.setSearchBarTitle(mLastQuery);
            }
        });
    }
}
