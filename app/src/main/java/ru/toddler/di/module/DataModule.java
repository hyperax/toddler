package ru.toddler.di.module;

import android.database.sqlite.SQLiteDatabase;

import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.Module;
import dagger.Provides;
import ru.toddler.di.scope.PerApplication;
import ru.toddler.model.jackson.JacksonConfigurator;
import ru.toddler.model.storage.ToddlerSQLiteOpenHelper;

@Module
public class DataModule {

    @Provides
    @PerApplication
    ObjectMapper provideMapper(){
        return new JacksonConfigurator().buildMapper();
    }

    @Provides
    @PerApplication
    SQLiteDatabase provideDatabase(ToddlerSQLiteOpenHelper sqLiteOpenHelper) {
        return sqLiteOpenHelper.getWritableDatabase();
    }
}
