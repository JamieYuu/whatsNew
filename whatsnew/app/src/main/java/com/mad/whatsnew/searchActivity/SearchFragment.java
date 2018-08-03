package com.mad.whatsnew.searchActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mad.whatsnew.R;
import com.mad.whatsnew.loginActivity.LoginActivity;
import com.mad.whatsnew.resultActivity.ResultActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.mad.whatsnew.searchActivity.SearchActivity.EXTRA_SEARCH_TEXT;
import static com.mad.whatsnew.searchActivity.SearchActivity.PREFER_HISTORY;
import static com.mad.whatsnew.searchActivity.SearchActivity.PREFER_KEY;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Search fragment class
 * Is the view in MVP
 */
public class SearchFragment extends Fragment implements SearchContract.View{
    private SearchContract.Presenter mPresenter;

    @BindView(R.id.history_listView) ListView mHistoryListView;
    @BindView(R.id.search_activity_searchBar) SearchView mSearchView;
    @BindView(R.id.nointernet_Tv) TextView mNoInternetTv;
    @BindView(R.id.search_activity_history_layout) LinearLayout mHistoryLayout;

    private static final String ARGUMENT_QUERY_ID = "QUERY_ID";
    private static final String INTERNET_TAG = "internet";

    /**
     * New instance class
     * @param query history query
     * @return is the new fragment
     */
    public static SearchFragment newInstance(ArrayList<String> query) {
        Bundle arguments = new Bundle();
        arguments.putStringArrayList(ARGUMENT_QUERY_ID, query);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(arguments);
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_search, container, false);
        ButterKnife.bind(this, root);

        if (isNetworkConnected()) {
            Log.e(INTERNET_TAG, "connected");
        } else {
            Log.e(INTERNET_TAG, "not connected");
            mNoInternetTv.setVisibility(View.VISIBLE);
            mSearchView.setVisibility(View.GONE);
            mHistoryLayout.setVisibility(View.GONE);
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    SharedPreferences settings = getContext().getSharedPreferences(PREFER_KEY, MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    Set<String> historyQuery = settings.getStringSet(PREFER_HISTORY, null);
                    if (historyQuery == null) {
                        historyQuery = new HashSet<>();
                    }
                    historyQuery.add(query);
                    editor.putStringSet(PREFER_HISTORY, historyQuery);
                    editor.commit();

                    query = query.trim();
                    Intent intent = new Intent(getActivity(), ResultActivity.class);
                    intent.putExtra(EXTRA_SEARCH_TEXT, query);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.filterHistory(newText);
                return false;
            }
        });

        mPresenter.createHistoryListAdapter();

        mHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.historyListClickedEvent(position);
            }
        });

        return root;
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setHistoryListAdapter(ArrayList<String> query) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, query);
        mHistoryListView.setAdapter(arrayAdapter);
    }

    @Override
    public void getResultWithHistory(String keyword) {
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra(EXTRA_SEARCH_TEXT, keyword);
        startActivity(intent);
    }

    @Override
    public void toLogoClicked(Boolean loggedIn) {
        if (loggedIn) {
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Check internet
     * @return is the status of network
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
