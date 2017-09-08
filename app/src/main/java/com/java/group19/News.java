package com.java.group19;

import org.litepal.crud.DataSupport;

import java.net.URL;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by strongoier on 17/9/8.
 */

public class News extends DataSupport {
    private String classTag;
    private String author;
    private String id;
    private Vector<String> pictures;
    private String source;
    private Date time;
    private String title;
    private URL url;
    private String intro;
    private String journal;
    private String content;
    private TreeMap<String, Double> keywords;
    private Vector<String> entries;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vector<String> getPictures() {
        return pictures;
    }

    public void setPictures(Vector<String> pictures) {
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

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
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

    public TreeMap<String, Double> getKeywords() {
        return keywords;
    }

    public void setKeywords(TreeMap<String, Double> keywords) {
        this.keywords = keywords;
    }

    public Vector<String> getEntries() {
        return entries;
    }

    public void setEntries(Vector<String> entries) {
        this.entries = entries;
    }
}
