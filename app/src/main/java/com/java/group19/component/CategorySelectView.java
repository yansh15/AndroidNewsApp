package com.java.group19.component;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.java.group19.R;
import com.java.group19.adapter.CategoryAdapter;
import com.java.group19.adapter.CategorySetAdaper;
import com.java.group19.helper.SharedPreferencesHelper;
import com.java.group19.listener.OnCategoryChangeListener;
import com.java.group19.listener.OnCategorySetListener;

public class CategorySelectView extends LinearLayout {

    private Context context;
    private ImageView control;
    private RecyclerView displayView;
    private View splitView;
    private RecyclerView setView;
    private LinearLayout controlLayout;

    private CategoryAdapter displayAdapter;
    private CategorySetAdaper setAdaper;

    public CategorySelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.category_select_view, this);
        control = (ImageView) findViewById(R.id.view_control);
        displayView = (RecyclerView) findViewById(R.id.category_display_view);
        controlLayout = (LinearLayout) findViewById(R.id.category_control_view);
        setView = (RecyclerView) findViewById(R.id.category_set_view);
        splitView = (View) findViewById(R.id.category_split_view);
        splitView.setVisibility(GONE);
        controlLayout.setVisibility(GONE);


        control.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (control.getTag().equals("collapse")) {
                    control.setTag("expand");
                    control.setImageResource(R.drawable.ic_keyboard_arrow_up);
                    displayAdapter.changeClickListening(true);
                    controlLayout.setVisibility(VISIBLE);
                    splitView.setVisibility(VISIBLE);
                } else {
                    control.setTag("collapse");
                    control.setImageResource(R.drawable.ic_add_black);
                    displayAdapter.changeClickListening(false);
                    controlLayout.setVisibility(GONE);
                    splitView.setVisibility(GONE);
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        displayView.setLayoutManager(linearLayoutManager);
        displayAdapter = new CategoryAdapter(SharedPreferencesHelper.getCategoryList());
        displayView.setAdapter(displayAdapter);

        GridLayoutManager setGridLayoutManage = new GridLayoutManager(context, 6, LinearLayoutManager.VERTICAL, false);
        setView.setLayoutManager(setGridLayoutManage);
        setAdaper = new CategorySetAdaper(SharedPreferencesHelper.getCategoryList());
        setAdaper.setOnCategorySetListener(new OnCategorySetListener() {
            @Override
            public void onCategorySetListener(int cate, boolean isAdd) {
                displayAdapter.changeCategory(cate, isAdd);
            }
        });
        setView.setAdapter(setAdaper);
    }

    public void setOnCategoryChangeListener(OnCategoryChangeListener listener) {
        displayAdapter.setOnCategoryChangeListener(listener);
    }

    public void storeCategoryList() {
        displayAdapter.storeCategoryList();
    }

    public void setCurrentCategory(int cate) {
        displayAdapter.setCurrentCategory(cate);
    }
}
