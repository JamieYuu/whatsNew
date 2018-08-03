package com.mad.whatsnew.mainActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mad.whatsnew.favoriteActivity.FavoriteActivity;
import com.mad.whatsnew.loginActivity.LoginActivity;
import com.mad.whatsnew.searchActivity.SearchActivity;
import com.mad.whatsnew.R;
import com.mad.whatsnew.settingActivity.SettingActivity;
import com.mad.whatsnew.util.ActivityUtils;
import com.mad.whatsnew.util.FirebaseAuthUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The activity class for main activity
 * Set up fragment and presenter
 * Defines some intent extra key
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // Define intent extra keys
    public final static String EXTRA_TITLE = "title";
    public final static String EXTRA_DATE = "date";
    public final static String EXTRA_AUTHOR = "creator";
    public final static String EXTRA_SOURCE = "source";
    public final static String EXTRA_LINK = "link";
    public final static String FIREBASE_REF_FAV = "Favorite/";
    public final static String FIREBASE_REF_CUSLINK = "CustomLink/";
    public final static String FIREBASE_REF_IMAGE = "images/";

    // Defines based urls of rss source
    public final static String TEST_URL1 = "http://www.abc.net.au/news/feed/52498/rss.xml";
    public final static String TEST_URL2 = "https://www.smh.com.au/rss/feed.xml";

    private MainPresenter mMainPresenter;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup fragment and presetner
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_activity_frame_content);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    mainFragment, R.id.main_activity_frame_content);
        }

        mMainPresenter = new MainPresenter(mainFragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set up navigation header information
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        mImageView = (ImageView) headerView.findViewById(R.id.menu_user_icon);
        final TextView email = (TextView) headerView.findViewById(R.id.menu_user_email);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseAuth.AuthStateListener authStateListener;
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    email.setText(R.string.nav_no_login_alert);
                    mImageView.setImageResource(R.drawable.ic_menu_logo);
                } else {
                    email.setText(user.getEmail());
                    StorageReference sref = FirebaseStorage.getInstance().getReference(FIREBASE_REF_IMAGE + user.getUid());
                    sref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            new UpdateLogo().execute(uri.toString());
                        }
                    });
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    /**
     * Handler of navigation logo click event
     * @param view is the context
     */
    public void logoClicked(View view) {
        mMainPresenter.logoClicked();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * AsyncTask for update user logo
     * Download logo from a string url
     */
    private class UpdateLogo extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap logo = null;
            try {
                Log.e(getClass().toString(), strings[0]);
                InputStream in = new URL(strings[0]).openStream();
                logo = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.main_menu_logout) {
            FirebaseAuthUtils.firebaseLogOut(getApplicationContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_nav_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_nav_favorite) {
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_nav_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
