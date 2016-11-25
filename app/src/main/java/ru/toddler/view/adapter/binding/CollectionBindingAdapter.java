package ru.toddler.view.adapter.binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.toddler.util.NpeUtils;

public abstract class CollectionBindingAdapter<ITEM, BINDING extends ViewDataBinding> extends CollectionAdapter<ITEM, BindingViewHolder<BINDING>> {

    private final int layoutResourceId;

    public CollectionBindingAdapter(@LayoutRes int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
    }

    @NonNull
    public List<ITEM> getItems() {
        return NpeUtils.getNonNull(items);
    }

    public ITEM getItem(int position) {
        return getItems().get(position);
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    @Override
    public BindingViewHolder<BINDING> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BINDING binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResourceId, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<BINDING> holder, int position) {
        BINDING binding = holder.getBinding();
        updateBinding(binding, getItem(position), position);
    }

    protected abstract void updateBinding(@NonNull BINDING binding, @NonNull ITEM item, int position);
}
