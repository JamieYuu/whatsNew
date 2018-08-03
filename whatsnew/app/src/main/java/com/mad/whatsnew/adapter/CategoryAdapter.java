package com.mad.whatsnew.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.whatsnew.R;

import java.util.ArrayList;

/**
 * Category adapter class, the adapter for showing categories
 * as gray circle item in news recycler view.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private ArrayList<String> mCategoryArrayList;

    /**
     * Constructor for category adapter
     * @param categoryArrayList is the list to shown
     */
    public CategoryAdapter(ArrayList<String> categoryArrayList) {
        this.mCategoryArrayList = categoryArrayList;
    }

    /**
     * View holder class, contains only a TextView to show category text
     */
    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView mCateText;

        /**
         * Constructor for category
         * @param itemView is the parent view, which is recyclerView item
         */
        public CategoryViewHolder(View itemView) {
            super(itemView);
            mCateText = (TextView) itemView.findViewById(R.id.category_item_text);
        }
    }

    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryViewHolder holder, int position) {
        String category = mCategoryArrayList.get(position);
        holder.mCateText.setText(category);
    }

    @Override
    public int getItemCount() {
        return mCategoryArrayList.size();
    }
}
