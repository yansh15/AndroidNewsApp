package com.java.group19.listener;

import com.java.group19.data.News;

import java.util.List;

public interface OnGetNewsListener {
    void onFinish(List<News> newsList);
    void onError(Exception e);
}
