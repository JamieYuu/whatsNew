package com.mad.whatsnew.registerActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mad.whatsnew.R;
import com.mad.whatsnew.util.ActivityUtils;

/**
 * Activity class for register activity
 */
public class RegisterActivity extends AppCompatActivity {
    private RegisterContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.register_activity_frame_content);
        if (fragment == null) {
            fragment = RegisterFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager()
                    , fragment, R.id.register_activity_frame_content);
        }
        mPresenter = new RegisterPresenter(fragment);
    }

}
