package ru.toddler.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import ru.toddler.presenter.Presenter;
import ru.toddler.util.NpeUtils;
import ru.toddler.view.View;
import ru.toddler.view.dialog.SpinnerProgressDialog;
import ru.toddler.view.notification.Message;

public abstract class PresenterActivity<P extends Presenter<V>, V extends View> extends BaseActivity
        implements View {

    @NonNull
    protected abstract P getPresenter();

    @NonNull
    protected abstract V fetchView();

    private SpinnerProgressDialog dialog;

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().bindView(fetchView());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
        NpeUtils.call(dialog, DialogFragment::dismissAllowingStateLoss);
        dialog = SpinnerProgressDialog.newInstance(info, false);
        showDialog(dialog);
    }

    @Override
    public void hideLoading() {
        NpeUtils.call(dialog, DialogFragment::dismissAllowingStateLoss);
        dialog = null;
    }

    @Override
    protected void onFinish() {
        NpeUtils.call(getPresenter(), Presenter::onFinish);
        super.onFinish();
    }
}
