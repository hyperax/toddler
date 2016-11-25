package ru.toddler.view.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import ru.toddler.presenter.Presenter;
import ru.toddler.util.NpeUtils;
import ru.toddler.view.View;
import ru.toddler.view.notification.Message;

public abstract class PresenterDialog<P extends Presenter<V>, V extends View> extends BaseDialogFragment
        implements View {

    @NonNull
    protected abstract P getPresenter();

    @NonNull
    protected abstract V fetchView();

    private SpinnerProgressDialog dialog;

    public PresenterDialog() {
        setArguments(new Bundle()); // prevent NPE when getting args inside fragment's lifecycle
    }

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
        NpeUtils.call(dialog, DialogFragment::dismissAllowingStateLoss);
        dialog = SpinnerProgressDialog.newInstance(info, false);
        dialog.show(getChildFragmentManager(), SpinnerProgressDialog.class.getSimpleName());
    }

    @Override
    public void hideLoading() {
        NpeUtils.call(dialog, DialogFragment::dismissAllowingStateLoss);
        dialog = null;
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        NpeUtils.call(getPresenter(), Presenter::onFinish);
    }
}
