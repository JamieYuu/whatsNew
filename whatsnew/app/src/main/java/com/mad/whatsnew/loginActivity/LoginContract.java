package com.mad.whatsnew.loginActivity;

import android.support.v4.app.FragmentActivity;

import com.mad.whatsnew.BasePresenter;
import com.mad.whatsnew.BaseView;

/**
 * The interface class for connect login fragment and login presenter
 */
public interface LoginContract {

    /**
     * Interface for view
     */
    interface View extends BaseView<Presenter> {

        /**
         * Get the email of user input
         * @return is the user input email
         */
        String getEmail();

        /**
         * Get the password of user input
         * @return is the user input password
         */
        String getPassword();

        /**
         * Show login result depending on logged in or not
         * @param loggedIn is the login status
         */
        void showLoginResult(Boolean loggedIn);

        /**
         * Set email edit text error message
         * @param hasError is the error status
         */
        void setEmailError(Boolean hasError);

        /**
         * Set password edit text error message
         * @param hasError is the error status
         */
        void setPasswordError(Boolean hasError);

        /**
         * Dismiss fragment
         */
        void finishActivity();

        /**
         * Show message of logout by toast
         */
        void showLogoutToast();
    }

    /**
     * Interface for presenter
     */
    interface Presenter extends BasePresenter {

        /**
         * Handle login button onclick event
         * @param fragmentActivity is the fragment activity of fragment
         */
        void loginClickEvent(FragmentActivity fragmentActivity);
    }
}
