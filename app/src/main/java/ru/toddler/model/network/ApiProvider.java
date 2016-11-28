package ru.toddler.model.network;

import android.support.annotation.NonNull;

public interface ApiProvider {
    RoutingApi getRoutingApi(@NonNull String baseUrl);
}
