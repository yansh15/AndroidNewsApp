package com.java.group19.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by 阎世宏 on 2017/9/11.
 */

public abstract class OnScrollToBottomListener extends RecyclerView.OnScrollListener {

    private boolean isLoading = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!isLoading && !recyclerView.canScrollVertically(1)) {
            isLoading = true;
            onScrollToBottom();
        }
    }

    public abstract void onScrollToBottom();

    public void noticeLoadingEnd() {
        isLoading = false;
    }
}
