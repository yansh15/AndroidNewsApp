package com.java.group19;

import com.iflytek.cloud.SpeechUtility;
import com.java.group19.helper.SpeechHelper;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;

public class NewsApp extends LitePalApplication {
    private SpeechHelper speechHelper;
    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        SpeechUtility.createUtility(NewsApp.this, "appid=" + getString(R.string.app_id));
        Connector.getDatabase();
        speechHelper = SpeechHelper.getInstance(this);
        imageLoader = ImageLoaderFactory.create(this);
        super.onCreate();
    }

    public SpeechHelper getSpeechHelper() {
        return speechHelper;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
