package com.mad.whatsnew.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * ActivityUtils for reusable class
 */
public class ActivityUtils {

    /**
     * Add fragment to activity
     * @param fragmentManager the fragment manager
     * @param fragment the fragment
     * @param framId the frame layout id
     */
    public static void addFragmentToActivity (FragmentManager fragmentManager, Fragment fragment, int framId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(framId, fragment);
        transaction.commit();
    }
}
