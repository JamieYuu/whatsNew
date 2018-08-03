package com.mad.whatsnew.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.mad.whatsnew.R;
import com.mad.whatsnew.adapter.FavoriteAdapter;
import com.mad.whatsnew.model.Favorite;
import com.mad.whatsnew.newsActivity.NewsActivity;
import com.mad.whatsnew.util.FirebaseDBUtils;

import java.util.ArrayList;

import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_AUTHOR;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_DATE;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_LINK;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_SOURCE;
import static com.mad.whatsnew.mainActivity.MainActivity.EXTRA_TITLE;

/**
 * Adapter class for favorite news, contains a top view to display
 * information about the news, and a bottom view for delete event.
 */
public class FavoriteAdapter extends RecyclerSwipeAdapter<FavoriteAdapter.SimpleViewHolder> {
    private Context mContext;
    private ArrayList<Favorite> mFavoriteArrayList;
    private String mDeleteItemId;

    /**
     * View holder class for adapter
     */
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout mSwipeLayout;
        TextView mTitleTv;
        TextView mAuthorTv;
        TextView mDateTv;
        RelativeLayout mDelete;

        /**
         * Constructor for view holder
         * @param itemView is the view of view holder
         */
        public SimpleViewHolder(View itemView) {
            super(itemView);
            mSwipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            mTitleTv = (TextView) itemView.findViewById(R.id.swipe_title);
            mAuthorTv = (TextView) itemView.findViewById(R.id.swipe_item_author);
            mDateTv = (TextView) itemView.findViewById(R.id.swipe_item_date);
            mDelete = (RelativeLayout) itemView.findViewById(R.id.bottom_wrapper) ;
        }
    }

    /**
     * Constructor for adapter
     * @param context is the parent context
     * @param FavoriteArrayList is the data set for adapter
     */
    public FavoriteAdapter(Context context, ArrayList<Favorite> FavoriteArrayList) {
        mContext = context;
        mFavoriteArrayList = FavoriteArrayList;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final Favorite item = mFavoriteArrayList.get(position);
        viewHolder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.mSwipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemManger.isOpen(position)
                        || viewHolder.mSwipeLayout.getOpenStatus() == SwipeLayout.Status.Middle) {
                    return;
                }
                Intent intent = new Intent(mContext, NewsActivity.class);
                intent.putExtra(EXTRA_TITLE, item.getTitle());
                intent.putExtra(EXTRA_DATE, item.getDate());
                intent.putExtra(EXTRA_AUTHOR, item.getCreator());
                intent.putExtra(EXTRA_SOURCE, item.getSource());
                intent.putExtra(EXTRA_LINK, item.getLink());
                mContext.startActivity(intent);
            }
        });
        viewHolder.mSwipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
            }
        });
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteItemId = mFavoriteArrayList.get(position).getFirebaseKey();
                FirebaseDBUtils.deleteItemDb(mDeleteItemId);
            }
        });
        viewHolder.mTitleTv.setText(item.getTitle());
        viewHolder.mAuthorTv.setText(item.getCreator());
        viewHolder.mDateTv.setText(item.getDate());
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mFavoriteArrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}
