package com.java.group19;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Vector;

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
                    callback.onFinish(parseJSONForNewsVector(responseData, callback));
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
                    callback.onFinish(parseJSONForNewsVector(responseData, callback));
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

    public static void askDetailNews(final News news, final CallBack callback) {
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
                    callback.onFinish();
                }catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e);
                }
            }
        }).start();

    }

    private static void parseJSONForSingleNews(String jsonData, News news, final CallBack callback) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            news.setJournal(jsonObject.getString("news_Journal"));
            news.setContent(jsonObject.getString("news_Content"));
            //Keywords
            JSONArray jsonArray = jsonObject.getJSONArray("Keywords");
            Vector<Keyword> keywordVector = new Vector<Keyword>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonKeywordObject = jsonArray.getJSONObject(i);
                Keyword keyword = new Keyword();
                keyword.setNews(news);
                keyword.setWord(jsonKeywordObject.getString("word"));
                keyword.setScore(jsonKeywordObject.getDouble("score"));
                keywordVector.add(keyword);
            }
            news.setKeywords(keywordVector);
            //Entries
            Vector<String> entries = new Vector<String>();
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

    private static Vector<News> parseJSONForNewsVector(String jsonData, final CallBack callback) {
        Vector<News> newsVector = new Vector<News>();
        try {
            JSONArray jsonArray = new JSONObject(jsonData).getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                News news = new News();
                news.setClassTag(jsonObject.getString("newsClassTag"));
                news.setAuthor(jsonObject.getString("news_Author"));
                news.setUniqueId(jsonObject.getString("news_ID"));
                news.setPictures(new Vector<String>(Arrays.asList(jsonObject.getString("news_Pictures").split(";"))));
                news.setSource(jsonObject.getString("news_Source"));
                news.setTime(new SimpleDateFormat("yyyyMMdd", Locale.CHINA).parse(jsonObject.getString("news_Time")));
                news.setTitle(jsonObject.getString("news_Title"));
                news.setUrl(jsonObject.getString("news_URL"));
                news.setIntro(jsonObject.getString("news_Intro"));
                newsVector.add(news);
            }
        }catch (Exception e) {
            e.printStackTrace();
            callback.onError(e);
        }
        return newsVector;
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
