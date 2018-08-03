package com.mad.whatsnew.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mad.whatsnew.newsActivity.NewsActivity;
import com.mad.whatsnew.R;
import com.mad.whatsnew.model.News;

import java.util.ArrayList;

import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_AUTHOR;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_DATE;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_LINK;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_SOURCE;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_TITLE;

/**
 * Adapter class for news class, will be used in recycler view to show
 * information of the news
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private ArrayList<News> mNewsArrayList;
    private ArrayList<String> mCategoryArrayList;
    public CategoryAdapter mCategoryAdapter;
    public ViewGroup mParent;
    private Context mContext;

    /**
     * Constructor of the adapter
     * @param context is the parent context
     * @param newsArrayList is the data set for adapter
     */
    public NewsAdapter(Context context, ArrayList<News> newsArrayList) {
        this.mNewsArrayList = newsArrayList;
        this.mContext = context;
    }

    /**
     * View holder class for adapter, it displays some information about news
     */
    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTv, mDescriptionTv, mDateTv, mAuthorTv, mSourceTv, mUrlTv;
        public LinearLayout mLinearLayout;
        public RecyclerView mCateRecyclerView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.news_item_title);
            mDescriptionTv = (TextView) itemView.findViewById(R.id.news_item_description);
            mDateTv = (TextView) itemView.findViewById(R.id.news_item_date);
            mAuthorTv = (TextView) itemView.findViewById(R.id.news_item_author);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.news_item_linear_layout);
            mCateRecyclerView = (RecyclerView) itemView.findViewById(R.id.news_item_recycler_view);

            mSourceTv = (TextView) itemView.findViewById(R.id.news_item_source_tv);
            mUrlTv = (TextView) itemView.findViewById(R.id.news_item_url_tv);

            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewsActivity.class);
                    intent.putExtra(EXTRA_TITLE, mTitleTv.getText());
                    intent.putExtra(EXTRA_DATE, mDateTv.getText());
                    intent.putExtra(EXTRA_AUTHOR, mAuthorTv.getText());
                    intent.putExtra(EXTRA_SOURCE, mSourceTv.getText());
                    intent.putExtra(EXTRA_LINK, mUrlTv.getText());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        mParent = parent;
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = mNewsArrayList.get(position);

        holder.mTitleTv.setText(news.getTitle());
        holder.mDescriptionTv.setText(news.getDescription());
        holder.mDateTv.setText(news.getDate());
        holder.mAuthorTv.setText(news.getCreator());
        holder.mSourceTv.setText(news.getSource());
        holder.mUrlTv.setText(news.getLink());

        mCategoryArrayList = news.getCategories();
        mCategoryAdapter = new CategoryAdapter(mCategoryArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mParent.getContext()
                , LinearLayoutManager.HORIZONTAL, false);
        holder.mCateRecyclerView.setLayoutManager(layoutManager);
        holder.mCateRecyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.mCateRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mNewsArrayList.size();
    }
}
