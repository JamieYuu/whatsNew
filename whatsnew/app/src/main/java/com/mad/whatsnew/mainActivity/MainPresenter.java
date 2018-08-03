package com.mad.whatsnew.mainActivity;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.whatsnew.adapter.NewsAdapter;
import com.mad.whatsnew.model.CustomLink;
import com.mad.whatsnew.model.News;
import com.mad.whatsnew.util.DownloadUtils;

import java.util.ArrayList;

import static com.mad.whatsnew.mainActivity.MainActivity.FIREBASE_REF_CUSLINK;
import static com.mad.whatsnew.mainActivity.MainActivity.TEST_URL1;

/**
 * Presenter class for main activity
 * Is the presenter in MVP
 */
public class MainPresenter implements MainContract.Presenter {
    private final MainContract.View mView;
    private NewsAdapter mNewsAdapter;
    private Boolean mLoggedIn = false;
    private long mStart;
    private long mEnd;
    private final ArrayList<String> mUrlList = new ArrayList<>();

    private final static String SNAPSHOT_TAG = "snapshot";

    /**
     * Constructor of main presenter
     * @param view is the main fragment object
     */
    public MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mUrlList.add(TEST_URL1);

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
    public void setAdapter() {
        new TestDownloadXml().execute();
    }

    @Override
    public void refresh() {
        new TestDownloadXml().execute();
    }

    @Override
    public void logoClicked() {
        mView.toLogoClicked(mLoggedIn);
    }

    /**
     * AsyncTask for download news from xml resources
     */
    public class TestDownloadXml extends AsyncTask<Void, Void, ArrayList<News>> {

        /**
         * Constructor of AsyncTask
         */
        public TestDownloadXml() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mStart = System.currentTimeMillis();
            mView.setProgressBarVisible(true);
            mView.setRecyclerViewVisible(false);
        }

        @Override
        protected ArrayList<News> doInBackground(Void... voids) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            if (user != null) {
                ref = FirebaseDatabase.getInstance().getReference(FIREBASE_REF_CUSLINK + user.getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Log.e(SNAPSHOT_TAG, postSnapshot.getKey());
                            CustomLink customLink = postSnapshot.getValue(CustomLink.class);
                            mUrlList.add(customLink.getLink());
                        }
                        new DownloadXml().execute();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {
                new DownloadXml().execute();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            super.onPostExecute(news);
        }
    }

    /**
     * AsyncTask for download news from xml resources
     */
    public class DownloadXml extends AsyncTask<Void, Void, ArrayList<News>> {

        /**
         * Constructor for AsyncTask
         */
        public DownloadXml() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.setProgressBarVisible(true);
            mView.setRecyclerViewVisible(false);
        }

        @Override
        protected ArrayList<News> doInBackground(Void... voids) {
            ArrayList<News> newsList = new ArrayList<>();
            ArrayList<News> reorderdList = new ArrayList<>();

            for (int i = 0; i < mUrlList.size(); i++) {
                String url = mUrlList.get(i);
                ArrayList<News> downloadList = DownloadUtils.downLoadXml(url, "", 150/ mUrlList.size());
                Log.e(url, downloadList.size()+"");
                for (News item : downloadList) {
                    newsList.add(item);
                    Log.e(url, item.getTitle());
                }
            }
            int size = newsList.size();
            for (int i = 0; i < size; i++) {
                int randomPos = (int)(Math.random()*newsList.size());
                reorderdList.add(newsList.get(randomPos));
                newsList.remove(randomPos);
            }
            return reorderdList;
        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            super.onPostExecute(news);
            mEnd = System.currentTimeMillis() - mStart;
            float timeUse = mEnd/1000F;
            mView.setFoundTv(news.size(), timeUse);
            mView.setRecyclerViewContent(mNewsAdapter, news);
            mView.setProgressBarVisible(false);
            mView.setRecyclerViewVisible(true);
        }
    }
}
