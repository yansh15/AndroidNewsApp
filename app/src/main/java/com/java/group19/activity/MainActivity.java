package com.java.group19.activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.content.Intent;
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
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.java.group19.component.CategorySelectView;
import com.java.group19.helper.HttpHelper;
import com.java.group19.R;
import com.java.group19.adapter.NewsAdapter;
import com.java.group19.helper.SharedPreferencesHelper;
import com.java.group19.listener.OnCategoryChangeListener;
import com.java.group19.listener.OnGetNewsListener;
import com.java.group19.data.News;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    public static int category;
    private static List<List<News>> newsLists;

    private FloatingSearchView searchView;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private CategorySelectView categorySelectView;
    private String lastQuery = "";
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "MainActivity";

    static {
        category = HttpHelper.ALL;
        newsLists = new ArrayList<>();
        for (int i = 0; i < 13; ++i)
            newsLists.add(new ArrayList<News>());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: shp init");
        SharedPreferencesHelper.init(this);
        super.onCreate(savedInstanceState);
        if (SharedPreferencesHelper.getNightMode())
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.LightTheme);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setNavigationView(navigationView);

        searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        categorySelectView = (CategorySelectView) findViewById(R.id.category_view);

        //set
        searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
        setupSearchBar();
        setupRecyclerView();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh);
        setupSwipeRefreshLayout();

        //推荐类别第一次获取数据
        if (newsLists.get(0).isEmpty()) {
            HttpHelper.askLatestNews(10, 0, new OnGetNewsListener() {
                @Override
                public void onFinish(final List<News> newsList) {
                    newsLists.set(0, newsList);
                    Log.d(TAG, "onFinish: " + newsList.size());
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          adapter.addToFirstNewsList(newsLists.get(category));
                                      }
                                  }
                    );
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }
        adapter.addToFirstNewsList(newsLists.get(category));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        categorySelectView.storeCategoryList();
        Log.d(TAG, "onDestroy: ");
        SharedPreferencesHelper.store();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        searchView.setTranslationY(verticalOffset);
    }

    private void setNavigationView(final NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.nav_favorite);
        final SwitchCompat themeSwitch = (SwitchCompat) navigationView.getMenu().getItem(4).getActionView().findViewById(R.id.nav_switch);
        themeSwitch.setChecked(SharedPreferencesHelper.getNightMode());
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MenuItem themeItem = navigationView.getMenu().getItem(4);
                SharedPreferencesHelper.setNightMode(b);
                if (b) {
                    themeItem.setIcon(R.drawable.ic_brightness_2_black);

                } else {
                    themeItem.setIcon(R.drawable.ic_wb_sunny_black);
                }
                Log.d(TAG, "onCheckedChanged: recreate");
                recreate();
            }
        });
        final SwitchCompat modeSwitch = (SwitchCompat) navigationView.getMenu().getItem(5).getActionView().findViewById(R.id.nav_switch);
        modeSwitch.setChecked(SharedPreferencesHelper.getTextMode());
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MenuItem menuItem = navigationView.getMenu().getItem(5);
                SharedPreferencesHelper.setTextMode(b);
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
                    case R.id.nav_voice:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, VoiceSetActivity.class));
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

    private void setupSearchBar() {
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                searchView.swapSuggestions(SharedPreferencesHelper.getSearchRecord(newQuery, 5));
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                searchView.setSearchText(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {
                lastQuery = currentQuery;
                SharedPreferencesHelper.addSearchRecord(lastQuery);
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", lastQuery);
                startActivity(intent);
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                searchView.swapSuggestions(SharedPreferencesHelper.getSearchRecord("", 5));
            }

            @Override
            public void onFocusCleared() {
                searchView.setSearchBarTitle("");
            }
        });
        searchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                leftIcon.setImageResource(R.drawable.ic_history_black);
                textView.setText(item.getBody());
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(null);
        recyclerView.setAdapter(adapter);

        categorySelectView.setCurrentCategory(category);

        categorySelectView.setOnCategoryChangeListener(new OnCategoryChangeListener() {
            @Override
            public void onCategoryChange(final int cate) {
                swipeRefreshLayout.setRefreshing(false);
                category = cate;
                Log.d(TAG, "onCategoryChange: cate" + cate);
                if (newsLists.get(cate).isEmpty()) { // 新类别第一次获取数据
                    adapter.addToFirstNewsList(new ArrayList<News>());
                    HttpHelper.askLatestNews(10, cate, new OnGetNewsListener() {
                        @Override
                        public void onFinish(final List<News> newsList) {
                            Log.d(TAG, "onFinish: " + newsList.size());
                            newsLists.set(cate, newsList);
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  adapter.addToFirstNewsList(newsLists.get(category));
                                              }
                                          }
                            );
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
                } else {
                    adapter.addToFirstNewsList(newsLists.get(category));
                }
            }
        });
    }

    private void setupSwipeRefreshLayout() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        swipeRefreshLayout.setColorSchemeColors(typedValue.data);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { // 上拉刷新
                final int cate = category;
                HttpHelper.askLatestNews(10, cate, new OnGetNewsListener() {
                    @Override
                    public void onFinish(final List<News> newsList) {
                        Log.d(TAG, "onFinish: " + newsList.size());
                        List<News> oldList = newsLists.get(cate);
                        for (News news : oldList)
                            newsList.add(news);
                        newsLists.set(cate, newsList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addToFirstNewsList(newsLists.get(category));
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
            }
        });

    }
}