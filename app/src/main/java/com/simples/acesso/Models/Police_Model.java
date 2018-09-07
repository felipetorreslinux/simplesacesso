package com.simples.acesso.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Police_Model {

    String id;
    String name;
    String local;
    double lat;
    double lng;

    public Police_Model(JSONObject jsonObject) throws JSONException{
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

}
