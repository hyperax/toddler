package ru.toddler.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.toddler.R;
import ru.toddler.databinding.ActMainBinding;
import ru.toddler.presenter.ToddlerPresenter;
import ru.toddler.view.ToddlerView;
import ru.toddler.view.model.ToddlerVM;

public class MainActivity extends PresenterActivity<ToddlerPresenter, ToddlerView> implements ToddlerView {

    @Inject
    ToddlerPresenter presenter;

    private ActMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        getAppComponent().inject(this);
        binding = bindView(R.layout.act_main);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void showToddler(@NonNull ToddlerVM toddlerVM) {
        binding.setToddler(toddlerVM);
    }

    @NonNull
    @Override
    protected ToddlerPresenter getPresenter() {
        return presenter;
    }

    @NonNull
    @Override
    protected ToddlerView fetchView() {
        return this;
    }
}
