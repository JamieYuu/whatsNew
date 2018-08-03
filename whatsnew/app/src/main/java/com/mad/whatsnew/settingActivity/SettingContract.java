package com.mad.whatsnew.settingActivity;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;
import com.mad.whatsnew.BasePresenter;
import com.mad.whatsnew.BaseView;
import com.mad.whatsnew.model.CustomLink;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Contract for view and presenter
 */
public interface SettingContract {

    /**
     * View interface
     */
    interface View extends BaseView<Presenter> {

        /**
         * Set recycler view content
         * @param customLinks is the custom links
         */
        void setRecyclerViewContent(ArrayList<CustomLink> customLinks);

        /**
         * Set visiblity of error layout
         * @param visible is the status of visibility
         */
        void setErrorLayoutVisible(Boolean visible);

        /**
         * Set visiblity of content layout
         * @param visible is the status of visibility
         */
        void setContentLayoutVisible(Boolean visible);

        /**
         * Set visiblity of no item layout
         * @param visible is the status of visibility
         */
        void setNoItemLayoutVisible(Boolean visible);

        /**
         * Set visiblity of recycler view
         * @param visible is the status of visibility
         */
        void setRecyclerViewVisible(Boolean visible);

        /**
         * Set user email
         * @param userEmail the text
         */
        void setUserEmail(String userEmail);

        /**
         * Set user image uri
         * @param imageUri image uri
         * @throws IOException error
         */
        void setUserImage(Uri imageUri) throws IOException;

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
         * Set adapter
         */
        void setAdapter();

        /**
         * Add new link
         * @param title link title
         * @param link link url
         */
        void addNewCusLink(String title, String link);

        /**
         * Update logo
         * @param filePath the image uri
         */
        void updateLogo(Uri filePath);

        /**
         * Handle logo click event
         */
        void logoClicked();

    }
}
