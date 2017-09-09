package com.java.group19;

import java.util.List;

/**
 * Created by liena on 17/9/9.
 */

public class SearchHelper {

    public interface OnFindSuggestionsListener {
        void onResults(List<SearchRecordSuggestion> results);
    }

    public static void findSuggestions(String query, final int limit, final OnFindSuggestionsListener listener) {

    }
}
