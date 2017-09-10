package com.java.group19;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.database.DatabaseUtilsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.Comparator;
import java.util.List;

/**
 * Created by liena on 17/9/10.
 */

public class ScrollingSearchFragment extends BaseSearchFragment implements AppBarLayout.OnOffsetChangedListener {
    
    private static final String TAG = "ScrollingSearchFragment";
    
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    
    private FloatingSearchView searchView;
    
    private AppBarLayout appBarLayout;

    private RecyclerView recyclerView;

    private NewsAdapter adapter;

    private int pageNo;
    
    private boolean isDarkSearchTheme = false;
    
    private String lastQuery = "";
    
    public ScrollingSearchFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scrolling_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        appBarLayout.addOnOffsetChangedListener(this);
        pageNo = 1;
        setupDrawer();
        setupSearchBar();
        setupRecyclerView();
        updateLatestNewsList();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        searchView.setTranslationY(verticalOffset);
    }

    @Override
    public boolean onActivityBackPress() {
        if (!searchView.setSearchFocused(false))
            return false;
        return true;
    }

    public NewsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(NewsAdapter adapter) {
        this.adapter = adapter;
    }

    private void setupDrawer() {
        attachSearchViewActivityDrawer(searchView);
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
//                lastQuery = searchSuggestion.getBody();
                searchView.setSearchText(searchSuggestion.getBody());
//                DatabaseHelper.addSearchRecord(lastQuery);
//                Intent intent = new Intent(getActivity(), SearchActivity.class);
//                intent.putExtra("query", lastQuery);
//                startActivity(intent);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                lastQuery = currentQuery;
                DatabaseHelper.addSearchRecord(lastQuery);
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("query", lastQuery);
                startActivity(intent);
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                SearchHelper.findSuggestions("", 5, new SearchHelper.OnFindSuggestionsListener() {
                    @Override
                    public void onResults(List<SearchRecordSuggestion> results) {
                        searchView.swapSuggestions(results);
                    }
                });
            }

            @Override
            public void onFocusCleared() {
                searchView.setSearchBarTitle(lastQuery);
            }
        });

        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_change_colors:
                        isDarkSearchTheme = true;
                        //demonstrate setting colors for items
                        searchView.setBackgroundColor(Color.parseColor("#787878"));
                        searchView.setViewTextColor(Color.parseColor("#e9e9e9"));
                        searchView.setHintTextColor(Color.parseColor("#e9e9e9"));
                        searchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
                        searchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
                        searchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                        searchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
                        searchView.setDividerColor(Color.parseColor("#BEBEBE"));
                        searchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                        break;
                    //// TODO: 17/9/10
                    default:
                        break;
                }
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(new Comparator<News>() {
            @Override
            public int compare(News news, News t1) {
                long diff = news.getTime().getTime() - t1.getTime().getTime();
                if (diff > 0) return -1;
                if (diff == 0) return 0;
                return 1;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void updateLatestNewsList() {
        HttpHelper.askLatestNews(pageNo, new CallBack() {
            @Override
            public void onFinishNewsList(final List<News> newsList) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addNewsListRondom(newsList);
                    }
                });
            }

            @Override
            public void onFinishDetail() {

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
