package ru.toddler.util;

import rx.Observable;
import rx.functions.Action0;

public class RxUtils {
    private RxUtils() {
    }

    public static <T> Observable.Transformer<T, T> doAfterCompleteOrError(Action0 action) {
        return observable -> observable
                .doOnCompleted(action)
                .doOnError(e->action.call());
    }
}
