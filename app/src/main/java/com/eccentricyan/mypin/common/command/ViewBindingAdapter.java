package com.eccentricyan.mypin.common.command;

import android.databinding.BindingAdapter;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.eccentricyan.mypin.common.listener.OnRcvScrollListener;

public class ViewBindingAdapter {
    @BindingAdapter({"clickCommand"})
    public static void clickCommand(View view, final ReplyCommand clickCommand) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCommand != null) {
                    clickCommand.execute();
                }
            }
        });
    }
    @BindingAdapter({"onLoadMoreCommand"})
    public static void onLoadMoreCommand(RecyclerView recyclerView, final ReplyCommand
            onLoadMoreCommand) {
        recyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                if (onLoadMoreCommand != null) {
                    onLoadMoreCommand.execute();
                }
            }

            @Override
            public int setRestItem() {
                return 20;
            }
        });
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
//            Glide.with(view.getContext()).load(SITE_URL + "/images/face_male.jpg").fitCenter().into(view);
        } else {
            Glide.with(view.getContext())
                    .load(imageUrl)
                    //                .placeholder(R.drawable.placeholder)
                    .into(view);
        }
    }

    @BindingAdapter("addOnOffsetChangedListener")
    public static void initToolbar(AppBarLayout appBarLayout, AppBarLayout
            .OnOffsetChangedListener listener) {
        appBarLayout.addOnOffsetChangedListener(listener);
    }

    @BindingAdapter({"isRefresh"})
    public static void setRefresh(SwipeRefreshLayout swipeRefreshLayout, boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SwipeRefreshLayout swipeRefreshLayout, final ReplyCommand
            onRefreshCommand) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshCommand != null) {
                    onRefreshCommand.execute();
                }
            }
        });
    }

    @BindingAdapter({"render"})
    public static void loadHtml(WebView webView, final String html) {
        if (!TextUtils.isEmpty(html)) {
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }

    @BindingAdapter("onQueryTextListener")
    public static void setOnQueryTextListener(SearchView v, SearchView.OnQueryTextListener listener) {
        v.setOnQueryTextListener(listener);
    }
}
