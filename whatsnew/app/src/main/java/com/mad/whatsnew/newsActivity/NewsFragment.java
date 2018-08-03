package com.mad.whatsnew.newsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.mad.whatsnew.R;
import com.mad.whatsnew.loginActivity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_AUTHOR;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_DATE;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_LINK;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_TITLE;

/**
 * Fragment class for news activity
 * Is the view in MVP
 */
public class NewsFragment extends Fragment implements NewsContract.View{
    private NewsContract.Presenter mPresenter;

    @BindView(R.id.news_activity_title_tv) TextView mTitleTv;
    @BindView(R.id.news_activity_author_tv) TextView mAuthorTv;
    @BindView(R.id.news_activity_date_tv) TextView mDateTv;
    @BindView(R.id.news_activity_content_tv) TextView mContentTv;
    @BindView(R.id.news_activity_progress_bar) ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_news, container, false);
        ButterKnife.bind(this, root);

        mPresenter.getIntentContent();
        mPresenter.downloadContent();

        return root;
    }

    /**
     * Get the new instance
     * @param title in intent
     * @param date in intent
     * @param author in intent
     * @param contentLink in intent
     * @return new fragment
     */
    public static NewsFragment newInstance(String title, String date, String author, String contentLink) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_TITLE, title);
        arguments.putString(EXTRA_DATE, date);
        arguments.putString(EXTRA_AUTHOR, author);
        arguments.putString(EXTRA_LINK, contentLink);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void setPresenter(NewsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    @Override
    public void setDate(String date) {
        mDateTv.setText(date);
    }

    @Override
    public void setAuthor(String author) {
        mAuthorTv.setText(author);
    }

    @Override
    public void setContent(String content) {
        mContentTv.setText(content);
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
    public void setContentTvVisible(Boolean visible) {
        if (visible) {
            mContentTv.setVisibility(View.VISIBLE);
        } else {
            mContentTv.setVisibility(View.GONE);
        }
    }

    @Override
    public String getContent() {
        return mContentTv.getText().toString().trim();
    }

    @Override
    public String getTitleTvText() {
        return mTitleTv.getText().toString().trim();
    }

    @Override
    public void noUserFav() {
        Toast toast = Toast.makeText(getContext(), getString(R.string.login_fav_toast), Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}

