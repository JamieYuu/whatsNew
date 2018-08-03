package com.mad.whatsnew.newsActivity;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.whatsnew.R;
import com.mad.whatsnew.model.Favorite;
import com.mad.whatsnew.model.News;
import com.mad.whatsnew.util.DownloadUtils;
import com.mad.whatsnew.util.FirebaseDBUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.mad.whatsnew.mainActivity.MainActivity.FIREBASE_REF_FAV;

/**
 * Presenter class for news activity
 * Is the presenter in MVP
 */
public class NewsPresenter implements NewsContract.Presenter{
    private final NewsContract.View mNewsView;
    private String mTitle;
    private String mDate;
    private String mAuthor;
    private String mContentLink;
    private String mNewsContent = "";
    private Boolean mLoggedIn = false;
    private Boolean mLiked = false;

    /**
     * Constructor of presenter
     * @param title of news
     * @param date of news
     * @param author of news
     * @param contentLink of news
     * @param newsView the fragment of news
     */
    public NewsPresenter(String title, String date, String author,
                         String contentLink, NewsContract.View newsView) {
        mTitle = title;
        mDate = date;
        mAuthor = author;
        mContentLink = contentLink;
        mNewsView = newsView;

        mNewsView.setPresenter(this);

        //for login
        FirebaseAuth firebaseAuth;

        //check login
        FirebaseAuth.AuthStateListener authStateListener;
        //for login
        firebaseAuth = FirebaseAuth.getInstance();
        //check login
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    mLoggedIn = false;
                } else {
                    mLoggedIn = true;
                }
            }
        };
        //check login
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void start() {

    }

    @Override
    public void getIntentContent() {
        mNewsView.setTitle(mTitle);
        mNewsView.setDate(mDate);
        mNewsView.setAuthor(mAuthor);
    }

    @Override
    public void downloadContent() {
        new GetLinkContent().execute();
    }

    @Override
    public void speechContent(TextToSpeech tts) {
        mNewsContent = mNewsView.getTitleTvText() + " " + mNewsView.getContent();
        tts.speak(mNewsContent, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void addToFavorite() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            mNewsView.noUserFav();
        } else {
            String refPath = FIREBASE_REF_FAV + user.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(refPath);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Favorite favorite = postSnapshot.getValue(Favorite.class);
                        if (mTitle.equals(favorite.getTitle())) {
                            return;
                        }
                    }
                    ArrayList<String> cate = new ArrayList<>();
                    cate.add("null");
                    News news = new News(mTitle, null, mDate, mAuthor, mContentLink, null, null, mNewsContent, cate);
                    FirebaseDBUtils.pushToDb(FIREBASE_REF_FAV + user.getUid(), news);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /**
     * AsyncTask for get content of news link
     */
    public class GetLinkContent extends AsyncTask<Void, Void, String> {

        /**
         * Constructor
         */
        public GetLinkContent() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mNewsView.setProgressBarVisible(true);
            mNewsView.setContentTvVisible(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            mNewsContent = DownloadUtils.downloadHtml(mContentLink);
            return mNewsContent;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mNewsView.setContent(s);
            mNewsView.setProgressBarVisible(false);
            mNewsView.setContentTvVisible(true);
        }
    }
}

