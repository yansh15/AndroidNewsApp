package com.java.group19.helper;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
        return this.score < newsWithScore.score ? 1 : (this.score > newsWithScore.score ? 1 : 0);
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
    private static final int STORAGESIZE = 100;
    private static final int KEYWORDMAXIMUMSIZE = 10;

    private static OkHttpClient client;
    private static String rootURL = "http://166.111.68.66:2042/news/action/query/";
    private static final String TAG = "HttpHelper";
    private static Pattern pattern = Pattern.compile("(^　*)|(　*$)");
    private static HashMap<String, Double> scoreMap = new HashMap<>();
    private static ArrayList<News> newsList;
    private static String url;
    private static List<String> forbiddenWordList;
    private static int unreadNewsCount;
    private static HashSet<String> newsIDSet;
    private static int pageStartNo;
    private static int searchPageSize = 10;

    private static ArrayList subList(ArrayList arrayList, int l, int r) {
        ArrayList newsArrayList = new ArrayList<>();
        for (int i = l; i < r; ++i)
            newsArrayList.add(arrayList.get(i));
        return newsArrayList;
    }

    private static String checkStringCharacter(String content) {
        String s = "";
        for (char c : content.toCharArray()) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                    || ub == Character.UnicodeBlock.VERTICAL_FORMS
                    || c == ' ' || c == '　')
                s += c;
        }
        s.replaceAll(" 　　", "　　");
        return s;
    }

    private static String connectNetworkFromURL() throws Exception{
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();
        return responseData;
    }

    private static void askBestRecommendation(final int pageSize) {
        scoreMap = new HashMap<>();
        ArrayList<NewsWithScore> newsWithScoreArrayList = new ArrayList<>();
        for (News news : newsList)
            if (news.getVisitCount() > 0){
                for (Keyword keyword : news.getKeywords())
                    if (scoreMap.containsKey(keyword.getWord()))
                        scoreMap.put(keyword.getWord(), scoreMap.get(keyword.getWord()) + keyword.getScore() * news.getVisitCount());
                    else
                        scoreMap.put(keyword.getWord(), keyword.getScore() * news.getVisitCount());
            }
            else
                newsWithScoreArrayList.add(new NewsWithScore(news));
        for (String word : forbiddenWordList)
            scoreMap.put(word, -oo);
        for (NewsWithScore newsWithScore : newsWithScoreArrayList) {
            double score = 0;
            for (Keyword keyword : newsWithScore.getNews().getKeywords()){
                if (scoreMap.containsKey(keyword.getWord()))
                    score += scoreMap.get(keyword.getWord()) * keyword.getScore();
            }
            newsWithScore.setScore(score);
        }
        Collections.sort(newsWithScoreArrayList);
        newsList = new ArrayList<>();
        for (NewsWithScore newsWithScore : (ArrayList<NewsWithScore>)subList(newsWithScoreArrayList, 0, newsWithScoreArrayList.size())){
            newsList.add(newsWithScore.getNews());
            DatabaseHelper.removeNews(newsWithScore.getNews().getUniqueId());
        }
    }

    private static ArrayList<News> parseJSONForNewsList(String jsonData, final OnGetNewsListener listener) throws Exception{
        ArrayList<News> thisNewsList = new ArrayList<>();
        JSONArray jsonArray = new JSONObject(jsonData).getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            News news = new News();
            String id = jsonObject.getString("news_ID");
            if (newsIDSet.contains(id))
                continue;
            else
                newsIDSet.add(id);
            if (DatabaseHelper.getNews(id) != null)
                continue;
            url = rootURL+"detail?newsId="+id;
            if (!parseJSONForSingleNews(connectNetworkFromURL(), news, listener))
                continue;
            news.setUniqueId(id);
            news.setClassTag(jsonObject.getString("newsClassTag"));
            news.setAuthor(jsonObject.getString("news_Author"));
            news.setPictures(new ArrayList<>(Arrays.asList(jsonObject.getString("news_Pictures").split("//s|;"))));
            news.setSource(jsonObject.getString("news_Source"));
            news.setTime(new SimpleDateFormat("yyyyMMdd", Locale.CHINA).parse(jsonObject.getString("news_Time").substring(0, 8)));
            news.setTitle(jsonObject.getString("news_Title"));
            news.setUrl(jsonObject.getString("news_URL"));
            String intro = jsonObject.getString("news_Intro");
            intro = "　　" + pattern.matcher(intro).replaceAll("");
            news.setIntro(intro);
            thisNewsList.add(news);
            if (DatabaseHelper.getNews(news.getUniqueId()) != null)
                DatabaseHelper.saveNews(news);
            unreadNewsCount ++;
        }
        return thisNewsList;
    }

    private static boolean getKeyword(News news, JSONObject jsonObject) throws Exception{
        JSONArray jsonArray = jsonObject.getJSONArray("Keywords");
        ArrayList<Keyword> keywordList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonKeywordObject = jsonArray.getJSONObject(i);
            Keyword keyword = new Keyword();
            keyword.setNews(news);
            keyword.setWord(jsonKeywordObject.getString("word"));
            if (forbiddenWordList.contains(keyword.getWord()))
                return false;
            keyword.setScore(jsonKeywordObject.getDouble("score"));
            keywordList.add(keyword);
        }
        Collections.sort(keywordList);
        ArrayList<Keyword> keywords = new ArrayList<>();
        for (int i = 0; i < Math.min(KEYWORDMAXIMUMSIZE, keywordList.size()); ++i)
            keywords.add(keywordList.get(i));
        news.setKeywords(keywords);
        return true;
    }

    private static void searchWithKeyword(final String keyword, final int category, final int pageSize, final OnGetNewsListener listener) {
        try {
            while (pageStartNo <= searchPageSize && newsList.size() < pageSize) {
                url = rootURL + "search?keyword=" + keyword + "&pageNo=" + pageStartNo + "&pageSize=" + searchPageSize;
                if (category > 0 && category < 13)
                    url += "&category=" + category;
                ++pageStartNo;
                String responseData = connectNetworkFromURL();
                newsList.addAll(parseJSONForNewsList(responseData,listener));
            }
            listener.onFinish((ArrayList<News>)subList(newsList, 0, pageSize));
            newsList = (ArrayList<News>) subList(newsList, pageSize, newsList.size());
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

    public static void askLatestNews(final int pageSize, final int category, final OnGetNewsListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                forbiddenWordList = DatabaseHelper.getAllForbiddenWords();
                if (category <= 0 || category > 12) {
                    askLatestNews(pageSize, listener);
                    return;
                }
                newsList = new ArrayList<News>();
                newsIDSet = new HashSet<String>();
                try {
                    for (int i = 1; i <= (pageSize << 1) && newsList.size() < pageSize; ++i) {
                        String url = rootURL + "latest?pageNo="+i+"&pageSize=" + (pageSize << 1) + "&category=" + category;
                        newsList.addAll(parseJSONForNewsList(connectNetworkFromURL(), listener));
                    }
                    listener.onFinish((ArrayList<News>)subList(newsList, 0, pageSize));
                }catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                }
            }
        }).start();
    }

    public static void askLatestNews(final int pageSize, final OnGetNewsListener listener) {
        try {
            newsList = (ArrayList<News>) DatabaseHelper.getAllNews();
            unreadNewsCount = 0;
            newsIDSet = new HashSet<String>();
            for (News news : newsList) {
                if (news.getVisitCount() == 0)
                    unreadNewsCount++;
                newsIDSet.add(news.getUniqueId());
            }
            for (int i = 1; i <= (pageSize << 1) && unreadNewsCount - pageSize < STORAGESIZE; ++i) {
                url = rootURL + "latest?pageNo="+i+"&pageSize="+(pageSize<<1);
                newsList.addAll(parseJSONForNewsList(connectNetworkFromURL(), listener));
            }
            askBestRecommendation(pageSize);
            listener.onFinish(newsList);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(e);
        }
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
                newsList = new ArrayList<News>();
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
        if (!news.getKeywords().isEmpty())
            for (Keyword keyword : news.getKeywords())
                output += "\n   word: " + keyword.getWord() + ", score: " + keyword.getScore();
        output += "\nnews_Entries:\n";
        if (!news.getEntries().isEmpty())
            for (String s : news.getEntries())
                output += s + "\n";
        Log.d(TAG, output);
    }

}
