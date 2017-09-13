package com.java.group19.helper;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.java.group19.data.Keyword;
import com.java.group19.listener.OnGetDetailListener;
import com.java.group19.listener.OnGetNewsListener;
import com.java.group19.data.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class NewsWithScore implements Comparable<NewsWithScore>{
    private News news;
    private Double score;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    NewsWithScore(News news) {this.news = news;}

    @Override
    public int compareTo(@NonNull NewsWithScore newsWithScore) {
        return this.score < newsWithScore.score ? 1 : (this.score > newsWithScore.score ? -1 : 0);
    }
}

public class HttpHelper {

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

    private static final double oo = 1e100;
    private static final int STORAGESIZE = 1200;
    private static final int KEYWORDMAXIMUMSIZE = 10;

    private static final OkHttpClient client = new OkHttpClient();
    private static final String rootURL = "http://166.111.68.66:2042/news/action/query/";
    private static final String TAG = "HttpHelper";
    private static final Pattern pattern = Pattern.compile("(^　*)|(　*$)");
    private static ArrayList<News> searchNewsList;
    private static int pageStartNo;
    private static final int searchPageSize = 20;

    private static ArrayList subList(ArrayList arrayList, int l, int r) {
        ArrayList newsArrayList = new ArrayList<>();
        for (int i = l; i < r && i < arrayList.size(); ++i)
            newsArrayList.add(arrayList.get(i));
        return newsArrayList;
    }

    private static String checkStringCharacter(String content) {
        return content.replaceAll("\\s　　", "\n　　");
    }

    private static String connectNetworkFromURL(String url) throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static void askBestRecommendation(final int pageSize, ArrayList<News> newsList) {
        HashMap<String, Double> scoreMap = new HashMap<>();
        ArrayList<NewsWithScore> newsWithScoreArrayList = new ArrayList<>();
        for (News news : newsList)
            if (news.getVisitCount() > 0){
                for (int i = 0; i < news.getWords().size(); ++i) {
                    String keyword = news.getWords().get(i);
                    double score = news.getScores().get(i);
                    if (scoreMap.containsKey(keyword))
                        scoreMap.put(keyword, scoreMap.get(keyword) + score * news.getVisitCount());
                    else
                        scoreMap.put(keyword, score * news.getVisitCount());
                }
            }
            else
                newsWithScoreArrayList.add(new NewsWithScore(news));
        for (String word : SharedPreferencesHelper.getForbiddenWord())
            scoreMap.put(word, -oo);
        for (NewsWithScore newsWithScore : newsWithScoreArrayList) {
            double score = Math.random() / 1e100;
            for (int i = 0; i < newsWithScore.getNews().getWords().size(); ++i) {
                String keyword = newsWithScore.getNews().getWords().get(i);
                if (scoreMap.containsKey(keyword))
                    score += scoreMap.get(keyword) * newsWithScore.getNews().getScores().get(i);
            }
            newsWithScore.setScore(score);
        }
        Collections.sort(newsWithScoreArrayList);
        newsList = new ArrayList<>();
        for (NewsWithScore newsWithScore : (ArrayList<NewsWithScore>)subList(newsWithScoreArrayList, 0, Math.min(pageSize, newsWithScoreArrayList.size()))){
            Log.d(TAG, "askBestRecommendation: " + newsWithScore.getScore());
            newsList.add(newsWithScore.getNews());
            DatabaseHelper.removeNews(newsWithScore.getNews().getUniqueId());
        }
    }

    private static ArrayList<News> parseJSONForNewsList(String jsonData, final OnGetNewsListener listener, HashSet<String> newsIDSet, int unreadNewsCount) throws Exception{
        ArrayList<News> thisNewsList = new ArrayList<>();
        JSONArray jsonArray = new JSONObject(jsonData).getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                News news = new News();
                String id = jsonObject.getString("news_ID");
                if (newsIDSet.contains(id))
                    continue;
                if (DatabaseHelper.getNews(id) != null)
                    continue;
                String url = rootURL+"detail?newsId="+id;
                if (!parseJSONForSingleNews(connectNetworkFromURL(url), news, listener))
                    continue;
                news.setUniqueId(id);
                news.setClassTag(jsonObject.getString("newsClassTag"));
                news.setAuthor(jsonObject.getString("news_Author"));
                news.setPictures(new ArrayList<>(Arrays.asList(jsonObject.getString("news_Pictures").split("\\s|;"))));
                ArrayList<String> pictureList = new ArrayList<>();
                for (String s : news.getPictures()){
                    if (s.contains("."))
                        pictureList.add(s);
                }
                news.setPictures(pictureList);
                news.setSource(jsonObject.getString("news_Source"));
                news.setTime(new SimpleDateFormat("yyyyMMdd", Locale.CHINA).parse(jsonObject.getString("news_Time").substring(0, 8)));
                news.setTitle(jsonObject.getString("news_Title"));
                news.setUrl(jsonObject.getString("news_URL"));
                String intro = jsonObject.getString("news_Intro");
                intro = "　　" + pattern.matcher(intro).replaceAll("");
                news.setLastFavoriteTime(new Date(0));
                news.setLastVisitTime(new Date(0));
                news.setIntro(intro);
                thisNewsList.add(news);
                newsIDSet.add(id);
                if (unreadNewsCount >= 0) {
                    DatabaseHelper.saveNews(news);
                    unreadNewsCount++;
                    Log.d(TAG, "parseJSONForNewsList: " + unreadNewsCount);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thisNewsList;
    }

    private static boolean getKeyword(News news, JSONObject jsonObject) throws Exception{
        JSONArray jsonArray = jsonObject.getJSONArray("Keywords");
        List<String> forbiddenWordList = SharedPreferencesHelper.getForbiddenWord();
        double totalScore = 0;
        ArrayList<Keyword> keywordList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonKeywordObject = jsonArray.getJSONObject(i);
            Keyword keyword = new Keyword();
            keyword.setWord(jsonKeywordObject.getString("word"));
            if (forbiddenWordList.contains(keyword.getWord()))
                return false;
            keyword.setScore(jsonKeywordObject.getDouble("score"));
            totalScore += jsonKeywordObject.getDouble("score");
            keywordList.add(keyword);
        }
        Collections.sort(keywordList);
        ArrayList<String> words = new ArrayList<>();
        ArrayList<Double> score = new ArrayList<>();
        for (int i = 0; i < Math.min(KEYWORDMAXIMUMSIZE, keywordList.size()); ++i) {
            words.add(keywordList.get(i).getWord());
            score.add(keywordList.get(i).getScore() / totalScore);
        }
        news.setWords(words);
        news.setScores(score);
        return true;
    }

