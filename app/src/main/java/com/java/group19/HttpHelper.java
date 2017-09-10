package com.java.group19;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lzl on 2017/9/8.
 */

public class HttpHelper {
    private static OkHttpClient client;
    private static String rootURL = "http://166.111.68.66:2042/news/action/query/";
    private static final String TAG = "HttpHelper";
    private static Pattern pattern = Pattern.compile("(^　*)|(　*$)");

    public static final int SCIENCE = 1;
    public static final int EDUCATION = 2;
    public static final int MILITARY = 3;
    public static final int NATIONAL = 4;
    public static final int SOCIETY = 5;
    public static final int CULTURE = 6;
    public static final int TRANSPORT = 7;
    public static final int INTERNATIONAL = 8;
    public static final int SPORTS = 9;
    public static final int COMMERCIAL = 10;
    public static final int HEALTH = 11;
    public static final int ENTERTAINMENT = 12;

    public static void askLatestNews(final int pageNo, final int pageSize, final  int category, final CallBack callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = rootURL+"latest?pageNo="+pageNo+"&pageSize="+pageSize;
                    if (category > 0 && category < 13)
                        url += "&category=" + category;
                    client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    callback.onFinishNewsList(parseJSONForNewsList(responseData, callback));
                }catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e);
                }
            }
        }).start();
    }

    public static void askLatestNews(final int pageNo, final int category, final CallBack callback) {
        askLatestNews(pageNo, 20, category, callback);
    }

    public static void askLatestNews(final int pageNo, final CallBack callback) {
        askLatestNews(pageNo, 0, callback);
    }

    public static void askKeywordNews(final String keyword, final int category, final int pageNo, final int pageSize, final CallBack callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = rootURL+"search?keyword="+keyword+"&pageNo="+pageNo+"&pageSize="+pageSize;
                    if (category > 0 && category < 13)
                        url += "&category=" + category;
                    client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    callback.onFinishNewsList(parseJSONForNewsList(responseData, callback));
                }catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e);
                }
            }
        }).start();
    }

    public static void askKeywordNews(final String keyword, final int category, final int pageNo, final CallBack callback) {
        askKeywordNews(keyword, category, pageNo, 20, callback);
    }

    public static void askKeywordNews(final String keyword, final int pageNo, final CallBack callback) {
        askKeywordNews(keyword, 0, pageNo, 20, callback);
    }

    public static void askDetailNews(final Context context, final News news, final CallBack callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = rootURL+"detail?newsId="+news.getUniqueId();
                    client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONForSingleNews(responseData, news, callback);
                    //printNews(news);
                    //ArrayList<Bitmap> bitmaps = new ArrayList<>();
                    downloadImage(context, news, callback);
                    callback.onFinishDetail();
                }catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e);
                }
            }
        }).start();

    }

    private static void parseJSONForSingleNews(final String jsonData, final News news, final CallBack callback) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            news.setJournal(jsonObject.getString("news_Journal"));
            news.setContent(jsonObject.getString("news_Content"));
            //Keywords
            JSONArray jsonArray = jsonObject.getJSONArray("Keywords");
            ArrayList<Keyword> keywordList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonKeywordObject = jsonArray.getJSONObject(i);
                Keyword keyword = new Keyword();
                keyword.setNews(news);
                keyword.setWord(jsonKeywordObject.getString("word"));
                keyword.setScore(jsonKeywordObject.getDouble("score"));
                keywordList.add(keyword);
            }
            news.setKeywords(keywordList);
            //Entries
            ArrayList<String> entries = new ArrayList<>();
            jsonArray = jsonObject.getJSONArray("locations");
            for (int i = 0; i < jsonArray.length(); ++i)
                entries.add(jsonArray.getJSONObject(i).getString("word"));
            jsonArray = jsonObject.getJSONArray("persons");
            for (int i = 0; i < jsonArray.length(); ++i)
                entries.add(jsonArray.getJSONObject(i).getString("word"));
            news.setEntries(entries);
        }catch (Exception e) {
            e.printStackTrace();
            callback.onError(e);
        }
    }

    private static ArrayList<News> parseJSONForNewsList(String jsonData, final CallBack callback) {
        ArrayList<News> newsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(jsonData).getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                News news = new News();
                news.setClassTag(jsonObject.getString("newsClassTag"));
                news.setAuthor(jsonObject.getString("news_Author"));
                news.setUniqueId(jsonObject.getString("news_ID"));
                news.setPictures(new ArrayList<String>(Arrays.asList(jsonObject.getString("news_Pictures").split("//s|;"))));
                news.setSource(jsonObject.getString("news_Source"));
                news.setTime(new SimpleDateFormat("yyyyMMdd", Locale.CHINA).parse(jsonObject.getString("news_Time").substring(0, 8)));
                news.setTitle(jsonObject.getString("news_Title"));
                news.setUrl(jsonObject.getString("news_URL"));
                String intro = jsonObject.getString("news_Intro");
                intro = "　　" + pattern.matcher(intro).replaceAll("");
                news.setIntro(intro);
                newsList.add(news);
            }
        }catch (Exception e) {
            e.printStackTrace();
            callback.onError(e);
        }
        return newsList;
    }

    private static void downloadImage(final Context context, final News news, final CallBack callBack) {
        //final Vector<Bitmap> bitmaps = new Vector<Bitmap>();
        Vector<Thread> threads = new Vector<Thread>();
        final Vector<String> pictureVector = new Vector<String>();
        for (final String s : news.getPictures()) {
            if (!s.contains("."))
                continue;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap = Glide.with(context)
                                .load(s)
                                .asBitmap()
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get();
                        if (bitmap != null) {
                            saveImageToDevice(context, s, bitmap, callBack);
                            pictureVector.add(s);
                        }
                    }catch (ExecutionException e) {
                        Log.e(TAG, "run: "+s);
                        e.printStackTrace();
                    }catch (Exception e) {
                        e.printStackTrace();
                        callBack.onError(e);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            }catch (Exception e) {
                e.printStackTrace();
                callBack.onError(e);
            }
        }
        news.setPictures(new ArrayList<String>(pictureVector));
        //return bitmaps;
    }

    private synchronized static void saveImageToDevice(final Context context, final String string, final Bitmap bitmap, final CallBack callBack) {
        // 首先保存图片
        File file = Environment.getDataDirectory().getAbsoluteFile();
        file = new File(file, "/data/com.java.group19/");
        String fileName = "newsPicture";
        File appDir = new File(file ,fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        fileName = string.replaceAll("[^a-zA-Z0-9.]", "");
        File currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                callBack.onError(e);
            }
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    currentFile.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            callBack.onError(e);
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
    }

    public static void printNews(News news) {
        String output = "\nnewsClassTag: "+news.getClassTag()+"\nnews_Author: "+news.getAuthor()+"\nnews_ID: "+news.getUniqueId() + "\nnews_Pictures: ";
        if (!news.getPictures().isEmpty())
            for (String s : news.getPictures())
                output += "\n   "+s;
        output += "\nnews_Source: "+news.getSource();
        output += "\nnews_Time: "+news.getTime().toString();
        output += "\nnews_Title: "+news.getTitle();
        output += "\nnews_URL: "+news.getUrl();
        output += "\nnews_Intro: "+news.getIntro()+"\n";
        output += "news_journal: "+news.getJournal() + "\nnews_content: "+news.getContent() + "\nKeywords: ";
        /*if (!news.getKeywords().isEmpty())
            for (String s : news.getKeywords().keySet())
                output += "\n   word: " + s + ", score: " + news.getKeywords().get(s);*/
        output += "\nnews_Entries:\n";
        if (!news.getEntries().isEmpty())
            for (String s : news.getEntries())
                output += s + "\n";
        Log.d(TAG, output);
    }
}
