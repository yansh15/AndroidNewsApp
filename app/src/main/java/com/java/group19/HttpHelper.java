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
import com.java.group19.Listener.OnGetDetailListener;
import com.java.group19.Listener.OnGetImagesListener;
import com.java.group19.Listener.OnGetNewsListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHelper {
    private static OkHttpClient client;
    private static String rootURL = "http://166.111.68.66:2042/news/action/query/";
    private static final String TAG = "HttpHelper";
    private static Pattern pattern = Pattern.compile("(^　*)|(　*$)");

    public static final int ALL = 0;
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

    public static void askLatestNews(final int pageNo, final int pageSize, final  int category, final OnGetNewsListener listener) {
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
                    listener.onFinish(parseJSONForNewsList(responseData, listener));
                }catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                }
            }
        }).start();
    }

    public static void askLatestNews(final int pageNo, final int category, final OnGetNewsListener listener) {
        askLatestNews(pageNo, 20, category, listener);
    }

    public static void askLatestNews(final int pageNo, final OnGetNewsListener listener) {
        askLatestNews(pageNo, 0, listener);
    }

    public static void askKeywordNews(final String keyword, final int category, final int pageNo, final int pageSize, final OnGetNewsListener listener) {
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
                    listener.onFinish(parseJSONForNewsList(responseData, listener));
                }catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                }
            }
        }).start();
    }

    public static void askKeywordNews(final String keyword, final int category, final int pageNo, final OnGetNewsListener listener) {
        askKeywordNews(keyword, category, pageNo, 20, listener);
    }

    public static void askKeywordNews(final String keyword, final int pageNo, final OnGetNewsListener listener) {
        askKeywordNews(keyword, 0, pageNo, 20, listener);
    }

    public static void askDetailNews(final News news, final OnGetDetailListener listener) {
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
                    parseJSONForSingleNews(responseData, news, listener);
                    listener.onFinish();
                }catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                }
            }
        }).start();

    }

    public static void downloadImage(final Context context, final List<String> urls, final OnGetImagesListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> results = new ArrayList<String>();
                for (String s : urls) {
                    if (!s.contains("."))
                        continue;
                    try {
                        Bitmap bitmap = Glide.with(context).load(s).asBitmap()
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        if (bitmap != null) {
                            saveImageToDevice(context, s, bitmap, listener);
                            results.add(s);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onError(e);
                    }
                }
                listener.onFinish(results);
            }
        }).start();
    }

    private static void parseJSONForSingleNews(final String jsonData, final News news, final OnGetDetailListener listener) {
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
            listener.onError(e);
        }
    }

    private static ArrayList<News> parseJSONForNewsList(String jsonData, final OnGetNewsListener listener) {
        ArrayList<News> newsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(jsonData).getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                News news = new News();
                news.setClassTag(jsonObject.getString("newsClassTag"));
                news.setAuthor(jsonObject.getString("news_Author"));
                news.setUniqueId(jsonObject.getString("news_ID"));
                news.setPictures(new ArrayList<>(Arrays.asList(jsonObject.getString("news_Pictures").split("//s|;"))));
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
            listener.onError(e);
        }
        return newsList;
    }

    private synchronized static void saveImageToDevice(final Context context, final String string, final Bitmap bitmap, final OnGetImagesListener listener) {
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
            listener.onError(e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                listener.onError(e);
            }
        }

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    currentFile.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            listener.onError(e);
        }

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
