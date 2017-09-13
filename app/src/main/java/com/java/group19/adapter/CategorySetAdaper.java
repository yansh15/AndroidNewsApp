package com.java.group19.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.group19.R;
import com.java.group19.listener.OnCategorySetListener;

import java.util.List;

/**
 * Created by 阎世宏 on 2017/9/12.
 */

public class CategorySetAdaper extends RecyclerView.Adapter<CategorySetAdaper.ViewHolder> {

    private final static String[] categorys = {"推荐", "科技", "教育", "军事", "国内", "社会",
            "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"};
    private Context context;
    private boolean[] highlight = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    private OnCategorySetListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view;
        }
    }

    public CategorySetAdaper(List<Integer> list) {
        for (int i : list)
            highlight[i] = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null)
            context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int cate = position + 1;
        holder.textView.setText(categorys[cate]);
        holder.textView.setTag(cate);
        if (highlight[cate])
            holder.textView.setTextColor(holder.textView.getHighlightColor());
        else
            holder.textView.setTextColor(holder.textView.getTextColors());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cate = (Integer) view.getTag();
                TextView textView = (TextView) view;
                if (highlight[cate]) {
                    highlight[cate] = false;
                    textView.setTextColor(textView.getTextColors());
                    if (listener != null)
                        listener.onCategorySetListener(cate, false);
                } else {
                    highlight[cate] = true;
                    textView.setTextColor(textView.getHighlightColor());
                    if (listener != null)
                        listener.onCategorySetListener(cate, true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public void setOnCategorySetListener(OnCategorySetListener listener) {
        this.listener = listener;
    }
}
