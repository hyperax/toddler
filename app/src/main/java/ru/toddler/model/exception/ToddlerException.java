package ru.toddler.model.exception;

import android.support.annotation.NonNull;

import com.android.annotations.Nullable;

import ru.toddler.util.NpeUtils;

public class ToddlerException extends RuntimeException {

    @Nullable
    private final String title;

    @Nullable
    private final String details;

    public ToddlerException(@Nullable String title, @Nullable String details) {
        super(title + ": " + details);
        this.title = title;
        this.details = details;
    }

    @NonNull
    public String getTitle() {
        return NpeUtils.getNonNull(title);
    }

    @NonNull
    public String getDetails() {
        return NpeUtils.getNonNull(details);
    }
}
