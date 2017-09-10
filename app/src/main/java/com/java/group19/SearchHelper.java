package com.java.group19;

import java.util.List;
import java.util.Vector;

/**
 * Created by liena on 17/9/9.
 */

public class SearchHelper {

    public interface OnFindSuggestionsListener {
        void onResults(List<SearchRecordSuggestion> results);
    }

    public static void findSuggestions(String query, final int limit, final OnFindSuggestionsListener listener) {
        List<String> tmps = DatabaseHelper.getLatestSearchRecords(query, limit);
        List<SearchRecordSuggestion> suggestions = new Vector<>();
        for (String str : tmps)
            suggestions.add(new SearchRecordSuggestion(str));
        listener.onResults(suggestions);
    }
}
