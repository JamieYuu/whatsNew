package com.mad.whatsnew.model;

import java.net.URL;
import java.util.ArrayList;

/**
 * News model class
 */
public class News {
    private String mTitle;
    private String mDescription;
    private String mCreator;
    private String mDate;
    private String mLink;
    private String mImageUrl;
    private String mSource;
    private String mContent;
    private ArrayList<String> mCategories;

    /**
     * Non-parameter constructor
     */
    public News() {}

    /**
     * Constructor of news model
     * @param title the title of news
     * @param description the description of news
     * @param date the date of news
     * @param creator the creator of news
     * @param link the link of news
     * @param imageUrl the imageUrl of news
     * @param source the source of news
     * @param content the content of news
     * @param categories the categories of news
     */
    public News(String title, String description, String date
                , String creator, String link, String imageUrl
                , String source, String content, ArrayList<String> categories) {
        this.mTitle = title;
        this.mDescription = description;
        this.mDate = date;
        this.mCreator = creator;
        this.mLink = link;
        this.mImageUrl = imageUrl;
        this.mSource = source;
        this.mContent = content;
        this.mCategories = categories;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
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

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public ArrayList<String> getCategories() {
        return mCategories;
    }

    public void setCategories(ArrayList<String> categories) {
        mCategories = categories;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
