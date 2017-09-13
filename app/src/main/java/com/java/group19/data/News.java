package com.java.group19.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class News implements Serializable {
    private String uniqueId;
    private String classTag;
    private String author;
    private ArrayList<String> pictures;
    private String source;
    private Date time;
    private String title;
    private String url;
    private String intro;
    private String journal;
    private String content;
    private ArrayList<String> words;
    private ArrayList<Double> scores;
    private ArrayList<String> entries;
    private Date lastVisitTime;
    private Date lastFavoriteTime;
    private int visitCount;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getClassTag() {
        return classTag;
    }

    public void setClassTag(String classTag) {
        this.classTag = classTag;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public ArrayList<Double> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Double> scores) {
        this.scores = scores;
    }

    public ArrayList<String> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<String> entries) {
        this.entries = entries;
    }

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public Date getLastFavoriteTime() {
        return lastFavoriteTime;
    }

    public void setLastFavoriteTime(Date lastFavoriteTime) {
        this.lastFavoriteTime = lastFavoriteTime;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }
}
