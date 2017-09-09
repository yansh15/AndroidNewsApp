package com.java.group19;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Lzl on 2017/9/9.
 */

public interface CallBack {
    public void onFinishNewsList(List<News> newsList);
    public void onFinishDetail(List<Bitmap> bitmaps);
    public void onError(Exception e);
}
