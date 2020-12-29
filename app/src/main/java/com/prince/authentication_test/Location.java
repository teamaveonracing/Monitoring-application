package com.prince.authentication_test;

public class Location {
    double lat;
    double lon;
    public Location(){}

    public Location(double lat,double lon){
        this.lat=lat;
        this.lon=lon;

    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
