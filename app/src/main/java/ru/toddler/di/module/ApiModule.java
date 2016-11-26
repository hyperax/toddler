package ru.toddler.di.module;

import dagger.Binds;
import dagger.Module;
import ru.toddler.di.scope.PerApplication;
import ru.toddler.model.network.ApiProvider;
import ru.toddler.model.network.RoutingApiImpl;

@Module
public abstract class ApiModule {

    @Binds
    @PerApplication
    abstract ApiProvider bindNetworkApi(RoutingApiImpl networkApiImpl);

}
