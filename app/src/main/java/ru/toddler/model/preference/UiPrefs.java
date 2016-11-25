package ru.toddler.model.preference;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

import ru.toddler.BuildConfig;
import ru.toddler.di.scope.PerApplication;

@PerApplication
public class UiPrefs extends BasePrefs {

    @IntDef({ProductsViewType.UNDEFINED, ProductsViewType.EXPAND_LIST, ProductsViewType.FLAT_LIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProductsViewType {
        int UNDEFINED = -1;
        int EXPAND_LIST = 0;
        int FLAT_LIST = 1;
    }

    @Inject
    public UiPrefs(Context context) {
        super(context);
    }

    public void setFirstRun(boolean flag) {
        putBoolean(PrefKeys.Ui.FIRST_RUN, flag);
    }

    public boolean firstRun() {
        return getBoolean(PrefKeys.Ui.FIRST_RUN, true);
    }

    @NonNull
    public String appVersionName() {
        return BuildConfig.VERSION_NAME;
    }
}
