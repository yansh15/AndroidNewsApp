package com.java.group19;

import android.graphics.drawable.BitmapDrawable;

import com.iflytek.cloud.SpeechUtility;
import com.java.group19.helper.SpeechHelper;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import in.srain.cube.Cube;
import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.ImageTask;
import in.srain.cube.image.iface.ImageLoadHandler;

public class NewsApp extends LitePalApplication {
    private SpeechHelper speechHelper;
    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        super.onCreate();
        Cube.onCreate(this);
        SpeechUtility.createUtility(NewsApp.this, "appid=" + getString(R.string.app_id));
        Connector.getDatabase();
        speechHelper = SpeechHelper.getInstance(this);
        imageLoader = ImageLoaderFactory.create(this);
        imageLoader.setImageLoadHandler(new ImageLoadHandler() {
            @Override
            public void onLoading(ImageTask imageTask, CubeImageView cubeImageView) {
            }

            @Override
            public void onLoadFinish(ImageTask imageTask, CubeImageView cubeImageView, BitmapDrawable drawable) {
                cubeImageView.setImageDrawable(drawable);
            }

            @Override
            public void onLoadError(ImageTask imageTask, CubeImageView imageView, int errorCode) {
                imageView.setImageResource(R.drawable.ic_menu_black);
            }
        });
    }

    public SpeechHelper getSpeechHelper() {
        return speechHelper;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
