package com.java.group19;

import org.litepal.crud.DataSupport;

/**
 * Created by strongoier on 17/9/9.
 */

public class Config extends DataSupport {
    private boolean isDarkTheme;
    private boolean isTextMode;

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
}
