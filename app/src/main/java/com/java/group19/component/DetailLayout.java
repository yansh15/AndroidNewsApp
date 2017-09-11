package com.java.group19.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.group19.R;

import java.util.List;

/**
 * Created by 阎世宏 on 2017/9/11.
 */

public class DetailLayout extends LinearLayout {

    private TextView title;
    private TextView author;
    private TextView date;
    private ImageView imageOne;
    private TextView content;
    private LinearLayout imageLayout;
    private LinearLayout classTagLayout;
    private TextView source;

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
    
    public CharSequence getTitle() {
        return title.getText();
    }
    
    public void setTitle(CharSequence charSequence) {
        title.setText(charSequence);
    }
    
    public CharSequence getAuthor() {
        return author.getText();
    }
    
    public void setAuthor(CharSequence charSequence) {
        author.setText(charSequence);
    }
    
    public CharSequence getDate() {
        return date.getText();
    }
    
    public void setDate(CharSequence charSequence) {
        date.setText(charSequence);
    }
    
    public CharSequence getContent() {
        return content.getText();
    }
    
    public void setContent(CharSequence charSequence) {
        content.setText(charSequence);
    }
    
    public CharSequence getSource() {
        return source.getText();
    }
    
    public void setSource(CharSequence charSequence) {
        source.setText(charSequence);
    }
    
    public void addImage(ImageView view) {
        //// TODO: 2017/9/11  
    }
}
