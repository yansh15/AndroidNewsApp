package com.java.group19.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.group19.listener.OnCategoryChangeListener;
import com.java.group19.R;

/**
 * Created by 阎世宏 on 2017/9/11.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final static String[] categorys = {"推荐", "科技", "教育", "军事", "国内", "社会",
            "文化", "汽车", "国际", "体育", "经济", "健康", "娱乐"};
    private Context context;
    private OnCategoryChangeListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view;
        }
    }

    public CategoryAdapter(OnCategoryChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null)
            context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String s = categorys[position];
        holder.textView.setText(s);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCategoryChange(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorys.length;
    }
}
