package com.simples.acesso.Models;

import org.json.JSONObject;

public class SearchPlace_Model {
    int id;
    String address;
    double lat;
    double lng;
    String name;

    public SearchPlace_Model(int id, String address, double lat, double lng, String name) {
        this.id = id;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
