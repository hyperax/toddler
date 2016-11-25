package ru.toddler.view.adapter;

import android.support.v7.util.DiffUtil;

import java.util.List;

public abstract class DiffCallback<ITEM> extends DiffUtil.Callback {
    protected List<ITEM> newItems;
    protected List<ITEM> oldItems;

    public DiffCallback(List<ITEM> newItems, List<ITEM> oldItems) {
        this.newItems = newItems;
        this.oldItems = oldItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public abstract boolean areItemsTheSame(int oldItemPosition, int newItemPosition);

    @Override
    public abstract boolean areContentsTheSame(int oldItemPosition, int newItemPosition);
}
