package ru.toddler.di.module;

import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import ru.toddler.di.scope.PerApplication;

@Module
public class AppModule {

    private final Context context;

    public AppModule(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @PerApplication
    Context provideAppContext(){
        return context;
    }
}
