package ru.toddler.view.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Button;

import javax.inject.Inject;

import ru.toddler.di.AppComponent;
import ru.toddler.view.activity.BaseActivity;
import ru.toddler.view.behaviour.CallbackProvider;
import ru.toddler.view.notification.MenuNotification;
import ru.toddler.view.notification.Message;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseDialogFragment extends AppCompatDialogFragment {

    @NonNull
    private CompositeSubscription subscription = new CompositeSubscription();

    @Inject
    MenuNotification notification;

    public BaseDialogFragment() {
        setArguments(new Bundle()); // prevents null-pointer exception on getArguments call
    }

    protected AppComponent getAppComponent() {
        return ((BaseActivity) getActivity()).getAppComponent();
    }

    protected Object getHostParent() {
        Object hostParent = null;
        if (getParentFragment() != null) {
            hostParent = getParentFragment();
        } else if (getActivity() != null) {
            hostParent = getActivity();
        }
        return hostParent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppComponent().inject(this);
    }

    @Nullable
    protected <T> T getCallback(@NonNull Class<T> callbackClass) {
        Object hostParent = getHostParent();
        if (hostParent instanceof CallbackProvider) {
            return ((CallbackProvider) hostParent).getCallback(callbackClass);
        } else if (callbackClass.isInstance(hostParent)) {
            return callbackClass.cast(hostParent);
        }
        return null;
    }

    public boolean onBackPressed() {
        return false;
    }

    public boolean hasBackStack() {
        return false;
    }

    protected Button getButton(int which) {
        return ((AlertDialog) getDialog()).getButton(which);
    }

    protected void showToast(Message message) {
        notification.showToast(message);
    }

    protected void addSubscription(Subscription s) {
        subscription.add(s);
    }

    @Override
    public void onDestroyView() {
        subscription.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (isRemoving() || getActivity().isFinishing()) {
            onFinish();
        }
        super.onDestroy();
    }

    protected void onFinish() {
    }
}
