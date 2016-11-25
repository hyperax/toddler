package ru.toddler.view.adapter.binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class BindingAdapter<BINDING extends ViewDataBinding> extends RecyclerView.Adapter<BindingViewHolder<BINDING>> {

    private final int layoutResourceId;

    public BindingAdapter(@LayoutRes int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public BindingViewHolder<BINDING> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BINDING binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResourceId, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<BINDING> holder, int position) {
        BINDING binding = holder.getBinding();
        updateBinding(binding, position);
    }

    protected abstract void updateBinding(@NonNull BINDING binding, int position);

}
