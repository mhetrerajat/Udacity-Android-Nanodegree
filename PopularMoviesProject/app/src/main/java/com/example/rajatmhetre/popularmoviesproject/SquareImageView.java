package com.example.rajatmhetre.popularmoviesproject;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by rajatmhetre on 16/06/15.
 */
public class SquareImageView extends ImageView {
    public SquareImageView(Context c){
        super(c);
    }

    public SquareImageView(Context c, AttributeSet attrs){
        super(c,attrs);
    }

    public SquareImageView(Context c,AttributeSet attrs, int defStyle){
        super(c,attrs,defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight());
    }
}
