package ru.toddler.view.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ToddlerVM implements Parcelable {

    @NonNull
    public abstract long id();

    @NonNull
    public abstract String name();

    public static Builder builder() {
        return new AutoValue_ToddlerVM.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long value);

        public abstract Builder name(@NonNull String value);

        public abstract ToddlerVM build();

    }
}