package com.methodica.lizalinto.dummyinbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;
import com.methodica.lizalinto.dummyinbox.NewsFragment.OnNewsFragmentInteractionListener;
import com.methodica.lizalinto.dummyinbox.NewsContent.NewsItem;
/**
 * Created by lizalinto on 4/6/16.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {

    private final Map<String, NewsContent.NewsItem> mDataSet;
    private final OnNewsFragmentInteractionListener mListener;

    public NewsRecyclerViewAdapter(Map<String, NewsContent.NewsItem> dataSet,OnNewsFragmentInteractionListener listener) {
        mDataSet = dataSet;
        mListener = listener;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        private final TextView mTitleView;
        private final TextView mPublishedDateView;
        private final TextView mPublishedView;
        private final TextView mContentView;
        public ImageView mImageView;
        public NewsItem mItem;


        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mTitleView = (TextView) itemView.findViewById(R.id.title);
            mContentView = (TextView) itemView.findViewById(R.id.content);
            mPublishedView = (TextView) itemView.findViewById(R.id.publisher);
            mPublishedDateView = (TextView) itemView.findViewById(R.id.published_date);
            mImageView = (ImageView) itemView.findViewById(R.id.image);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String index = String.valueOf(position+1);

        holder.mItem = mDataSet.get(index);

        holder.mTitleView.setText(Html.fromHtml(mDataSet.get(index).title));

        holder.mContentView.setText(Html.fromHtml(mDataSet.get(index).content));

        holder.mPublishedView.setText(mDataSet.get(index).publisher);

        holder.mPublishedDateView.setText(mDataSet.get(index).publishedDate);

       // holder.mImageView.setImageResource(R.drawable.flower1);

        holder.mImageView.setImageBitmap(mDataSet.get(index).imageBM);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.OnNewsFragmentInteraction(holder.mItem);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
