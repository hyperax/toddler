package ru.toddler.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import javax.inject.Inject;

import ru.toddler.di.scope.PerApplication;
import ru.toddler.model.entity.ToddlerEntity;
import ru.toddler.model.preference.UiPrefs;
import ru.toddler.model.repository.LocalRepository;
import ru.toddler.view.ToddlerView;
import ru.toddler.view.model.ToddlerVM;

@PerApplication
public class ToddlerPresenter extends BasePresenter<ToddlerView> {

    @NonNull
    private final UiPrefs uiPrefs;

    @NonNull
    private final LocalRepository localRepo;

    @Inject
    public ToddlerPresenter(@NonNull Context context,
                            @NonNull UiPrefs uiPrefs,
                            @NonNull LocalRepository localRepo) {
        super(context);
        this.uiPrefs = uiPrefs;
        this.localRepo = localRepo;

        if (uiPrefs.firstRun()) {
            ToddlerEntity toddler = new ToddlerEntity();
            toddler.id = 1;
            toddler.name = "Toddler";
            toddler.age = 1;

            localRepo.put(toddler);
        }
    }

    @Override
    public void bindView(@NonNull ToddlerView view) {
        super.bindView(view);
        showToddler();
        showWelcome();
    }

    private void showWelcome() {
        if (uiPrefs.firstRun()) {
            showConfirm("Ура! Первый запуск приложения!");
            uiPrefs.setFirstRun(false);
        }
    }

    private void showToddler() {
        Stream.of(localRepo.get(ToddlerEntity.class))
                .findFirst()
                .map(t -> ToddlerVM.builder()
                        .id(t.id)
                        .name(t.name)
                        .build())
                .ifPresent(t -> getView().ifPresent(v -> v.showToddler(t)));
    }
}
