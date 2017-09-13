package com.java.group19.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.java.group19.R;
import com.java.group19.helper.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by 阎世宏 on 2017/9/13.
 */

public class ForbiddenWordAdapter extends RecyclerView.Adapter<ForbiddenWordAdapter.ViewHolder> {

    private Context context;
    private List<String> forbiddenWordList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        ImageView delete;

        public ViewHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.forbidden_word_item_content);
            delete = (ImageView) view.findViewById(R.id.forbidden_word_item_delete);
        }
    }

    public ForbiddenWordAdapter() {
        forbiddenWordList = SharedPreferencesHelper.getForbiddenWord();
        Collections.sort(forbiddenWordList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null)
            context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.forbidden_word_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String word = forbiddenWordList.get(position);
        holder.content.setText(word);
        holder.delete.setTag(word);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = (String) view.getTag();
                SharedPreferencesHelper.removeForbiddenWord(word);
                forbiddenWordList.remove(word);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return forbiddenWordList.size();
    }

    public void addForbiddenWord(String word) {
        SharedPreferencesHelper.addForbiddenWord(word);
        forbiddenWordList.add(word);
        Collections.sort(forbiddenWordList);
        notifyDataSetChanged();
    }
}
