package com.eccentricyan.mypin.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by shiyanhui on 2017/04/19.
 */

public class RatioImageView extends ImageView {
    private int originalWidth;
    private int originalHeight;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOriginalHeight(int originalHeight) {
        this.originalHeight = originalHeight;
    }

    public void setOriginalWidth(int originalWidth) {
//        Log.d("originalWidth", Integer.toString(originalWidth));
        this.originalWidth = originalWidth;
    }

    public void setUrl(String url) {
        Log.d("url", url);
        Glide.with(getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (originalHeight > 0 && originalWidth > 0){
            float ratio = (float) originalWidth / (float) originalHeight;
            //调整高度
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            if (width > 0){
                height = (int)((float) width / ratio );
            }
            setMeasuredDimension(width, height);

        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}