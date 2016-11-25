package ru.toddler.view.adapter.binding;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class BindingViewHolder<BINDING extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private final BINDING layoutBinding;

    public BindingViewHolder(BINDING layoutBinding) {
        super(layoutBinding.getRoot());
        this.layoutBinding = layoutBinding;
    }

    public BINDING getBinding() {
        return layoutBinding;
    }
}
