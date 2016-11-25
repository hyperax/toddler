package ru.toddler.view;

import android.support.annotation.NonNull;

import ru.toddler.view.model.ToddlerVM;

public interface ToddlerView extends View {

    void showToddler(@NonNull ToddlerVM toddlerVM);

}
