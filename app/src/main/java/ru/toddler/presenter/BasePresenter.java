package ru.toddler.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.annimon.stream.Optional;

import java.lang.ref.WeakReference;

import ru.toddler.R;
import ru.toddler.model.exception.ToddlerException;
import ru.toddler.util.NpeUtils;
import ru.toddler.view.View;
import timber.log.Timber;

public abstract class BasePresenter<V extends View> implements Presenter<V> {

    private final Context context;

    private WeakReference<V> view = new WeakReference<>(null);

    public BasePresenter(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void bindView(@NonNull V view) {
        this.view.clear();
        this.view = new WeakReference<>(view);
    }

    @Override
    public void unbindView(@NonNull V view) {
        if (NpeUtils.equals(this.view.get(), view)) {
            this.view.clear();
        }
    }

    protected void showLoadingState(String info) {
        getView().ifPresent(v -> v.showLoading(info));
    }

    protected void hideLoadingState() {
        getView().ifPresent(View::hideLoading);
    }

    protected Optional<V> getView() {
        return Optional.ofNullable(view.get());
    }

    protected void handleError(Throwable throwable) {
        if (throwable != null) {
            String mes;
            if (throwable instanceof ToddlerException) {
                ToddlerException te = (ToddlerException) throwable;
                mes = getString(R.string.pattern_definition, te.getTitle(), te.getDetails());
            } else {
                String details = NpeUtils.isEmpty(throwable.getMessage()) ? throwable.toString() : throwable.getMessage();
                mes = getString(R.string.error_unknown_with_description, details);
            }
            showAlert(mes);

            Timber.e(throwable, mes);
        }
    }

    protected void showAlert(String error) {
        getView().ifPresent(v -> v.showError(error));
    }

    protected void showInfo(String info) {
        getView().ifPresent(v -> v.showInfo(info));
    }

    protected void showConfirm(String confirm) {
        getView().ifPresent(v -> v.showConfirm(confirm));
    }

    @NonNull
    public final String getString(@StringRes int resId) {
        return context.getString(resId);
    }

    @NonNull
    public final String getString(@StringRes int resId, Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }

    @Override
    public void onFinish() {
    }
}
