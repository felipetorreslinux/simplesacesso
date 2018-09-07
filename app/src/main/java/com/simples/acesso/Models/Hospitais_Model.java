package com.simples.acesso.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Hospitais_Model {

    String id;
    String name;
    String local;
    double distance;
    double lat;
    double lng;
    boolean open;

    public Hospitais_Model(JSONObject jsonObject) throws JSONException{
        this.id = jsonObject.getString("id");
        this.name = jsonObject.getString("name");
        this.local = jsonObject.getString("vicinity");
        this.lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        this.lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
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

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
