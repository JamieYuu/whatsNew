package com.mad.whatsnew.model;

import java.net.URL;
import java.util.ArrayList;

/**
 * News model class
 */
public class Favorite {
    private String mTitle;
    private String mCreator;
    private String mDate;
    private String mLink;
    private String mSource;

    private String mFirebaseKey;

    /**
     * Non-parameter constructor
     */
    public Favorite() {}

    /**
     * Constructor of favorite model
     * @param title the title of news
     * @param date the date of news
     * @param creator the author of news
     * @param link the link of news
     * @param source the source of news
     * @param firebaseKey the unique firebase database key
     */
    public Favorite(String title, String date
            , String creator, String link, String source, String firebaseKey) {
        this.mTitle = title;
        this.mDate = date;
        this.mCreator = creator;
        this.mLink = link;
        this.mSource = source;
        this.mFirebaseKey = firebaseKey;
    }


    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getCreator() {
        return mCreator;
    }

    public void setCreator(String creator) {
        mCreator = creator;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getFirebaseKey() {
        return mFirebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        mFirebaseKey = firebaseKey;
    }
}
