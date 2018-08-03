package com.mad.whatsnew.model;

/**
 * Custom link model, carry custom define data
 */
public class CustomLink {
    private String mLink;
    private String mTitle;

    private String mFirebaseKey;

    public CustomLink() {}

    /**
     * Constructor of custom link model
     * @param link the link
     * @param title the title
     * @param firebaseKey the unique firebase database key
     */
    public CustomLink(String link, String title, String firebaseKey) {
        this.mLink = link;
        this.mTitle = title;
        mFirebaseKey = firebaseKey;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getFirebaseKey() {
        return mFirebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        mFirebaseKey = firebaseKey;
    }
}
