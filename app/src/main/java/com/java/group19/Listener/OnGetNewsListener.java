package com.java.group19.Listener;

import com.java.group19.News;

import java.util.List;

public interface OnGetNewsListener {
    void onFinish(List<News> newsList);
    void onError(Exception e);
}
