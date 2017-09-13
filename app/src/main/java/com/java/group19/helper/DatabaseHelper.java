package com.java.group19.helper;

import com.java.group19.data.News;
import com.java.group19.data.NewsInDatabase;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by strongoier on 17/9/9.
 */

public class DatabaseHelper {

    public static synchronized News getNews(String uniqueId) {
        return complete(DataSupport.where("uniqueid = ?", uniqueId).findFirst(NewsInDatabase.class));
    }

    public static synchronized List<News> getAllNews() {
        return completeList(DataSupport.findAll(NewsInDatabase.class));
    }

    public static synchronized void removeNews(String uniqueId) {
        DataSupport.deleteAll(NewsInDatabase.class, "uniqueid = ?", uniqueId);
    }

    public static synchronized void setNewsVisitCount(String uniqueId, int visitCount) {
        NewsInDatabase newsInDatabase = DataSupport.where("uniqueid = ?", uniqueId).findFirst(NewsInDatabase.class);
        newsInDatabase.setVisitCount(visitCount);
        newsInDatabase.save();
    }

    public static synchronized void setNewsLastFavoriteTime(String uniqueId, Date lastFavoriteTime) {
        NewsInDatabase newsInDatabase = DataSupport.where("uniqueid = ?", uniqueId).findFirst(NewsInDatabase.class);
        newsInDatabase.setLastFavoriteTime(lastFavoriteTime);
        newsInDatabase.save();
    }

    public static synchronized void setNewsLastVisitTime(String uniqueId, Date lastVisitTime) {
        NewsInDatabase newsInDatabase = DataSupport.where("uniqueid = ?", uniqueId).findFirst(NewsInDatabase.class);
        newsInDatabase.setLastVisitTime(lastVisitTime);
        newsInDatabase.save();
    }

    public static synchronized void saveNews(News news) {
        NewsInDatabase newsInDatabase = new NewsInDatabase();
        newsInDatabase.setUniqueId(news.getUniqueId());
        newsInDatabase.setClassTag(news.getClassTag());
        newsInDatabase.setAuthor(news.getAuthor());
        newsInDatabase.setPictures(arrayListToString(news.getPictures()));
        newsInDatabase.setSource(news.getSource());
        newsInDatabase.setTime(news.getTime());
        newsInDatabase.setTitle(news.getTitle());
        newsInDatabase.setUrl(news.getUrl());
        newsInDatabase.setIntro(news.getIntro());
        newsInDatabase.setJournal(news.getJournal());
        newsInDatabase.setContent(news.getContent());
        newsInDatabase.setWords(arrayListToString(news.getWords()));
        newsInDatabase.setScores(arrayListToString(news.getScores()));
        newsInDatabase.setEntries(arrayListToString(news.getEntries()));
        newsInDatabase.setLastFavoriteTime(news.getLastFavoriteTime());
        newsInDatabase.setLastVisitTime(news.getLastVisitTime());
        newsInDatabase.setVisitCount(news.getVisitCount());
        newsInDatabase.save();
    }

    public static synchronized List<News> getLatestAllVisits() {
        return completeList(DataSupport.order("lastvisittime desc").find(NewsInDatabase.class));
    }

    public static synchronized List<News> getLatestAllFavorites() {
        return completeList(DataSupport.where("lastfavoritetime > ?", "0").order("lastfavoritetime desc").find(NewsInDatabase.class));
    }

    private static String arrayListToString(ArrayList list) {
        if (list.size() == 0) {
            return "";
        }
        String result = "" + list.get(0);
        for (int i = 1; i < list.size(); ++i) {
            result = result + list.get(i);
        }
        return result;
    }

    private static List<News> completeList(List<NewsInDatabase> newsList) {
        ArrayList<News> result = new ArrayList<>();
        if (newsList == null) {
            return result;
        }
        for (NewsInDatabase newsInDatabase : newsList) {
            result.add(complete(newsInDatabase));
        }
        return result;
    }

    private static News complete(NewsInDatabase newsInDatabase) {
        if (newsInDatabase == null) {
            return null;
        }
        News news = new News();
        news.setUniqueId(newsInDatabase.getUniqueId());
        news.setClassTag(newsInDatabase.getClassTag());
        news.setAuthor(newsInDatabase.getAuthor());
        news.setPictures(newsInDatabase.getPictures().equals("") ? new ArrayList<String>() :
                new ArrayList<String>(Arrays.asList(newsInDatabase.getPictures().split(" "))));
        news.setSource(newsInDatabase.getSource());
        news.setTime(newsInDatabase.getTime() == null ? new Date(0) : newsInDatabase.getTime());
        news.setTitle(newsInDatabase.getTitle());
        news.setUrl(newsInDatabase.getUrl());
        news.setIntro(newsInDatabase.getIntro());
        news.setJournal(newsInDatabase.getJournal());
        news.setContent(newsInDatabase.getContent());
        news.setWords(newsInDatabase.getWords().equals("") ? new ArrayList<String>() :
                new ArrayList<String>(Arrays.asList(newsInDatabase.getWords().split(" "))));
        String[] scoresStr = newsInDatabase.getScores().split(" ");
        ArrayList<Double> scores = new ArrayList<>();
        for (String scoreStr : scoresStr) {
            scores.add(Double.parseDouble(scoreStr));
        }
        news.setScores(scores);
        news.setEntries(newsInDatabase.getEntries().equals("") ? new ArrayList<String>() :
                new ArrayList<String>(Arrays.asList(newsInDatabase.getEntries().split(" "))));
        news.setLastFavoriteTime(newsInDatabase.getLastFavoriteTime() == null ? new Date(0) : newsInDatabase.getLastFavoriteTime());
        news.setLastVisitTime(newsInDatabase.getLastVisitTime() == null ? new Date(0) : newsInDatabase.getLastVisitTime());
        news.setVisitCount(newsInDatabase.getVisitCount());
        return news;
    }
}
