package com.java.group19;

import android.graphics.drawable.BitmapDrawable;

import com.iflytek.cloud.SpeechUtility;
import com.java.group19.helper.SpeechHelper;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        if (!getFilesDir().exists()) {
            getFilesDir().mkdir();
        }
        copyFromAssetsToDatabases("NewsDatabase.db");
        copyFromAssetsToDatabases("NewsDatabase.db-journal");
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
                if (imageView != null) {
                    imageView.setImageResource(R.drawable.ic_menu_black);
                }
            }
        });
    }

    public SpeechHelper getSpeechHelper() {
        return speechHelper;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    private void copyFromAssetsToDatabases(String fileName) {
        String dirPath = getFilesDir().getParent() + "/databases";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            InputStream is = getResources().getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            fos.write(buffer);
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
