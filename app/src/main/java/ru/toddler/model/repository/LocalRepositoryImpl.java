package ru.toddler.model.repository;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.annimon.stream.Optional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import nl.qbusict.cupboard.CupboardFactory;
import nl.qbusict.cupboard.DatabaseCompartment;
import ru.toddler.di.scope.PerApplication;
import ru.toddler.model.storage.entity.contract.Contract;
import ru.toddler.util.DateUtils;
import ru.toddler.util.NpeUtils;

import static ru.toddler.util.SqlUtils.in;

@PerApplication
public class LocalRepositoryImpl implements LocalRepository {

    private static final int ENTITY_CHANGES_TIMEOUT = 300;

    private final SQLiteDatabase sql;
    private final DatabaseCompartment db;

    private final PublishSubject<Class<?>> changesBus = PublishSubject.create();

    @Inject
    public LocalRepositoryImpl(SQLiteDatabase database) {
        sql = database;
        db = CupboardFactory.cupboard().withDatabase(database);
    }

    private void onDataChanged(Class entityClass) {
        changesBus.onNext(entityClass);
    }

    @Override
    public <T> void put(@NonNull List<T> entities) {
        if (!NpeUtils.isEmpty(entities)) {
            db.put(entities);
            onDataChanged(NpeUtils.getComponentClass(entities));
        }
    }

    @NonNull
    @Override
    public <T> List<T> get(@NonNull Class<T> entity) {
        DatabaseCompartment.QueryBuilder<T> query = db.query(entity);
        return query.list();
    }

    @NonNull
    @Override
    public <T> Optional<T> get(@NonNull Class<T> entityClass, long id) {
        return Optional.ofNullable(db.get(entityClass, id));
    }

    @Override
    public <T> void put(@NonNull T entity) {
        db.put(entity);
        onDataChanged(entity.getClass());
    }

    @Override
    public void beginTransaction() {
        sql.beginTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        sql.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        sql.endTransaction();
    }

    @Override
    public <T> int delete(Class<T> entityClass) {
        int deletedCount = db.delete(entityClass, null);
        if (deletedCount > 0) {
            onDataChanged(entityClass);
        }
        return deletedCount;
    }

    @Override
    public <T> int delete(Class<T> entityClass, Long... ids) {
        int deletedCount = db.delete(entityClass, in(Contract.ID, ids));
        if (deletedCount > 0) {
            onDataChanged(entityClass);
        }
        return deletedCount;
    }

    @Override
    public <T> boolean delete(T entity) {
        boolean isDeleted = db.delete(entity);
        if (isDeleted) {
            onDataChanged(entity.getClass());
        }
        return isDeleted;
    }

    @Override
    public Observable<Long> changes(Class<?> entityClass) {
        return changesBus.hide()
                .filter(changedEntityClass -> entityClass == changedEntityClass)
                .map(entity -> DateUtils.getCurrentMillis())
                .debounce(ENTITY_CHANGES_TIMEOUT, TimeUnit.MILLISECONDS);
    }

}
