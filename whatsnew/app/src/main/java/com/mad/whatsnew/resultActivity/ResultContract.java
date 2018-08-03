package com.mad.whatsnew.resultActivity;

import com.mad.whatsnew.BasePresenter;
import com.mad.whatsnew.BaseView;
import com.mad.whatsnew.adapter.NewsAdapter;
import com.mad.whatsnew.model.News;

import java.util.ArrayList;

/**
 * Contract for view and presenter
 */
public interface ResultContract {

    /**
     * View interface
     */
    interface View extends BaseView<Presenter> {

        /**
         * Set recycler view content
         * @param newsAdapter the news adapter
         * @param newsArrayList the data set
         */
        void setRecyclerViewContent(NewsAdapter newsAdapter, ArrayList<News> newsArrayList);

        /**
         * Set visibility of progress bar
         * @param visible is the status of visibility
         */
        void setProgressBarVisible(Boolean visible);

        /**
         * Set visibility of recycler view
         * @param visible is the status of visibility
         */
        void setRecyclerViewVisible(Boolean visible);

        /**
         * Set visibility of error textview
         * @param visible is the status of visibility
         */
        void setErrorTextVisible(Boolean visible);

        /**
         * Set found textview content
         * @param number the number
         * @param time the time used
         */
        void setFoundTv(int number, float time);

    }

    /**
     * Presenter interface
     */
    interface Presenter extends BasePresenter {

        /**
         * Set up adapter
         */
        void setAdapter();

        /**
         * Handle refresh event
         */
        void refresh();

    }

}
