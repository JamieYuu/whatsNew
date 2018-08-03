package com.mad.whatsnew.loginActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mad.whatsnew.R;
import com.mad.whatsnew.util.ActivityUtils;

/**
 * The activity class for login activity
 * Set up fragment and presenter
 */
public class LoginActivity extends AppCompatActivity {
    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.login_activity_frame_content);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager()
                    , loginFragment, R.id.login_activity_frame_content);
        }
        mPresenter = new LoginPresenter(loginFragment);
    }
}
