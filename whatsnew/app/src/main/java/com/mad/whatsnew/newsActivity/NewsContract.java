package com.mad.whatsnew.newsActivity;

import android.speech.tts.TextToSpeech;

import com.google.firebase.auth.FirebaseUser;
import com.mad.whatsnew.BasePresenter;
import com.mad.whatsnew.BaseView;

/**
 * Contract class for news activity
 * define view and presenter
 */
public interface NewsContract {
    /**
     * View interface
     */
    interface View extends BaseView<Presenter> {

        /**
         * Set title textview text
         * @param title the text to be set
         */
        void setTitle(String title);

        /**
         * Set date textview test
         * @param date the date to be set
         */
        void setDate(String date);

        /**
         * Set author textview text
         * @param author the author to be set
         */
        void setAuthor(String author);

        /**
         * Set content text
         * @param content the content to be set
         */
        void setContent(String content);

        /**
         * Set visibility of progress bar
         * @param visible is the status of visibility
         */
        void setProgressBarVisible(Boolean visible);

        /**
         * Set visibility of content textview
         * @param visible is the status of visibility
         */
        void setContentTvVisible(Boolean visible);

        /**
         * Get the content of content textview
         * @return is the content textview text
         */
        String getContent();

        /**
         * Get the title text
         * @return the text of title
         */
        String getTitleTvText();

        /**
         * If user is null
         */
        void noUserFav();
    }

    /**
     * Presenter interface
     */
    interface Presenter extends BasePresenter {

        /**
         * Get intent content
         */
        void getIntentContent();

        /**
         * Download content;
         */
        void downloadContent();

        /**
         * Read out text
         * @param tts is the texttospeech object
         */
        void speechContent(TextToSpeech tts);

        /**
         * Handle fav fab click event
         */
        void addToFavorite();
    }

}

