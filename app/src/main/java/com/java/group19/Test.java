package com.java.group19;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * Created by Lzl on 2017/9/9.
 */

public class Test implements CallBack {

    Context context;
    @Override
    public void onFinishNewsList(List<News> newsList) {
        // 测试查询新闻内容
        int cnt = 0;
        if (!newsList.isEmpty())
            for (News news : newsList) {
                cnt += news.getPictures().size();
                HttpHelper.askDetailNews(context, news, this);
            }
        Log.d("Test", "onFinishNewsList: "+cnt);
    }

    public void onFinishDetail() {

    }

    @Override
    public void onError(Exception e) {

    }

    Test(Context context) {
        // 测试关键词搜索
        this.context = context;
        File file = Environment.getDataDirectory().getAbsoluteFile(); //注意小米手机必须这样获得public绝对路径
        file = new File(file, "/data/com.java.group19/");
        Log.d("Test", "saveImageToDevice: " + file.toString());
        HttpHelper.askKeywordNews("北京", 11, 1, this);
    }
}
