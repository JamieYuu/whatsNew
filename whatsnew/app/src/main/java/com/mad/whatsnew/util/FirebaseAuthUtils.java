package com.mad.whatsnew.util;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mad.whatsnew.R;

/**
 * Firebase authtication utils
 */
public class FirebaseAuthUtils {

    private final static String LOGIN_STATUS = "login status";
    /**
     * Login with email and password
     * @param fragmentActivity the fragment activity
     * @param firebaseAuth the firebase auth
     * @param email the email
     * @param password the password
     */
    public static void loginWithEmailPassword(final FragmentActivity fragmentActivity
            , FirebaseAuth firebaseAuth, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(fragmentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(LOGIN_STATUS, task.isSuccessful() + "");
                        if (!task.isSuccessful()) {
                            firebaseLoginFail(fragmentActivity);
                        }
                    }
                });
    }

    /**
     * Toast for login
     * @param context the application context
     */
    public static void firebaseLoginFail(Context context) {
        Toast toast = Toast.makeText(context
                , context.getString(R.string.fireauth_login_fail), Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Toast for register
     * @param context the application context
     */
    public static void firebaseRegisterFail(Context context) {
        Toast toast = Toast.makeText(context
                , context.getString(R.string.fireauth_reg_fail), Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Toast for log out
     * @param context the application context
     */
    public static void firebaseLogOut(Context context) {
        FirebaseAuth.getInstance().signOut();
        Toast toast = Toast.makeText(context
                , context.getString(R.string.fireauth_logout_toast), Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Sign up with email and password
     * @param fragmentActivity the fragment activity
     * @param firebaseAuth the firebase auth
     * @param email the email
     * @param password the password
     */
    public static void firebaseSignUp(final FragmentActivity fragmentActivity
            , final FirebaseAuth firebaseAuth, final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(fragmentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            firebaseRegisterFail(fragmentActivity);
                        } else {
                            loginWithEmailPassword(fragmentActivity, firebaseAuth, email, password);
                        }
                    }
                });
    }

    /**
     * Update user profile
     * @param firebaseAuth the firebase auth
     * @param username the username
     */
    public static void firebaseUpdateUserProfile(FirebaseAuth firebaseAuth, String username) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}
