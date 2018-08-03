package com.mad.whatsnew.searchActivity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Search presenter class
 */
public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View mSearchView;
    private ArrayList<String> mQuery;
    private Boolean mLoggedIn = false;

    /**
     * Constructor
     * @param query history query
     * @param searchView search fragment
     */
    public SearchPresenter(ArrayList<String> query, SearchContract.View searchView) {
        mQuery = query;
        mSearchView = searchView;
        mSearchView.setPresenter(this);

        //for login
        FirebaseAuth firebaseAuth;

        //check login
        FirebaseAuth.AuthStateListener authStateListener;
        //for login
        firebaseAuth = FirebaseAuth.getInstance();
        //check login
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    mLoggedIn = false;
                } else {
                    mLoggedIn = true;
                }
            }
        };
        //check login
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void start() {

    }

    @Override
    public void filterHistory(String keyword) {
        if (!keyword.equals("")) {
            ArrayList<String> newQuery = new ArrayList<>();
            for (int i = 0;i < mQuery.size(); i ++) {
                if (mQuery.get(i).toLowerCase().contains(keyword.toLowerCase())) {
                    newQuery.add(mQuery.get(i));
                }
            }
            mSearchView.setHistoryListAdapter(newQuery);
        } else {
            mSearchView.setHistoryListAdapter(mQuery);
        }
    }

    @Override
    public void createHistoryListAdapter() {
        mSearchView.setHistoryListAdapter(mQuery);
    }


    @Override
    public void historyListClickedEvent(int position) {
        mSearchView.getResultWithHistory(mQuery.get(position));
    }

    @Override
    public void logoClicked() {
        mSearchView.toLogoClicked(mLoggedIn);
    }
}
