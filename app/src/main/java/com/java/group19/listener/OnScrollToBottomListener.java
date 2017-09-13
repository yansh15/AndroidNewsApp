package com.java.group19.listener;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class OnScrollToBottomListener extends RecyclerView.OnScrollListener {

    private boolean isLoading = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!isLoading && recyclerView.getLayoutManager().getItemCount() != 0 && !recyclerView.canScrollVertically(1)) {
            isLoading = true;
            onScrollToBottom();
        }
    }

    public abstract void onScrollToBottom();

    public void noticeLoadingEnd() {
        isLoading = false;
    }
}
