package com.mad.whatsnew.searchActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mad.whatsnew.R;
import com.mad.whatsnew.favoriteActivity.FavoriteActivity;
import com.mad.whatsnew.mainActivity.MainActivity;
import com.mad.whatsnew.settingActivity.SettingActivity;
import com.mad.whatsnew.util.ActivityUtils;
import com.mad.whatsnew.util.FirebaseAuthUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.mad.whatsnew.mainActivity.MainActivity.FIREBASE_REF_IMAGE;

/**
 * Activity class for search activity
 */
public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SearchContract.Presenter mSearchPresenter;
    public final static String EXTRA_SEARCH_TEXT = "SEARCH_TEXT";
    public final static String PREFER_KEY = "HISTORY";
    public final static String PREFER_HISTORY = "PREFER_HISTORY";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFram);

        SharedPreferences settings = getSharedPreferences(PREFER_KEY, MODE_PRIVATE);
        ArrayList<String> testQuery = new ArrayList<>();
        Set<String> getQuery = settings.getStringSet(PREFER_HISTORY, null);
        if (getQuery != null && !getQuery.isEmpty()) {
            for (String his : getQuery) {
                testQuery.add(his);
            }
        }

        if (searchFragment == null) {
            searchFragment = SearchFragment.newInstance(testQuery);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    searchFragment, R.id.contentFram);
        }

        mSearchPresenter = new SearchPresenter(testQuery, searchFragment);
    }

    /**
     * Handle logo click event
     * @param view is the view
     */
    public void logoClicked(View view) {
        mSearchPresenter.logoClicked();
    }

    /**
     * AsyncTask for download user logo
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        if (id == R.id.menu_nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
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
