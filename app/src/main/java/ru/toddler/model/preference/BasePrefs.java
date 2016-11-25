package ru.toddler.model.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import ru.toddler.util.DateUtils;
import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class BasePrefs {

    private static final long PREFERENCE_CHANGES_TIMEOUT = 100;

    @NonNull
    protected final Context context;

    @NonNull
    private final SharedPreferences prefs;

    @NonNull
    private final PublishSubject<String> changesBus = PublishSubject.create();

    public BasePrefs(@NonNull Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void onPreferenceChanged(@NonNull String prefName) {
        changesBus.onNext(prefName);
    }

    @NonNull
    public Observable<Long> changes(@NonNull String prefName) {
        return changesBus.asObservable()
                .filter(changedPrefName -> changedPrefName.equalsIgnoreCase(prefName))
                .map(entity -> DateUtils.getCurrentMillis())
                .onBackpressureLatest()
                .debounce(PREFERENCE_CHANGES_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @NonNull
    protected SharedPreferences.Editor getEditor() {
        return prefs.edit();
    }

    @Nullable
    protected String getString(String key, String defaultVal) {
        return prefs.getString(key, defaultVal);
    }

    protected void putString(@NonNull String key, @Nullable String var) {
        getEditor().putString(key, var).apply();
        onPreferenceChanged(key);
    }

    @Nullable
    protected Set<String> getStringSet(@NonNull String key, Set<String> defaultVal) {
        return prefs.getStringSet(key, defaultVal);
    }

    protected void putStringSet(@NonNull String key, @Nullable Set<String> var) {
        getEditor().putStringSet(key, var).apply();
        onPreferenceChanged(key);
    }

    protected int getInt(@NonNull String key, int defaultVal) {
        return prefs.getInt(key, defaultVal);
    }

    protected void putInt(@NonNull String key, int var) {
        getEditor().putInt(key, var).apply();
        onPreferenceChanged(key);
    }

    protected long getLong(@NonNull String key, long defaultVal) {
        return prefs.getLong(key, defaultVal);
    }

    protected void putLong(@NonNull String key, long var) {
        getEditor().putLong(key, var).apply();
        onPreferenceChanged(key);
    }

    protected float getFloat(@NonNull String key, float defaultVal) {
        return prefs.getFloat(key, defaultVal);
    }

    protected void putFloat(@NonNull String key, float var) {
        getEditor().putFloat(key, var).apply();
        onPreferenceChanged(key);
    }

    protected boolean getBoolean(@NonNull String key, boolean defaultVal) {
        return prefs.getBoolean(key, defaultVal);
    }

    protected void putBoolean(@NonNull String key, boolean var) {
        getEditor().putBoolean(key, var).apply();
        onPreferenceChanged(key);
    }
}
