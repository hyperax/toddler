package ru.toddler.presenter;

import android.support.annotation.NonNull;

import ru.toddler.view.View;

public interface Presenter<V extends View> {

    void bindView(@NonNull V view);

    void unbindView(@NonNull V view);

    void onFinish();
}
