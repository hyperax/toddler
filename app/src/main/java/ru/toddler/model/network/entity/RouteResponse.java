package ru.toddler.model.network.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Class describes the result of request to
 * https://developers.google.com/maps/documentation/directions/
 *
 */
public class RouteResponse {
    public RouteResponse() {
        // 4 JSON
    }
    public List<Route> routes;

    public RouteResponse setRoutes(List<Route> routes) {
        this.routes = routes;
        return this;
    }

    public static class Route {
        public Route() {
            // 4 JSON
        }

        public List<Leg> legs;

        public Route setLegs(List<Leg> legs) {
            this.legs = legs;
            return this;
        }
    }

    public static class Leg {
        public Leg() {
            // 4 JSON
        }

        public Distance distance;
        public Duration duration;
        @JsonProperty("end_location")
        public EndLocation endLocation;

        public Leg setDistance(Distance distance) {
            this.distance = distance;
            return this;
        }
        public Leg setDuration(Duration duration) {
            this.duration = duration;
            return this;
        }
        public Leg setEndLocation(EndLocation endLocation) {
            this.endLocation = endLocation;
            return this;
        }
    }

    public static class EndLocation {
        public EndLocation() {
            // 4 JSON
        }

        public Double lat;
        public Double lng;

        public EndLocation setLat(Double lat) {
            this.lat = lat;
            return this;
        }

        public EndLocation setLng(Double lng) {
            this.lng = lng;
            return this;
        }
    }

    public static class Distance {
        public Distance() {
            // 4 JSON
        }

        public String text;
        public int value; // meters

        public Distance setText(String text) {
            this.text = text;
            return this;
        }
        public Distance setValue(int value) {
            this.value = value;
            return this;
        }
    }

    public static class Duration {
        public Duration() {
            // 4 JSON
        }

        public String text;
        public int value; // seconds

        public Duration setText(String text) {
            this.text = text;
            return this;
        }
        public Duration setValue(int value) {
            this.value = value;
            return this;
        }
    }
}