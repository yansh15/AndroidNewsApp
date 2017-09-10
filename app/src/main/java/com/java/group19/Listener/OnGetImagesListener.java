package com.java.group19.Listener;

import java.util.ArrayList;

public interface OnGetImagesListener {
    void onFinish(ArrayList<String> urls);
    void onError(Exception e);
}
