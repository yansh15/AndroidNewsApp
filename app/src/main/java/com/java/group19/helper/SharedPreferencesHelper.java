package com.java.group19.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.java.group19.data.SearchRecordSuggestion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SharedPreferencesHelper {

    private static final String TAG = "SharedPreferencesHelper";

    private static class SearchRecord {
        String string;
        long date;

        SearchRecord(String s, long d) {
            string = s;
            date = d;
        }
    }

    private static SharedPreferences sharedPreferences;
    private static final String CATEGORY_LIST = "CATEGORY_LIST";
    private static final String TEXT_MODE = "TEXT_MODE";
    private static final String NIGHT_MODE = "NIGHT_MODE";
    private static final String SEARCH_RECORD = "SEARCH_RECORD";
    private static final String SEARCH_RECORD_STRING = "SEARCH_RECORD_STRING";
    private static final String SEARCH_RECORD_DATE = "SEARCH_RECORD_DATE";
    private static final String FORBIDDEN_WORD = "FORBIDDEN_WORD";

    private static final int SECORD_RECORD_MAX_COUNT = 1000;

    private static boolean isTextMode;
    private static boolean isNightMode;

    private static LinkedList<SearchRecord> searchRecords;
    private static ArrayList<String> forbiddenWords;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        isTextMode = sharedPreferences.getBoolean(TEXT_MODE, false);
        isNightMode = sharedPreferences.getBoolean(NIGHT_MODE, false);
        searchRecords = new LinkedList<>();
        try {
            JSONArray array = new JSONArray(sharedPreferences.getString(SEARCH_RECORD, "[]"));
            for (int i = 0; i < array.length(); ++i) {
                JSONObject object = array.getJSONObject(i);
                String string = object.getString(SEARCH_RECORD_STRING);
                Log.d(TAG, "init: " + string);
                Long date = Long.parseLong(object.getString(SEARCH_RECORD_DATE));
                SearchRecord searchRecord = new SearchRecord(string, date);
                searchRecords.offer(searchRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(searchRecords, new Comparator<SearchRecord>() {
            @Override
            public int compare(SearchRecord searchRecord, SearchRecord t1) {
                long diff = searchRecord.date - t1.date;
                if (diff > 0) return -1;
                if (diff == 0) return 0;
                return 1;
            }
        });
        forbiddenWords = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(sharedPreferences.getString(FORBIDDEN_WORD, "[]"));
            for (int i = 0; i < array.length(); ++i)
                forbiddenWords.add(array.getString(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void store() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TEXT_MODE, isTextMode);
        editor.putBoolean(NIGHT_MODE, isNightMode);
        JSONArray searchArray = new JSONArray();
        for (SearchRecord searchRecord : searchRecords) {
            JSONObject object = new JSONObject();
            try {
                object.put(SEARCH_RECORD_STRING, searchRecord.string);
                object.put(SEARCH_RECORD_DATE, "" + searchRecord.date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            searchArray.put(object);
        }
        Log.d(TAG, "store: " + searchArray.toString());
        editor.putString(SEARCH_RECORD, searchArray.toString());
        JSONArray wordArray = new JSONArray();
        for (String s : forbiddenWords)
            wordArray.put(s);
        editor.putString(FORBIDDEN_WORD, wordArray.toString());
        editor.commit();
    }

    public static void putCategoryList(List<Integer> list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray array = new JSONArray();
        for (int i : list)
            array.put(i);
        editor.putString(CATEGORY_LIST, array.toString());
        editor.commit();
    }

    public static List<Integer> getCategoryList() {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(sharedPreferences.getString(CATEGORY_LIST, "[0]"));
            for (int i = 0; i < array.length(); ++i)
                list.add(array.getInt(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void setTextMode(boolean textMode) {
        isTextMode = textMode;
    }

    public static boolean getTextMode() {
        return isTextMode;
    }

    public static void setNightMode(boolean nightMode) {
        isNightMode = nightMode;
    }

    public static boolean getNightMode() {
        return isNightMode;
    }

    public static void addSearchRecord(String string) {
        SearchRecord searchRecord = new SearchRecord(string, new Date().getTime());
        searchRecords.offerFirst(searchRecord);
        if (searchRecords.size() > SECORD_RECORD_MAX_COUNT)
            searchRecords.pollLast();
    }

    public static List<SearchRecordSuggestion> getSearchRecord(String prefix, int count) {
        List<SearchRecordSuggestion> list = new ArrayList<>();
        int c = 0;
        for (SearchRecord searchRecord : searchRecords) {
            Log.d(TAG, "getSearchRecord: " + searchRecord.string);
            if (searchRecord.string.startsWith(prefix)) {
                list.add(new SearchRecordSuggestion(searchRecord.string));
                ++c;
                if (c == count)
                    break;
            }
        }
        return list;
    }

    public static void addForbiddenWord(String word) {
        forbiddenWords.add(word);
    }

    public static void removeForbiddenWord(String word) {
        forbiddenWords.remove(word);
    }

    public static List<String> getForbiddenWord() {
        return new ArrayList<>(forbiddenWords);
    }
}
