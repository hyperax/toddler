package ru.toddler.di;

import dagger.Component;
import ru.toddler.ToddlerApp;
import ru.toddler.di.module.AppModule;
import ru.toddler.di.module.DataModule;
import ru.toddler.di.module.NetworkModule;
import ru.toddler.di.module.NotificationsModule;
import ru.toddler.di.module.RepositoriesModule;
import ru.toddler.di.scope.PerApplication;
import ru.toddler.view.activity.BaseActivity;
import ru.toddler.view.activity.MainActivity;
import ru.toddler.view.dialog.BaseDialogFragment;
import ru.toddler.view.fragment.BaseFragment;

@PerApplication
@Component(modules = {AppModule.class, DataModule.class, NetworkModule.class,
        RepositoriesModule.class, NotificationsModule.class})
public interface AppComponent {

    void inject(ToddlerApp toddlerApp);

    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);

    void inject(BaseDialogFragment baseDialogFragment);

    void inject(MainActivity mainActivity);
}
