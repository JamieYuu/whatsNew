package com.mad.whatsnew.resultActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mad.whatsnew.R;
import com.mad.whatsnew.util.ActivityUtils;

import static com.mad.whatsnew.searchActivity.SearchActivity.EXTRA_SEARCH_TEXT;

/**
 * Activity class for result activity
 */
public class ResultActivity extends AppCompatActivity {
    private String mSearchText;
    private ResultPresenter mResultPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mSearchText = intent.getStringExtra(EXTRA_SEARCH_TEXT);

        ResultFragment resultFragment = (ResultFragment) getSupportFragmentManager()
                .findFragmentById(R.id.result_activity_frame_layout);

        if (resultFragment == null) {
            resultFragment = ResultFragment.newInstance(mSearchText);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    resultFragment, R.id.result_activity_frame_layout);
        }

        mResultPresenter = new ResultPresenter(mSearchText, resultFragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
