package ru.toddler.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.toddler.di.scope.PerApplication;
import ru.toddler.model.storage.entity.ToddlerEntity;
import ru.toddler.model.network.ApiProvider;
import ru.toddler.model.preference.UiPrefs;
import ru.toddler.model.repository.LocalRepository;
import ru.toddler.view.ToddlerView;
import ru.toddler.view.model.ToddlerVM;

@PerApplication
public class ToddlerPresenter extends BasePresenter<ToddlerView> {

    private final String API_URL = "https://maps.googleapis.com";

    @NonNull
    private final UiPrefs uiPrefs;

    @NonNull
    private final LocalRepository localRepo;
    @NonNull
    private final ApiProvider networkRepo;
    private boolean routeRequestInProgress = false;

    @Inject
    public ToddlerPresenter(@NonNull Context context,
                            @NonNull UiPrefs uiPrefs,
                            @NonNull LocalRepository localRepo,
                            @NonNull ApiProvider networkRepo) {
        super(context);
        this.uiPrefs = uiPrefs;
        this.localRepo = localRepo;
        this.networkRepo = networkRepo;

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

    public void loadDistance() {
        if (!routeRequestInProgress) {
            routeRequestInProgress = true;
            networkRepo.getRoutingApi(API_URL).getRoute("Sankt-Peterburg", "Praga")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate(() -> routeRequestInProgress = false)
                    .subscribe(res -> getView().ifPresent(v -> v.showInfo(res.routes.get(0).legs.get(0).distance.text)),
                            this::handleError);
        }
    }
}
