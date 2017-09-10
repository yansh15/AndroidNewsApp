package com.java.group19.data;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by strongoier on 17/9/9.
 */

public class Config extends DataSupport {
    private boolean isDarkTheme;
    private boolean isTextMode;
    private ArrayList<String> forbiddenWords;
    private ArrayList<String> searchRecords;

    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    public boolean isTextMode() {
        return isTextMode;
    }

    public void setTextMode(boolean textMode) {
        isTextMode = textMode;
    }

    public ArrayList<String> getForbiddenWords() {
        return forbiddenWords;
    }

    public void setForbiddenWords(ArrayList<String> forbiddenWords) {
        this.forbiddenWords = forbiddenWords;
    }

    public ArrayList<String> getSearchRecords() {
        return searchRecords;
    }

    public void setSearchRecords(ArrayList<String> searchRecords) {
        this.searchRecords = searchRecords;
    }
}
