package com.eccentricyan.mypin.presentation.card;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eccentricyan.mypin.R;
import com.eccentricyan.mypin.common.utils.NetUtils;
import com.eccentricyan.mypin.databinding.FragmentCardListBinding;
import com.eccentricyan.mypin.presentation.base.BaseFragment;
import com.pinterest.android.pdk.PDKPin;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by shiyanhui on 2017/04/19.
 */

public class CardListFragment extends BaseFragment {
    private FragmentCardListBinding binding;
    private CardListViewModel viewModel;
    private String topicId;
    boolean isLoad = false;
    List<PDKPin> cards;
    boolean refresh;

    public static CardListFragment newInstance() {
        CardListFragment fragment = new CardListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_list, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (!NetUtils.checkNet(this.getContext())) {
            Toast.makeText(this.getContext(), "ネットに繋がっていません", Toast.LENGTH_LONG).show();
        } else {
            viewModel = new CardListViewModel(component);
            binding.setViewModel(viewModel);

            setupRecyclerView(binding.recyclerViewRepos);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        CardListAdapter adapter = new CardListAdapter(component);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        setSwipeRefreshLayout();
        setupAdapterRepositories(adapter);
    }

    private void setupAdapterRepositories(CardListAdapter adapter) {
        if (cards != null) {
            adapter.setPins(cards, refresh);
            adapter.notifyDataSetChanged();
        }
    }

    private void setSwipeRefreshLayout() {
        //设置首次运行进度条刷新
        binding.swipeRefreshLayout.setProgressViewOffset(false,
                0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        getResources().getDisplayMetrics()));
        //设置进度条颜色
        binding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoad) {
            isLoad = true;
            viewModel = binding.getViewModel();
            if (viewModel != null) viewModel.onRefreshCommand.execute();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCardsChange(CardsChangeEvent event) {
        this.cards = event.pins;
        this.refresh = event.refresh;
        CardListAdapter adapter = (CardListAdapter) binding.recyclerViewRepos.getAdapter();
        setupAdapterRepositories(adapter);
    }
}