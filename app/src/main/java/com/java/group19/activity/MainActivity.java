package com.java.group19.activity;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;import android.widget.TextView;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.java.group19.TextSpeaker;
import com.java.group19.helper.DatabaseHelper;
import com.java.group19.helper.HttpHelper;
import com.java.group19.R;
import com.java.group19.helper.SearchHelper;
import com.java.group19.SearchRecordSuggestion;
import com.java.group19.adapter.CategoryAdapter;
import com.java.group19.adapter.NewsAdapter;
import com.java.group19.listener.OnCategoryChangeListener;
import com.java.group19.listener.OnGetNewsListener;
import com.java.group19.data.News;
import in.srain.cube.image.ImageLoader;

import java.util.Comparator;
import java.util.List;

import in.srain.cube.image.ImageLoaderFactory;

public class MainActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private FloatingSearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerView categoryRecyclerView;
    private NewsAdapter[] adapter;
    private CategoryAdapter categoryAdapter;
    public static int category = HttpHelper.ALL;
    private String lastQuery = "";
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextSpeaker textSpeaker;
    private ImageLoader imageLoader;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper.init();
        textSpeaker = TextSpeaker.getInstance(this);
        imageLoader = ImageLoaderFactory.create(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setNavigationView(navigationView);

        searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        categoryRecyclerView = (RecyclerView) findViewById(R.id.category_recycle_view);

        //set
        searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
        setupSearchBar();
        setupRecyclerView();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh);
        setupSwipeRefreshLayout();

        //推荐类别第一次获取数据
        HttpHelper.askLatestNews(10, 0, new OnGetNewsListener() {
            @Override
            public void onFinish(List<News> newsList) {
                adapter[0].setNewsList(newsList);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        searchView.setTranslationY(verticalOffset);
    }

    private void setNavigationView(final NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.nav_favorite);
        final SwitchCompat themeSwitch = (SwitchCompat) navigationView.getMenu().getItem(3).getActionView().findViewById(R.id.nav_switch);
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
        final SwitchCompat modeSwitch = (SwitchCompat) navigationView.getMenu().getItem(4).getActionView().findViewById(R.id.nav_switch);
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

    private void setupSearchBar() {
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                searchView.showProgress();
                SearchHelper.findSuggestions(newQuery, 5, new SearchHelper.OnFindSuggestionsListener() {
                    @Override
                    public void onResults(List<SearchRecordSuggestion> results) {
                        searchView.swapSuggestions(results);
                        searchView.hideProgress();
                    }
                });
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
                DatabaseHelper.addSearchRecord(lastQuery);
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", lastQuery);
                startActivity(intent);
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                searchView.showProgress();
                SearchHelper.findSuggestions("", 5, new SearchHelper.OnFindSuggestionsListener() {
                    @Override
                    public void onResults(List<SearchRecordSuggestion> results) {
                        searchView.swapSuggestions(results);
                        searchView.hideProgress();
                    }
                });
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
        adapter = new NewsAdapter[13];
        for (int i = 0; i < 13; ++i) {
            adapter[i] = new NewsAdapter(new Comparator<News>() {
                @Override
                public int compare(News news, News t1) {
                    long diff = news.getTime().getTime() - t1.getTime().getTime();
                    if (diff > 0) return -1;
                    if (diff == 0) return 0;
                    return 1;
                }
            }, imageLoader);
        }
        recyclerView.setAdapter(adapter[category]);
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(this);
        categoryLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);
        categoryAdapter = new CategoryAdapter(new OnCategoryChangeListener() {
            @Override
            public void onCategoryChange(int cate) {
                category = cate;
                recyclerView.setAdapter(adapter[category]);
                if (adapter[category].isEmpty()) { // 新类别第一次获取数据
                    HttpHelper.askLatestNews(10, 0, new OnGetNewsListener() {
                        @Override
                        public void onFinish(List<News> newsList) {
                            adapter[category].setNewsList(newsList);
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
                }
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { // 上拉刷新
                HttpHelper.askLatestNews(10, 0, new OnGetNewsListener() {
                    @Override
                    public void onFinish(List<News> newsList) {
                        adapter[category].setNewsList(newsList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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