package com.java.group19;

import android.support.v4.app.Fragment;
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

public class MainActivity extends AppCompatActivity
        implements BaseSearchFragment.BaseSearchFragmentCallbacks {

    private DrawerLayout mDrawerLayout;
    private Vector<News> newsList = new Vector<>();
    private NewsAdapter adapter;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper.init();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setNavigationView(navigationView);

        showFragment(new ScrollingSearchFragment());
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

    @Override
    public void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
        searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
    }

    @Override
    public void onBackPressed() {
        /*List fragments = getSupportFragmentManager().getFragments();
        BaseSearchFragment currentFragement = (BaseSearchFragment) fragments.get(fragments.size() - 1);

        if (!currentFragement.onActivityBackPress())*/
            super.onBackPressed();
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
    
    private void setNavigationView(final NavigationView navigationView) {
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
                //// TODO: 17/9/10  
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
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

}
