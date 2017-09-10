package com.java.group19;

import org.litepal.crud.DataSupport;

import java.util.Vector;

/**
 * Created by strongoier on 17/9/9.
 */

public class Config extends DataSupport {
    private boolean isDarkTheme;
    private boolean isTextMode;
    private Vector<String> forbiddenWords;
    private Vector<String> searchRecords;

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

    public Vector<String> getForbiddenWords() {
        return forbiddenWords;
    }

    public void setForbiddenWords(Vector<String> forbiddenWords) {
        this.forbiddenWords = forbiddenWords;
    }

    public Vector<String> getSearchRecords() {
        return searchRecords;
    }

    public void setSearchRecords(Vector<String> searchRecords) {
        this.searchRecords = searchRecords;
    }
}
