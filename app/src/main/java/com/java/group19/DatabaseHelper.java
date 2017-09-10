package com.java.group19;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Created by strongoier on 17/9/9.
 */

public class DatabaseHelper {

    private static Config config;

    public static void init() {
        Connector.getDatabase();
        // check whether config exists
        config = DataSupport.findFirst(Config.class);
        if (config == null) {
            config = new Config();
            config.save();
        }
    }

    public static boolean isVisited(String uniqueId) {
        return getNews(uniqueId) == null;
    }

    public static News getNews(String uniqueId) {
        return DataSupport.where("uniqueid = ", uniqueId).findFirst(News.class);
    }

    public static void saveNews(News news) {
        news.save();
    }

    public static boolean isDarkTheme() {
        return config.isDarkTheme();
    }

    public static void setDarkTheme(boolean darkTheme) {
        config.setDarkTheme(darkTheme);
        config.save();
    }

    public static boolean isTextMode() {
        return config.isTextMode();
    }

    public static void setTextMode(boolean textMode) {
        config.setTextMode(textMode);
        config.save();
    }

    public static void addSearchRecord(String record) {
        config.getSearchRecords().add(record);
        config.save();
    }

    public static Vector<String> getLatestSearchRecords(String prefix, int count) {
        Vector<String> records = config.getSearchRecords();
        Vector<String> result = new Vector<>();
        for (int i = 0, j = 0; j < count && i < records.size(); ++i) {
            String record = records.elementAt(records.size() - 1 - i);
            if (record.startsWith(prefix)) {
                result.add(record);
                ++j;
            }
        }
        return result;
    }

    public static boolean addForbiddenWord(String word) {
        if (config.getForbiddenWords().contains(word)) {
            return false;
        } else {
            config.getForbiddenWords().add(word);
            config.save();
            return true;
        }
    }

    public static void removeForbiddenWord(String word) {
        config.getForbiddenWords().remove(word);
        config.save();
    }

    public static Vector<String> getAllForbiddenWords() {
        Vector<String> result = new Vector<>(DataSupport.findFirst(Config.class).getForbiddenWords());
        Collections.reverse(result);
        return result;
    }

    public static Vector<News> getLatestVisits(int count) {
        return new Vector<>(DataSupport.order("lastvisittime desc").limit(count).find(News.class));
    }

    public static Vector<News> getLatestFavorites(int count) {
        return new Vector<>(DataSupport.where("lastfavoritetime > ?", "0").order("lastfavoritetime desc").limit(count).find(News.class));
    }
}
