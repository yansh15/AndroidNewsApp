package com.java.group19.listener;

import java.util.ArrayList;

public interface OnGetImagesListener {
    void onFinish(ArrayList<String> urls);
    void onError(Exception e);
}
