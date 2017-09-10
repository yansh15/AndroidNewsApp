package com.java.group19;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.arlib.floatingsearchview.FloatingSearchView;

/**
 * Created by liena on 17/9/10.
 */

public abstract class BaseSearchFragment extends Fragment {

    public interface BaseSearchFragmentCallbacks {
        void onAttachSearchViewToDrawer(FloatingSearchView searchView);
    }

    private BaseSearchFragmentCallbacks callbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseSearchFragmentCallbacks) {
            callbacks = (BaseSearchFragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement BaseSearchFragmentCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    protected void attachSearchViewActivityDrawer(FloatingSearchView searchView) {
        if (callbacks != null) {
            callbacks.onAttachSearchViewToDrawer(searchView);
        }
    }

    public abstract boolean onActivityBackPress();
}
