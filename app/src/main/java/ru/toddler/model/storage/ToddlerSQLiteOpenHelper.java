package ru.toddler.model.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;

import javax.inject.Inject;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;
import ru.toddler.di.scope.PerApplication;
import ru.toddler.model.storage.entity.contract.Contract;
import ru.toddler.model.storage.converter.BigDecimalFieldConverter;
import ru.toddler.model.storage.converter.LongArrayFieldConverter;
import ru.toddler.model.storage.converter.StringArrayFieldConverter;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

@PerApplication
public class ToddlerSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_FILE = "toddler.db";
    private static final int DB_VERSION = 1;

    static {
        Cupboard cupboard = new CupboardBuilder()
                .useAnnotations()
                .registerFieldConverter(long[].class, new LongArrayFieldConverter())
                .registerFieldConverter(String[].class, new StringArrayFieldConverter())
                .registerFieldConverter(BigDecimal.class, new BigDecimalFieldConverter())
                .build();

        for (Class clazz : Contract.ENTITIES) {
            cupboard.register(clazz);
        }

        CupboardFactory.setCupboard(cupboard);
    }

    @Inject
    public ToddlerSQLiteOpenHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

}
