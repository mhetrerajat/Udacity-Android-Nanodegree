package com.example.rajatmhetre.popularmovies2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rajatmhetre.popularmovies2.R;
import com.example.rajatmhetre.popularmovies2.model.Review;

import java.util.ArrayList;

/**
 * Created by rajatmhetre on 21/08/15.
 */
public class ReviewAdapter extends BaseAdapter {

    public Context mContext;
    public ArrayList<Review> mData;

    public ReviewAdapter(Context c, ArrayList<Review> d){
        mContext = c;
        mData = d;
    }

    @Override
    public int getCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review review = (Review) mData.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_review, parent, false);
        }

        TextView tvAuthor = (TextView) convertView.findViewById(R.id.review_author);
        TextView tvContent = (TextView) convertView.findViewById(R.id.review_content);

        tvAuthor.setText(review.mAuthor);
        tvContent.setText(review.mContent);

        return convertView;
    }

}
