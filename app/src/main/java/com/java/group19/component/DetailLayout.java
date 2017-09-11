package com.java.group19.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.group19.R;

/**
 * Created by 阎世宏 on 2017/9/11.
 */

public class DetailLayout extends LinearLayout {

    private interface OnDataChange {
        void onDataChange(Adapter adapter);
    }

    public abstract static class Adapter {
        public abstract String getTitle();
        public abstract String getAuthor();
        public abstract String getDate();
        public abstract String getContent();
        public abstract String getSource();
        public void notifyDataChange() {

        }
    }

    private TextView title;
    private TextView author;
    private TextView date;
    private ImageView imageOne;
    private TextView content;
    private LinearLayout imageLayout;
    private LinearLayout classTagLayout;
    private TextView source;

    private Adapter adapter;

    public DetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.detail_item, this);
        title = (TextView) findViewById(R.id.detail_title);
        author = (TextView) findViewById(R.id.detail_author);
        date = (TextView) findViewById(R.id.detail_date);
        imageOne = (ImageView) findViewById(R.id.detail_image);
        imageOne.setVisibility(GONE);
        content = (TextView) findViewById(R.id.detail_content);
        imageLayout = (LinearLayout) findViewById(R.id.detail_image_layout);
        imageLayout.setVisibility(GONE);
        classTagLayout = (LinearLayout) findViewById(R.id.detail_classtag_layout);
        classTagLayout.setVisibility(GONE);
        source = (TextView) findViewById(R.id.detail_source);
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    private void update() {
        title.setText(adapter.getTitle());
        author.setText(adapter.getAuthor());
        date.setText(adapter.getDate());
        content.setText(adapter.getDate());
        source.setText(adapter.getSource());
    }
}
