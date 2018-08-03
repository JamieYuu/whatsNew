package com.mad.whatsnew.favoriteActivity;

import com.mad.whatsnew.BasePresenter;
import com.mad.whatsnew.BaseView;
import com.mad.whatsnew.adapter.NewsAdapter;
import com.mad.whatsnew.mainActivity.MainContract;
import com.mad.whatsnew.model.Favorite;
import com.mad.whatsnew.model.News;

import java.util.ArrayList;

/**
 * Interface for connecting fragment and presenter of favorite activity
 */
public interface FavoriteContract {
    /**
     * View interface
     */
    interface View extends BaseView<FavoriteContract.Presenter> {

        /**
         * Set the content of favorite news recycler view content
         * @param favoriteArrayList is the data set
         */
        void setRecyclerViewContent(ArrayList<Favorite> favoriteArrayList);

        /**
         * Set the visibility of progress bar
         * @param visible is the visibility, true means visible, false means invisible
         */
        void setProgressBarVisible(Boolean visible);

        /**
         * Set the visibility of recycler view
         * @param visible is the visibility, true means visible, false means invisible
         */
        void setRecyclerViewVisible(Boolean visible);

        /**
         * Function for click user logo
         * @param loggedIn is the status of current user
         */
        void toLogoClicked(Boolean loggedIn);

        /**
         * Set the visibility of error text layout
         * @param visible is the visibility, true means visible, false means invisible
         */
        void setErrorLayoutVisible(Boolean visible);

        /**
         * Set the visibility of content layout
         * @param visible is the visibility, true means visible, false means invisible
         */
        void setContentLayoutVisible(Boolean visible);

        /**
         * Set the visibility of no item in favorite text layout
         * @param visible is the visibility, true means visible, false means invisible
         */
        void setNoItemTvVisible(Boolean visible);

    }

    /**
     * Presenter interface
     */
    interface Presenter extends BasePresenter {

        /**
         * Set adapter for recycler view
         */
        void setAdapter();

        /**
         * Refresh content of recycler view
         */
        void refresh();

        /**
         * Handle user logo click event
         */
        void logoClicked();

    }
}
