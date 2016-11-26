package ru.toddler.model.network;

import android.support.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import ru.toddler.di.scope.PerApplication;

@PerApplication
public class RoutingApiImpl implements ApiProvider {

    private final OkHttpClient client;
    private final Converter.Factory converter;
    private StubRoutingApi stubRoutingApi;

    @Inject
    public RoutingApiImpl(@NonNull OkHttpClient client,
                          @NonNull Converter.Factory converter,
                          @NonNull StubRoutingApi stubRoutingApi) {
        this.client = client;
        this.converter = converter;
        this.stubRoutingApi = stubRoutingApi;
    }

    @Override
    public RoutingApi getRoutingApi(@NonNull String baseUrl) {
        /*if (BuildConfig.DEBUG) {
            return stubRoutingApi;
        }*/

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converter)
                .build()
                .create(RoutingApi.class);
    }
}
