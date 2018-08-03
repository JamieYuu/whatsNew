package com.mad.whatsnew.searchActivity;

import com.mad.whatsnew.BasePresenter;
import com.mad.whatsnew.BaseView;

import java.util.ArrayList;

/**
 * Contract class for search activity
 */
public interface SearchContract {

    /**
     * View interface
     */
    interface View extends BaseView<Presenter> {

        /**
         * Set history list adapter
         * @param query is the dataset
         */
        void setHistoryListAdapter(ArrayList<String> query);

        /**
         * Get result with history
         * @param keyword is the search keyword
         */
        void getResultWithHistory(String keyword);

        /**
         * Handle logo click event
         * @param loggedIn is the login status
         */
        void toLogoClicked(Boolean loggedIn);
    }

    /**
     * Presenter interface
     */
    interface Presenter extends BasePresenter {

        /**
         * Filter history
         * @param keyword is the keyword input
         */
        void filterHistory(String keyword);

        /**
         * Set history list adpater
         */
        void createHistoryListAdapter();

        /**
         * Handle history list click event
         * @param position position clicked
         */
        void historyListClickedEvent(int position);

        /**
         * Handle logo click event
         */
        void logoClicked();
    }
}
