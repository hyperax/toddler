package ru.toddler.model.repository;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;

import java.util.List;

import io.reactivex.Observable;


public interface LocalRepository {

    @NonNull
    <T> List<T> get(@NonNull Class<T> entity);

    @NonNull
    <T> Optional<T> get(@NonNull Class<T> entity, long id);

    <T> void put(@NonNull T entity);

    <T> void put(@NonNull List<T> entity);

    void beginTransaction();

    void setTransactionSuccessful();

    void endTransaction();

    <T> int delete(Class<T> entity);

    <T> int delete(Class<T> entity, Long... ids);

    <T> boolean delete(T entity);

    Observable<Long> changes(Class<?> entityClass);
}
