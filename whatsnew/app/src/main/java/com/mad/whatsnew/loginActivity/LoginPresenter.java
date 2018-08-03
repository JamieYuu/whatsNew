package com.mad.whatsnew.loginActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.whatsnew.util.FirebaseAuthUtils;

/**
 * Presenter class for login activity
 * Is the presenter in MVP
 */
public class LoginPresenter implements LoginContract.Presenter{
    private String mEmail;
    private String mPassword;
    private FragmentActivity mFragmentActivity;
    private LoginContract.View mView;

    private static String TAG = "login presenter";

    //for login
    private FirebaseAuth mFirebaseAuth;

    //check login
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    /**
     * The constructor of login presenter
     * @param view is the login fragment
     */
    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mView.setPresenter(this);
        //for login
        mFirebaseAuth = FirebaseAuth.getInstance();
        //check login
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Log.e(TAG, "not logged in");
                } else {
                    Log.e(TAG, user.getUid());
                    mView.showLogoutToast();
                    mView.finishActivity();
                    Log.e(TAG, "name: " + user.getDisplayName());
                }
            }
        };
        //check login
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void start() {

    }

    @Override
    public void loginClickEvent(FragmentActivity fragmentActivity) {
        mFragmentActivity = fragmentActivity;
        Boolean loginValid = true;
        mEmail = mView.getEmail();
        mPassword = mView.getPassword();
        if (mEmail.equals("")) {
            mView.setEmailError(true);
            loginValid = false;
        } else {
            mView.setEmailError(false);
        }

        if (mPassword.equals("")) {
            mView.setPasswordError(true);
            loginValid = false;
        } else {
            mView.setPasswordError(false);
        }

        if (loginValid) {
            new LoginEvent().execute();
        }
    }

    /**
     * AsyncTask class for login event
     * In doInBackground thread, the firebase sigin in function
     * in util class will be called to sign in
     */
    private class LoginEvent extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            mView.setEmailError(false);
            mView.setPasswordError(false);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            FirebaseAuthUtils.loginWithEmailPassword(mFragmentActivity
                    , mFirebaseAuth, mEmail, mPassword);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
