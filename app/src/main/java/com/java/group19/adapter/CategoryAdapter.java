package com.java.group19.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.group19.helper.SharedPreferencesHelper;
import com.java.group19.listener.OnCategoryChangeListener;
import com.java.group19.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by 阎世宏 on 2017/9/11.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private static final String TAG = "CategoryAdapter";

    private final static String[] categorys = {"推荐", "科技", "教育", "军事", "国内", "社会",
            "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"};
    private Context context;
    private List<Integer> mList;
    private OnCategoryChangeListener listener;
    private int currentCage = 0;

    private View.OnClickListener clickListener;
    private View.OnClickListener cl;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = (TextView) view;
        }
    }

    public CategoryAdapter(List<Integer> list) {
        mList = list;
        Collections.sort(mList);
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: prev hight" + currentCage);
                TextView textView = (TextView) view;
                currentCage = (Integer) textView.getTag();
                Log.d(TAG, "onClick: now hight" + currentCage);
                if (listener != null)
                    listener.onCategoryChange(currentCage);
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
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[] {
                R.attr.colorAccent,
                R.attr.textColorPrimary,
        });
        final int highColor = array.getColor(0, 0xFF00FF);
        final int textColor = array.getColor(1, 0xFF00FF);
        holder.textView.setText(categorys[mList.get(position)]);
        holder.textView.setTag(mList.get(position));
        Log.d(TAG, "onBindViewHolder: hight" + currentCage);
        if (mList.get(position) == currentCage) {
            holder.textView.setTextColor(highColor);
        } else {
            holder.textView.setTextColor(textColor);
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
            if (currentCage == cate)
                currentCage = 0;
            listener.onCategoryChange(currentCage);
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

    public void storeCategoryList() {
        SharedPreferencesHelper.putCategoryList(mList);
    }

    public void setCurrentCategory(int cate) {
        currentCage = cate;
    }
}
