package com.mad.whatsnew.registerActivity;

import android.support.v4.app.FragmentActivity;

import com.mad.whatsnew.BasePresenter;
import com.mad.whatsnew.BaseView;

/**
 * Contract class for register activity
 */
public interface RegisterContract {

    /**
     * View interface
     */
    interface View extends BaseView<Presenter> {

        /**
         * Get email
         * @return email text
         */
        String getEmail();

        /**
         * Get second password
         * @return second password text
         */
        String getSecPass();

        /**
         * Get password
         * @return password text
         */
        String getPassword();

        /**
         * Set second password error
         */
        void setSecPassError();

        /**
         * Set password error
         */
        void setPasswordError();

        /**
         * Set email error
         */
        void setEmailError();

        /**
         * Clear error
         */
        void clearEtError();

        /**
         * Finish activity
         */
        void finishActivity();

        /**
         * Show toast
         */
        void registedToast();

    }

    /**
     * Presenter interface
     */
    interface Presenter extends BasePresenter {

        /**
         * Handle register event
         * @param fragmentActivity the register fragment activity
         */
        void registerBtnClickedEvent(FragmentActivity fragmentActivity);

    }
}
