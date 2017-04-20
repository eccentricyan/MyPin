package com.eccentricyan.mypin.presentation.card;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.eccentricyan.mypin.R;
import com.eccentricyan.mypin.databinding.ListItemCardBinding;
import com.eccentricyan.mypin.di.component.ActivityComponent;
import com.eccentricyan.mypin.domain.entities.Card;
import com.pinterest.android.pdk.PDKPin;

import java.util.Collections;
import java.util.List;

/**
 * Created by shiyanhui on 2017/04/19.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private ActivityComponent component;
    private List<PDKPin> pins;


    public CardListAdapter(ActivityComponent component) {
        this.component = component;
        this.pins = Collections.emptyList();
    }

    public void setPins(List<PDKPin> pins, boolean refresh) {
        if (refresh || this.pins.size() <= 0) {
            this.pins = pins;
        } else {
            this.pins.addAll(pins);
        }

    }

    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemCardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_card,
                parent,
                false
        );
        return new CardListAdapter.ViewHolder(binding, component);
    }

    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, int position) {
        holder.bind(pins.get(position));
    }

    @Override
    public int getItemCount() {
        return pins.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ListItemCardBinding binding;
        final ActivityComponent component;

        ViewHolder(ListItemCardBinding binding, ActivityComponent component) {
            super(binding.getRoot());
            this.binding = binding;
            this.component = component;
        }

        void bind(PDKPin pin) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new CardViewModel(component, pin));
            } else {
                binding.getViewModel().setCard(pin);
            }
            binding.executePendingBindings();
        }
    }
}