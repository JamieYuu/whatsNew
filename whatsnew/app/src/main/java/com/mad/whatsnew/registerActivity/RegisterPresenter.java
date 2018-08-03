package com.mad.whatsnew.registerActivity;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mad.whatsnew.util.FirebaseAuthUtils;
import com.mad.whatsnew.util.FirebaseStoreUtil;

/**
 * Presenter class for register activity
 * Is the presenter in MVP
 */
public class RegisterPresenter implements RegisterContract.Presenter{
    private RegisterContract.View mView;
    private FragmentActivity mFragmentActivity;
    private String mEmail;
    private String mPassword;
    private String mSecondPass;

    //for login
    private FirebaseAuth mFirebaseAuth;

    //check login
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public RegisterPresenter(RegisterContract.View view) {
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
                } else {
                    mView.registedToast();
                    mView.finishActivity();
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
    public void registerBtnClickedEvent(FragmentActivity fragmentActivity) {
        mFragmentActivity = fragmentActivity;
        mEmail = mView.getEmail();
        mPassword = mView.getPassword();
        mSecondPass = mView.getSecPass();
        Boolean valid = true;
        if (mEmail == null || mEmail.trim().equals("")) {
            mView.setEmailError();
            valid = false;
        }
        if (mPassword == null || mPassword.trim().equals("")) {
            mView.setPasswordError();
            valid = false;
        }
        if (!mSecondPass.equals(mPassword)) {
            mView.setSecPassError();
            valid = false;
        }
        if (valid) {
            new SignUpUser().execute();
        }
    }

    /**
     * AsyncTask for sign up user
     */
    public class SignUpUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseAuthUtils.firebaseSignUp(mFragmentActivity
                    , mFirebaseAuth, mEmail, mPassword);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
