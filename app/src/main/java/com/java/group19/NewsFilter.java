package com.java.group19;

import com.java.group19.data.News;

/**
 * Created by liena on 17/9/9.
 */

public interface NewsFilter {
    boolean accept(News news);
}
