package com.java.group19.data;

import android.support.annotation.NonNull;

public class Keyword implements Comparable<Keyword> {

    private String word;
    private double score;

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

    @Override
    public int compareTo(@NonNull Keyword keyword) {
        return this.score < keyword.getScore() ? 1 : (this.score > keyword.getScore() ? -1 : 0);
    }
}
