package com.java.group19.helper;

import com.java.group19.data.Config;
import com.java.group19.data.News;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by strongoier on 17/9/9.
 */

public class DatabaseHelper {

    private static Config config;

    public static synchronized void init() {
        Connector.getDatabase();
        // check whether config exists
        config = DataSupport.findFirst(Config.class);
        if (config == null) {
            config = new Config();
            config.setForbiddenWords(new ArrayList<String>());
            config.setSearchRecords(new ArrayList<String>());
            config.save();
        }
    }

    public static synchronized News getNews(String uniqueId) {
        return DataSupport.where("uniqueid = ?", uniqueId).findFirst(News.class);
    }

    public static synchronized List<News> getAllNews() {
        return new ArrayList<>(DataSupport.findAll(News.class));
    }

    public static synchronized void removeNews(String uniqueId) {
        DataSupport.deleteAll(News.class, "uniqueid = ?", uniqueId);
    }

    public static synchronized void saveNews(News news) {
        news.save();
    }

    public static synchronized boolean isDarkTheme() {
        return config.isDarkTheme();
    }

    public static synchronized void setDarkTheme(boolean darkTheme) {
        config.setDarkTheme(darkTheme);
        config.save();
    }

    public static synchronized boolean isTextMode() {
        return config.isTextMode();
    }

    public static synchronized void setTextMode(boolean textMode) {
        config.setTextMode(textMode);
        config.save();
    }

    public static synchronized void addSearchRecord(String record) {
        config.getSearchRecords().add(record);
        config.save();
    }

    public static synchronized List<String> getLatestSearchRecords(String prefix, int count) {
        List<String> records = config.getSearchRecords();
        List<String> result = new ArrayList<>();
        for (int i = 0, j = 0; j < count && i < records.size(); ++i) {
            String record = records.get(records.size() - 1 - i);
            if (record.startsWith(prefix)) {
                result.add(record);
                ++j;
            }
        }
        return result;
    }

    public static synchronized boolean addForbiddenWord(String word) {
        if (config.getForbiddenWords().contains(word)) {
            return false;
        } else {
            config.getForbiddenWords().add(word);
            config.save();
            return true;
        }
    }

    public static synchronized void removeForbiddenWord(String word) {
        config.getForbiddenWords().remove(word);
        config.save();
    }

    public static synchronized List<String> getAllForbiddenWords() {
        List<String> result = new ArrayList<>(DataSupport.findFirst(Config.class).getForbiddenWords());
        Collections.reverse(result);
        return result;
    }

    public static synchronized List<News> getLatestAllVisits() {
        return new ArrayList<>(DataSupport.order("lastvisittime desc").find(News.class));
    }

    public static synchronized List<News> getLatestVisits(int count) {
        return new ArrayList<>(DataSupport.order("lastvisittime desc").limit(count).find(News.class));
    }

    public static synchronized List<News> getLatestAllFavorites() {
        return new ArrayList<>(DataSupport.where("lastfavoritetime > ?", "0").order("lastfavoritetime desc").find(News.class));
    }

    public static synchronized List<News> getLatestFavorites(int count) {
        return new ArrayList<>(DataSupport.where("lastfavoritetime > ?", "0").order("lastfavoritetime desc").limit(count).find(News.class));
    }
}
