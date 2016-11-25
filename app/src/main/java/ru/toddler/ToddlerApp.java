package ru.toddler;

import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

import ru.toddler.di.AppComponent;
import ru.toddler.di.DaggerAppComponent;
import ru.toddler.di.module.AppModule;

public class ToddlerApp extends MultiDexApplication {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        buildComponent();

        appComponent.inject(this);

        initAppInspection();
    }

    private void initAppInspection() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    protected void buildComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
