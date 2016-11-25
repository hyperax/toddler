package ru.toddler.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.toddler.presenter.Presenter;
import ru.toddler.util.NpeUtils;
import ru.toddler.view.View;
import ru.toddler.view.dialog.SpinnerProgressDialog;
import ru.toddler.view.notification.Message;

public abstract class PresenterFragment<P extends Presenter<V>, V extends View> extends BaseFragment
        implements View {

    private SpinnerProgressDialog dialog;

    public PresenterFragment() {
        setArguments(new Bundle()); // prevent NPE when getting args inside fragment's lifecycle
    }

    @NonNull
    protected abstract P getPresenter();

    @NonNull
    protected abstract V fetchView();

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().bindView(fetchView());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hideLoading();
    }

    @Override
    public void onStop() {
        getPresenter().unbindView(fetchView());
        super.onStop();
    }

    @Override
    public void showError(String error) {
        showToast(Message.alert(error).setDuration(Message.LONG));
    }

    @Override
    public void showInfo(String info) {
        showToast(Message.info(info));
    }

    @Override
    public void showConfirm(String confirm) {
        showToast(Message.confirm(confirm));
    }

    @Override
    public void showLoading(@Nullable String info) {
        NpeUtils.call(dialog, SpinnerProgressDialog::dismissAllowingStateLoss);
        dialog = SpinnerProgressDialog.newInstance(info, false);
        showDialog(dialog);
    }

    @Override
    public void hideLoading() {
        NpeUtils.call(dialog, SpinnerProgressDialog::dismissAllowingStateLoss);
        dialog = null;
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        NpeUtils.call(getPresenter(), Presenter::onFinish);
    }
}
