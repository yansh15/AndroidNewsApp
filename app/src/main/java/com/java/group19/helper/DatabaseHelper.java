package com.java.group19.helper;

import com.java.group19.data.News;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by strongoier on 17/9/9.
 */

public class DatabaseHelper {

    public static synchronized News getNews(String uniqueId) {
        return complete(DataSupport.where("uniqueid = ?", uniqueId).findFirst(News.class));
    }

    public static synchronized List<News> getAllNews() {
        return completeList(DataSupport.findAll(News.class));
    }

    public static synchronized void removeNews(String uniqueId) {
        DataSupport.deleteAll(News.class, "uniqueid = ?", uniqueId);
    }

    public static synchronized void saveNews(News news) {
        news.save();
    }

    public static synchronized List<News> getLatestAllVisits() {
        return completeList(DataSupport.order("lastvisittime desc").find(News.class));
    }

    public static synchronized List<News> getLatestAllFavorites() {
        return completeList(DataSupport.where("lastfavoritetime > ?", "0").order("lastfavoritetime desc").find(News.class));
    }

    private static List<News> completeList(List<News> newsList) {
        ArrayList<News> result = new ArrayList<>();
        if (newsList == null) {
            return result;
        }
        for (News news : newsList) {
            result.add(complete(news));
        }
        return result;
    }

    private static News complete(News news) {
        if (news == null) {
            return null;
        }
        if (news.getPictures() == null) {
            news.setPictures(new ArrayList<String>());
        }
        if (news.getWords() == null) {
            news.setWords(new ArrayList<String>());
        }
        if (news.getScores() == null) {
            news.setScores(new ArrayList<Double>());
        }
        if (news.getEntries() == null) {
            news.setEntries(new ArrayList<String>());
        }
        if (news.getPictures() == null) {
            news.setPictures(new ArrayList<String>());
        }
        if (news.getTime() == null) {
            news.setTime(new Date(0));
        }
        if (news.getLastFavoriteTime() == null) {
            news.setLastFavoriteTime(new Date(0));
        }
        if (news.getLastVisitTime() == null) {
            news.setLastVisitTime(new Date(0));
        }
        return news;
    }
}