    private static void searchWithKeyword(final String keyword, final int category, final int pageSize, final OnGetNewsListener listener) {
        try {
            while (pageStartNo <= searchPageSize && searchNewsList.size() < pageSize) {
                String url = rootURL + "search?keyword=" + keyword + "&pageNo=" + pageStartNo + "&pageSize=" + searchPageSize;
                if (category > 0 && category < 13)
                    url += "&category=" + category;
                ++pageStartNo;
                String responseData = connectNetworkFromURL(url);
                HashSet<String> newsIDSet = new HashSet<String>();
                for (News news : searchNewsList)
                    newsIDSet.add(news.getUniqueId());
                searchNewsList.addAll(parseJSONForNewsList(responseData, listener, newsIDSet, -1));
            }
            ArrayList<News> newsArrayList = (ArrayList<News>)subList(searchNewsList, 0, pageSize);
            searchNewsList = (ArrayList<News>) subList(searchNewsList, pageSize, searchNewsList.size());
            listener.onFinish(newsArrayList);
        }catch (Exception e) {
            e.printStackTrace();
            listener.onError(e);
        }
    }

    private static boolean parseJSONForSingleNews(final String jsonData, final News news, final OnGetNewsListener listener) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (!getKeyword(news, jsonObject))
                return false;
            news.setJournal(jsonObject.getString("news_Journal"));
            news.setContent(checkStringCharacter(jsonObject.getString("news_Content")));
            //Entries
            ArrayList<String> entries = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("locations");
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
        return true;
    }

    private static void askLatestNews(final int pageSize, final OnGetNewsListener listener) {
        try {
            ArrayList<News> newsList = (ArrayList<News>) DatabaseHelper.getAllNews();
            int unreadNewsCount = 0;
            HashSet<String> newsIDSet = new HashSet<String>();
            for (News news : newsList) {
                if (news.getVisitCount() == 0)
                    unreadNewsCount++;
                newsIDSet.add(news.getUniqueId());
            }
            for (int i = 1; i <= (pageSize << 1) && unreadNewsCount - pageSize < STORAGESIZE; ++i) {
                String url = rootURL + "latest?pageNo="+i+"&pageSize="+(pageSize<<1);
                newsList.addAll(parseJSONForNewsList(connectNetworkFromURL(url), listener, newsIDSet, unreadNewsCount));
            }
            askBestRecommendation(pageSize, newsList);
            listener.onFinish(newsList);
        } catch (Exception e) {
            Log.e(TAG, "askLatestNews: ");
            e.printStackTrace();
            listener.onError(e);
        }
    }

    public static void askLatestNews(final int pageSize, final int category, final OnGetNewsListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (category <= 0 || category > 12) {
                    askLatestNews(pageSize, listener);
                    return;
                }
                ArrayList<News> newsList = new ArrayList<News>();
                HashSet<String> newsIDSet = new HashSet<String>();
                try {
                    for (int i = 1; i <= (pageSize << 1) && newsList.size() < pageSize; ++i) {
                        String url = rootURL + "latest?pageNo="+i+"&pageSize=" + (pageSize << 1) + "&category=" + category;
                        newsList.addAll(parseJSONForNewsList(connectNetworkFromURL(url), listener, newsIDSet, -1));
                    }
                    listener.onFinish((ArrayList<News>)subList(newsList, 0, pageSize));
                }catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                }
            }
        }).start();
    }

    public static void askMoreKeywordNews(final String keyword, final int category, final int pageSize, final OnGetNewsListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchWithKeyword(keyword, category, pageSize, listener);
            }
        }).start();
    }

    public static void askMoreKeywordNews(final String keyword, final int category, final OnGetNewsListener listener) {
        askMoreKeywordNews(keyword, category, 10, listener);
    }

    public static void askMoreKeywordNews(final String keyword, final OnGetNewsListener listener) {
        askMoreKeywordNews(keyword, 0, listener);
    }

    public static void askKeywordNews(final String keyword, final int category, final int pageSize, final OnGetNewsListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchNewsList = new ArrayList<News>();
                pageStartNo = 1;
                searchWithKeyword(keyword, category, pageSize, listener);
            }
        }).start();
    }

    public static void askKeywordNews(final String keyword, final int category, final OnGetNewsListener listener) {
        askKeywordNews(keyword, category, 10, listener);
    }

    public static void askKeywordNews(final String keyword, final OnGetNewsListener listener) {
        askKeywordNews(keyword, 0, listener);
    }
}
