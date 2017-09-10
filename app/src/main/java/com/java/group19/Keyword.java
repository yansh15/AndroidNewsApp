package com.java.group19;

import java.io.Serializable;

/**
 * Created by strongoier on 17/9/9.
 */

public class Keyword implements Serializable {
    private News news;
    private String word;
    private double score;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
