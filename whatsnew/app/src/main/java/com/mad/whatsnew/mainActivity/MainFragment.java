package com.mad.whatsnew.mainActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mad.whatsnew.R;
import com.mad.whatsnew.adapter.NewsAdapter;
import com.mad.whatsnew.loginActivity.LoginActivity;
import com.mad.whatsnew.model.News;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The fragment class for main activity
 * Is the view in MVP
 */
public class MainFragment extends Fragment implements MainContract.View {
    private MainContract.Presenter mPresenter;

    @BindView(R.id.main_activity_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.main_activity_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.main_activity_refresh_fab) FloatingActionButton mRefreshFab;
    @BindView(R.id.main_activity_found_tv) TextView mFoundTv;
    @BindView(R.id.nointernet_Tv) TextView mNoInternetTv;

    private final static String INTERNET_TAG = "internet";
    private final static String LOGO_TAG = "logo";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_main, container, false);
        ButterKnife.bind(this, root);

        mRefreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.refresh();
            }
        });

        mPresenter.setAdapter();

        if (isNetworkConnected()) {
            Log.e(INTERNET_TAG, "connected");
        } else {
            Log.e(INTERNET_TAG, "not connected");
            mNoInternetTv.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mRefreshFab.setVisibility(View.GONE);
            mFoundTv.setVisibility(View.GONE);
        }

        return root;
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setRecyclerViewContent(NewsAdapter newsAdapter, ArrayList<News> newsArrayList) {
        newsAdapter = new NewsAdapter(getActivity(), newsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setProgressBarVisible(Boolean visible) {
        if (visible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void setRecyclerViewVisible(Boolean visible) {
        if (visible) {
            mFoundTv.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mFoundTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void toLogoClicked(Boolean loggedIn) {
        if (loggedIn) {
            Log.e(LOGO_TAG, "logged in");
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void setFoundTv(int number, float time) {
        mFoundTv.setText(getString(R.string.found) + " "
                + number + " "
                + getString(R.string.in) + " "
                + time + getString(R.string.seconds));
    }

    /**
     * Function to check network connection
     * @return is the status of network connection
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
