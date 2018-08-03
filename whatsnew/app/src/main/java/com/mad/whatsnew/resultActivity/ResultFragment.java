package com.mad.whatsnew.resultActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mad.whatsnew.R;
import com.mad.whatsnew.adapter.NewsAdapter;
import com.mad.whatsnew.model.News;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Result fragment class
 * Is the view in MVP
 */
public class ResultFragment extends Fragment implements ResultContract.View{
    private ResultContract.Presenter mPresenter;

    @BindView(R.id.result_activity_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.result_activity_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.result_error_layout) LinearLayout mLinearLayout;
    @BindView(R.id.result_activity_refresh) FloatingActionButton mRefreshFab;
    @BindView(R.id.result_activity_found_tv) TextView mFoundTv;

    private final static String EXTRA_KEYWORD = "SEARCH_TEXT";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_result, container, false);
        ButterKnife.bind(this, root);

        mPresenter.setAdapter();

        mRefreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.refresh();
            }
        });

        return root;
    }

    /**
     * Function return new instance
     * @param searchText is the search keyword
     * @return new result fragment
     */
    public static ResultFragment newInstance(String searchText) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_KEYWORD, searchText);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void setPresenter(ResultContract.Presenter presenter) {
        this.mPresenter = presenter;
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
    public void setErrorTextVisible(Boolean visible) {
        if (visible) {
            mLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mLinearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setFoundTv(int number, float time) {
        mFoundTv.setText(getString(R.string.found) + " "
                + number + " "
                + getString(R.string.in) + " "
                + time + getString(R.string.seconds));
    }
}
