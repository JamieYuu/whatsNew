package com.mad.whatsnew.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.mad.whatsnew.R;
import com.mad.whatsnew.model.CustomLink;
import com.mad.whatsnew.util.FirebaseDBUtils;

import java.util.ArrayList;

/**
 * Adapter class for custom favorite rss resource links
 * Contains a top view to show the content
 * and a bottom view to contain delete button
 * When user swipe list to left, the bottom view will be shown
 */
public class CustomLinkAdapter extends RecyclerSwipeAdapter<CustomLinkAdapter.SimpleViewHolder> {

    private Context mContext;
    private ArrayList<CustomLink> mCustomLinkArrayList;
    private String mDeleteItemId;

    /**
     * View holder class, contains two TextView for top view
     * and a RelativeLayout for bottom view
     */
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout mSwipeLayout;
        TextView mTitleTv;
        TextView mLinkTv;
        RelativeLayout mDelete;

        /**
         * Constructor for view holder, define contents
         * @param itemView is the view of item
         */
        public SimpleViewHolder(View itemView) {
            super(itemView);
            mSwipeLayout = (SwipeLayout) itemView.findViewById(R.id.link_swipe);
            mTitleTv = (TextView) itemView.findViewById(R.id.link_swipe_title);
            mLinkTv = (TextView) itemView.findViewById(R.id.link_swipe_link);
            mDelete = (RelativeLayout) itemView.findViewById(R.id.link_bottom_wrapper);
        }
    }

    /**
     * Constructor for CustomLinkAdapter
     * @param context is the parent context
     * @param customLinkArrayList is the data set for adapter
     */
    public CustomLinkAdapter(Context context, ArrayList<CustomLink> customLinkArrayList) {
        mContext = context;
        mCustomLinkArrayList = customLinkArrayList;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_link_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        CustomLink item = mCustomLinkArrayList.get(position);
        viewHolder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.mSwipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
            }
        });
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteItemId = mCustomLinkArrayList.get(position).getFirebaseKey();
                FirebaseDBUtils.deleteCusLinkDb(mDeleteItemId);
            }
        });
        viewHolder.mTitleTv.setText(item.getTitle());
        viewHolder.mLinkTv.setText(item.getLink());
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mCustomLinkArrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.link_swipe;
    }
}
