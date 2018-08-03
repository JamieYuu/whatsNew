package com.mad.whatsnew.newsActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.mad.whatsnew.util.ActivityUtils;
import com.mad.whatsnew.util.DownloadUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_AUTHOR;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_DATE;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_LINK;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_SOURCE;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_TITLE;
import static com.mad.whatsnew.mainActivity.MainActivity.FIREBASE_REF_FAV;
import static com.mad.whatsnew.mainActivity.MainActivity.TEST_URL1;

/**
 * Activity class for news activity
 */
public class NewsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private String mTitle;
    private String mDate;
    private String mAuthor;
    private String mContentLink;

    private TextToSpeech mTts;
    private Boolean mOnPlay = false;
    private NewsPresenter mNewsPresenter;

    @BindView(R.id.news_activity_play_fab) FloatingActionButton mPlayBtn;
    @BindView(R.id.news_activity_fav_fab) android.support.design.widget.FloatingActionButton mFavFab;
    @BindView(R.id.news_activity_next_fab) FloatingActionButton mNextFab;

    private final static String TTS_TAG = "tts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mTitle = intent.getStringExtra(EXTRA_TITLE);
        mAuthor = intent.getStringExtra(EXTRA_AUTHOR);
        mDate = intent.getStringExtra(EXTRA_DATE);
        mContentLink = intent.getStringExtra(EXTRA_LINK);

        NewsFragment newsFragment = (NewsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frameContent);

        if (newsFragment == null) {
            newsFragment = NewsFragment.newInstance(mTitle, mDate, mAuthor, mContentLink);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    newsFragment, R.id.frameContent);
        }

        mNewsPresenter = new NewsPresenter(mTitle, mDate, mAuthor, mContentLink, newsFragment);

        mTts = new TextToSpeech(this, this);
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPlay) {
                    mTts.stop();
                    mOnPlay = false;
                    mPlayBtn.setImageResource(R.drawable.ic_action_play);
                } else {
                    mNewsPresenter.speechContent(mTts);
                    mPlayBtn.setImageResource(R.drawable.ic_action_stop);
                    mOnPlay = true;
                }
            }
        });

        mFavFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewsPresenter.addToFavorite();
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    mFavFab.setImageResource(R.drawable.ic_start_full);
                    Toast.makeText(getApplicationContext()
                            , getText(R.string.mark_fav_toast), Toast.LENGTH_SHORT);
                }
            }
        });

        mNextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NextClickEvent().execute();
            }
        });

        FirebaseAuth.AuthStateListener authStateListener;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                } else {
                    String refPath = FIREBASE_REF_FAV + user.getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(refPath);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                Favorite favorite = postSnapshot.getValue(Favorite.class);
                                if (mTitle.equals(favorite.getTitle())) {
                                    mFavFab.setImageResource(R.drawable.ic_start_full);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
        //check login
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    /**
     * AsyncTask for next news fab clicked event
     */
    private class NextClickEvent extends AsyncTask<Void, Void, News> {

        @Override
        protected News doInBackground(Void... voids) {
            News nextNews = DownloadUtils.downloadOneNews(TEST_URL1);
            return nextNews;
        }

        @Override
        protected void onPostExecute(News news) {
            super.onPostExecute(news);
            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
            intent.putExtra(EXTRA_TITLE, news.getTitle());
            intent.putExtra(EXTRA_DATE, news.getDate());
            intent.putExtra(EXTRA_AUTHOR, news.getCreator());
            intent.putExtra(EXTRA_SOURCE, news.getSource());
            intent.putExtra(EXTRA_LINK, news.getLink());
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = mTts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TTS_TAG, "This Language is not supported");
            }

        } else {
            Log.e(TTS_TAG, "Initilization Failed!");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTts.stop();
        mTts.shutdown();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.news_menu_quit) {
            mTts.stop();
            mTts.shutdown();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
