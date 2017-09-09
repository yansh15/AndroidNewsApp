package com.java.group19;

import java.util.Random;
import java.util.Vector;

/**
 * Created by strongoier on 17/9/9.
 */

public class DatabaseHelper {
    public static void saveNews(News news) {
        news.save();
    }

    public static boolean isDarkTheme() {
        return false;
    }

    public static boolean isTextMode() {
        return false;
    }

    public static boolean isVisited(String uniqueId) {
        return false;
    }

    public static Vector<String> getLatestSearchRecords(int count) {
        Vector<String> records = new Vector<>();
        records.add("Hello yansh");
        records.add("mo yansh");
        return records;
    }

    public static Vector<String> getLatestSearchRecords(String prefix, int count) {
        Vector<String> records = new Vector<>();
        records.add("" + new Random().nextInt());
        records.add("" + new Random().nextGaussian());
        return records;
    }

    public static void addForbiddenWord(String word) {

    }

    public static Vector<String> getAllForbiddenWords() {
        Vector<String> records = new Vector<>();
        records.add("北京");
        return records;
    }

    public static Vector<News> getLatestVisits(int count) {
        Vector<News> visits = new Vector<>();
        return visits;
    }
}
