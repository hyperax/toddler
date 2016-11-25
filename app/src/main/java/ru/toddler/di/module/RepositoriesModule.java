package ru.toddler.di.module;

import dagger.Binds;
import dagger.Module;
import ru.toddler.di.scope.PerApplication;
import ru.toddler.model.repository.LocalRepository;
import ru.toddler.model.repository.LocalRepositoryImpl;

@Module
public abstract class RepositoriesModule {

    @Binds
    @PerApplication
    abstract LocalRepository provideLocalRepository(LocalRepositoryImpl localRepository);
}
