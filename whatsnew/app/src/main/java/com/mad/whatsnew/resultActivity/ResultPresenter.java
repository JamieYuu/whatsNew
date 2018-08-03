package com.mad.whatsnew.resultActivity;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.whatsnew.adapter.NewsAdapter;
import com.mad.whatsnew.mainActivity.MainPresenter;
import com.mad.whatsnew.model.CustomLink;
import com.mad.whatsnew.model.News;
import com.mad.whatsnew.util.DownloadUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.mad.whatsnew.mainActivity.MainActivity.FIREBASE_REF_CUSLINK;
import static com.mad.whatsnew.mainActivity.MainActivity.TEST_URL1;
import static com.mad.whatsnew.mainActivity.MainActivity.TEST_URL2;

/**
 * Presenter class
 * Is the presenter in MVP
 */
public class ResultPresenter implements ResultContract.Presenter{
    private final ResultContract.View mView;
    private NewsAdapter mNewsAdapter;
    private String mSearchText;
    private long mStart;
    private long mEnd;
    private final ArrayList<String> mUrlList = new ArrayList<>();

    /**
     * Constructor
     * @param searchText the search keyword
     * @param view the fragment
     */
    public ResultPresenter(String searchText, ResultContract.View view) {
        mView = view;
        mSearchText = searchText;
        mView.setPresenter(this);
        mUrlList.add(TEST_URL1);
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

    /**
     * Download news AsyncTask
     */
    public class TestDownloadXml extends AsyncTask<Void, Void, ArrayList<News>> {

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
     * Download news AsyncTask
     */
    public class DownloadXml extends AsyncTask<Void, Void, ArrayList<News>> {

        public DownloadXml() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.setProgressBarVisible(true);
            mView.setRecyclerViewVisible(false);
            mView.setErrorTextVisible(false);
        }

        @Override
        protected ArrayList<News> doInBackground(Void... voids) {
            ArrayList<News> newsList = new ArrayList<>();
            ArrayList<News> reorderdList = new ArrayList<>();

            Log.e("url", mUrlList.size()+"");
            for (int i = 0; i < mUrlList.size(); i++) {
                String url = mUrlList.get(i);
                ArrayList<News> downloadList = DownloadUtils.downLoadXml(url, mSearchText, 256);
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
            if (news.isEmpty()) {
                mView.setRecyclerViewVisible(false);
                mView.setProgressBarVisible(false);
                mView.setErrorTextVisible(true);
            } else {
                mEnd = System.currentTimeMillis() - mStart;
                float timeUse = mEnd/1000F;
                mView.setFoundTv(news.size(), timeUse);
                mView.setRecyclerViewContent(mNewsAdapter, news);
                mView.setProgressBarVisible(false);
                mView.setRecyclerViewVisible(true);
            }
        }
    }
}
