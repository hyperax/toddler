package ru.toddler.di.module;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.toddler.BuildConfig;
import ru.toddler.di.scope.PerApplication;

@Module
public class NetworkModule {

    private static final long CONNECT_TIMEOUT_SECONDS = 5;
    private static final long READ_TIMEOUT_SECONDS = 10;
    private static final long WRITE_TIMEOUT_SECONDS = 10;

    @Provides
    @PerApplication
    OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        return builder.build();
    }

    @Provides
    @PerApplication
    Converter.Factory getJacksonFactory(ObjectMapper mapper) {
        return JacksonConverterFactory.create(mapper);
    }
}
