package com.mad.whatsnew.favoriteActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.mad.whatsnew.adapter.FavoriteAdapter;
import com.mad.whatsnew.R;
import com.mad.whatsnew.adapter.FavoriteAdapter;
import com.mad.whatsnew.loginActivity.LoginActivity;
import com.mad.whatsnew.model.Favorite;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment class for favorite class
 * Is the View in MVP
 */
public class FavoriteFragment extends Fragment implements FavoriteContract.View {
    private FavoriteContract.Presenter mPresenter;

    @BindView(R.id.favorite_activity_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.favorite_activity_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.favorite_activity_refresh_fab) FloatingActionButton mRefreshFab;
    @BindView(R.id.favorite_activity_error_Layout) LinearLayout mErrorLayout;
    @BindView(R.id.favorite_activity_content) RelativeLayout mContentLayout;
    @BindView(R.id.favorite_activity_noitem_tv) TextView mNoItemTv;
    @BindView(R.id.favorite_activity_login_btn) Button mLoginBtn;

    private final static String LOGO_TAG = "logo";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_favorite, container, false);
        ButterKnife.bind(this, root);

        mRefreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.refresh();
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mPresenter.setAdapter();

        return root;
    }

    /**
     * New instance function, for if there are no fragment manager
     * @return the new fragment
     */
    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void setPresenter(FavoriteContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setRecyclerViewContent(ArrayList<Favorite> favoriteArrayList) {
        RecyclerView.Adapter adapter;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FavoriteAdapter(getContext(), favoriteArrayList);
        ((FavoriteAdapter) adapter).setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(adapter);
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
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
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
    public void setErrorLayoutVisible(Boolean visible) {
        if (visible) {
            mErrorLayout.setVisibility(View.VISIBLE);
        } else {
            mErrorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setContentLayoutVisible(Boolean visible) {
        if (visible) {
            mContentLayout.setVisibility(View.VISIBLE);
        } else {
            mContentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setNoItemTvVisible(Boolean visible) {
        if (visible) {
            mNoItemTv.setVisibility(View.VISIBLE);
        } else {
            mNoItemTv.setVisibility(View.GONE);
        }
    }
}
