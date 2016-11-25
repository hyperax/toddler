package ru.toddler.view;

import android.support.annotation.Nullable;

public interface View {

    void showError(String error);

    void showInfo(String info);

    void showConfirm(String confirm);

    void showLoading(@Nullable String info);

    void hideLoading();
}
