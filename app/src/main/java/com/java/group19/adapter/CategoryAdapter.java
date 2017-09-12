package com.java.group19.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.group19.listener.OnCategoryChangeListener;
import com.java.group19.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 阎世宏 on 2017/9/11.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final static String[] categorys = {"推荐", "科技", "教育", "军事", "国内", "社会",
            "文化", "汽车", "国际", "体育", "经济", "健康", "娱乐"};
    private Context context;
    private List<Integer> mList;
    private OnCategoryChangeListener listener;
    private int highLight = 0;

    private View.OnClickListener clickListener;
    private View.OnClickListener cl;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = (TextView) view;
        }
    }

    public CategoryAdapter() {
        mList = new ArrayList<>();
        mList.add(0);
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) view;
                highLight = (Integer) textView.getTag();
                if (listener != null)
                    listener.onCategoryChange(mList.get(highLight));
                notifyDataSetChanged();
            }
        };
        cl = clickListener;
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
        holder.textView.setText(categorys[mList.get(position)]);
        holder.textView.setTag(position);
        if (position == highLight) {
            holder.textView.setTextColor(holder.textView.getHighlightColor());
        } else {
            holder.textView.setTextColor(holder.textView.getTextColors());
        }
        holder.textView.setOnClickListener(cl);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void changeCategory(int cate, boolean isAdd) {
        if (isAdd) {
            mList.add(cate);
            Collections.sort(mList);
            notifyDataSetChanged();
        } else {
            mList.remove(Integer.valueOf(cate));
            if (highLight == cate)
                highLight = 0;
            notifyDataSetChanged();
        }
    }

    public void setOnCategoryChangeListener(OnCategoryChangeListener listener) {
        this.listener = listener;
    }

    public void changeClickListening(boolean isForbidden) {
        if (isForbidden) {
            cl = null;
            notifyDataSetChanged();
        } else {
            cl = clickListener;
            notifyDataSetChanged();
        }
    }
}
