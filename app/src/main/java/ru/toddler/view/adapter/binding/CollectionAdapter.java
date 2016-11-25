package ru.toddler.view.adapter.binding;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ru.toddler.util.NpeUtils;

public abstract class CollectionAdapter<ITEM, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    @NonNull
    protected List<ITEM> items = Collections.emptyList();

    public abstract void updateItems(@Nullable List<ITEM> newItems);

    protected void updateList(@Nullable List<ITEM> newItems, DiffUtil.Callback diffCallback) {
        this.items = NpeUtils.getNonNull(newItems);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
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
}
