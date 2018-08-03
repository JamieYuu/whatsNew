package com.mad.whatsnew.favoriteActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.mad.whatsnew.mainActivity.MainActivity;
import com.mad.whatsnew.searchActivity.SearchActivity;
import com.mad.whatsnew.settingActivity.SettingActivity;
import com.mad.whatsnew.util.ActivityUtils;
import com.mad.whatsnew.util.FirebaseAuthUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.mad.whatsnew.mainActivity.MainActivity.FIREBASE_REF_IMAGE;

/**
 * Activity class for favorite activity, contains a fragment, a presenter and a navigation menu
 */
public class FavoriteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FavoritePresenter mFavoritePresenter;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Define and set fragment and presenter.
        FavoriteFragment favoriteFragment = (FavoriteFragment) getSupportFragmentManager()
                .findFragmentById(R.id.favorite_activity_frame_content);

        if (favoriteFragment == null) {
            favoriteFragment = FavoriteFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager()
                    , favoriteFragment, R.id.favorite_activity_frame_content);
        }

        mFavoritePresenter = new FavoritePresenter(favoriteFragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Define user logo and user email in navigation menu
        View headerView = navigationView.getHeaderView(0);
        mImageView = (ImageView) headerView.findViewById(R.id.menu_user_icon);
        final TextView email = (TextView) headerView.findViewById(R.id.menu_user_email);
        navigationView.setNavigationItemSelectedListener(this);

        // Check firebase user to set the text of navigation menu header
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
    }

    /**
     * Logo click event handler, if user not logged in and click logo
     * imageview, this function will be called
     * @param view is the view of current activity
     */
    public void logoClicked(View view) {
        mFavoritePresenter.logoClicked();
    }

    /**
     * AsyncTask for update logo, the logo will be download from firebase storage
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
    protected void onResume() {
        super.onResume();
        mFavoritePresenter.setAdapter();
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
        getMenuInflater().inflate(R.menu.favorite, menu);
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
        } else if (id == R.id.menu_nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
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
