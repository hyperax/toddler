package ru.toddler.model.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.toddler.model.network.entity.RouteResponse;

public interface RoutingApi {

    @GET("/maps/api/directions/json")
    Observable<RouteResponse> getRoute(
            @Query(value = "origin") String position,
            @Query(value = "destination") String destination);
}
