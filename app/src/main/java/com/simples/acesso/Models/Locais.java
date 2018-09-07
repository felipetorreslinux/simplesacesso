package com.simples.acesso.Models;

public class Locais {

    String id;
    String name;
    long distance;
    double lat;
    double lng;
    String local;

    public Locais(String id, String name, long distance, double lat, double lng, String local) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
        this.local = local;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
