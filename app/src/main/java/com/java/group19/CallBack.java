package com.java.group19;

import java.util.List;

/**
 * Created by Lzl on 2017/9/9.
 */

public interface CallBack {
    public void onFinish(List<News> newsList);
    public void onFinish();
    public void onError(Exception e);
}
