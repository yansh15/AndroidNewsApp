package com.java.group19;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Lzl on 2017/9/9.
 */

public class Test implements CallBack {

    @Override
    public void onFinishNewsList(List<News> newsList) {
        // 测试查询新闻内容
        if (!newsList.isEmpty())
            for (News news : newsList)
                HttpHelper.askDetailNews(news, this);
    }

    public void onFinishDetail(List<Bitmap> bitmaps) {

    }

    @Override
    public void onError(Exception e) {

    }

    Test() {
        // 测试关键词搜索
        HttpHelper.askKeywordNews("北京", 11, 1, this);
    }
}
