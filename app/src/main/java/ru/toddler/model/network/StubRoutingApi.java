package ru.toddler.model.network;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.http.Query;
import ru.toddler.model.network.entity.RouteResponse;

public class StubRoutingApi implements RoutingApi {

    @Inject
    public StubRoutingApi() {
        // 4 dagger 2
    }

    @Override
    public Observable<RouteResponse> getRoute(@Query(value = "origin") String position, @Query(value = "destination") String destination) {
        return Observable.just(new RouteResponse()
                .setRoutes(Collections.singletonList(new RouteResponse.Route()
                        .setLegs(Collections.singletonList(new RouteResponse.Leg()
                                .setDistance(new RouteResponse.Distance()
                                        .setText("very far from here!")
                                        .setValue(999999))
                                .setDuration(new RouteResponse.Duration()
                                        .setText("very long!")
                                        .setValue(11111111))
                                .setEndLocation(new RouteResponse.EndLocation()
                                        .setLat(0d)
                                        .setLng(0d)))))));
    }
}
