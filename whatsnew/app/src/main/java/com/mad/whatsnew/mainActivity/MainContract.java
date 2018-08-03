package com.mad.whatsnew.mainActivity;

import com.mad.whatsnew.BasePresenter;
import com.mad.whatsnew.BaseView;
import com.mad.whatsnew.adapter.NewsAdapter;
import com.mad.whatsnew.model.News;

import java.util.ArrayList;

/**
 * Contract class for define view and presenter interfaces
 */
public interface MainContract {
    /**
     * View interface
     */
    interface View extends BaseView<Presenter> {

        /**
         * Set news recycler view content
         * @param newsAdapter is the news adapter
         * @param newsArrayList is the data set
         */
        void setRecyclerViewContent(NewsAdapter newsAdapter, ArrayList<News> newsArrayList);

        /**
         * Set visibility of progress bar
         * @param visible is the visible status
         */
        void setProgressBarVisible(Boolean visible);

        /**
         * Set visibility of recycler view
         * @param visible is the visible status
         */
        void setRecyclerViewVisible(Boolean visible);

        /**
         * Handle logo clicked event
         * @param loggedIn is the login status
         */
        void toLogoClicked(Boolean loggedIn);

        /**
         * Set the found text view text
         * @param number is the number of news found
         * @param time is the time used
         */
        void setFoundTv(int number, float time);

    }

    /**
     * Presenter interface
     */
    interface Presenter extends BasePresenter {

        /**
         * Function for set adapter for presenter
         */
        void setAdapter();

        /**
         * Handle refresh button click event
         */
        void refresh();

        /**
         * Handle logo click event
         */
        void logoClicked();

    }
}
