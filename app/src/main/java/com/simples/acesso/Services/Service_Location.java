package com.simples.acesso.Services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.model.LatLng;
import com.simples.acesso.API.API;
import com.simples.acesso.Adapters.Adapter_Hospitais;
import com.simples.acesso.Adapters.Adapter_Polices;
import com.simples.acesso.Adapters.Adapter_SearchPlace;
import com.simples.acesso.Models.Hospitais_Model;
import com.simples.acesso.Models.Police_Model;
import com.simples.acesso.Models.SearchPlace_Model;
import com.simples.acesso.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Service_Location {

    Activity activity;
    SharedPreferences.Editor editor;

    public Service_Location(Activity activity){
        this.activity = activity;
        this.editor = activity.getSharedPreferences("attendance", Context.MODE_PRIVATE).edit();
    }

    public String getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address obj = addresses.get(0);
            String[] dados = obj.getAddressLine(0).split(",");
            String[] dados2 = dados[1].split("-");
            return dados[0] + ", " + dados2[0] + "\n" + dados2[1] + " - " + dados[2];
        } catch (IOException e) {
            return null;
        }
    }

    public void getPlaceAdress (String text, final RecyclerView recyclerView, final ProgressBar progressBar){
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+text+"&inputtype=textquery&fields=formatted_address,name,geometry&key=AIzaSyAfR29suxxy_ZWoiwkfpFNb2qGTCwWMIiE")
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "OK":
                                JSONArray array = response.getJSONArray("candidates");
                                if(array.length() > 0){
                                    List<SearchPlace_Model> list = new ArrayList<SearchPlace_Model>();
                                    list.clear();
                                    for(int i = 0; i < array.length(); i++){
                                        JSONObject jsonObject = array.getJSONObject(i);
                                        SearchPlace_Model searchPlaceModel = new SearchPlace_Model(
                                                i,
                                                jsonObject.getString("formatted_address"),
                                                jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                                jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng"),
                                                jsonObject.getString("name"));
                                        list.add(searchPlaceModel);
                                    }
                                    Adapter_SearchPlace adapterSearchPlace = new Adapter_SearchPlace(activity, list);
                                    recyclerView.setAdapter(adapterSearchPlace);
                                    progressBar.setVisibility(View.GONE);
                                }else{
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                                break;
                            default:

                                break;
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {

                }
            });
    }

    public void getPlaces (final double lat, double lng, final String type, String progress, final RecyclerView recyclerView, final ViewStub loading_places){
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius="+progress+"&type="+type+"&key=AIzaSyAfR29suxxy_ZWoiwkfpFNb2qGTCwWMIiE")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status = response.getString("status");
                            switch (status){
                                case "OK":
                                    JSONArray array = response.getJSONArray("results");
                                    if(array.length() > 0){
                                        switch (type){
                                            case "hospital":
                                                List<Hospitais_Model> list_hospitais = new ArrayList<Hospitais_Model>();
                                                list_hospitais.clear();
                                                for(int i = 0; i < array.length(); i++){
                                                    JSONObject jsonObject = array.getJSONObject(i);
                                                    Hospitais_Model hospitais_model = new Hospitais_Model(jsonObject);
                                                    list_hospitais.add(hospitais_model);
                                                }
                                                Adapter_Hospitais adapter_hospitais = new Adapter_Hospitais(activity, list_hospitais);
                                                recyclerView.setAdapter(adapter_hospitais);
                                                recyclerView.setVisibility(View.VISIBLE);
                                                loading_places.setVisibility(View.GONE);
                                                break;
                                            case "police":
                                                List<Police_Model> list_police = new ArrayList<Police_Model>();
                                                list_police.clear();
                                                for(int i = 0; i < array.length(); i++){
                                                    JSONObject jsonObject = array.getJSONObject(i);
                                                    Police_Model police = new Police_Model(jsonObject);
                                                    list_police.add(police);
                                                }
                                                Adapter_Polices adapter_places = new Adapter_Polices(activity, list_police);
                                                recyclerView.setAdapter(adapter_places);
                                                recyclerView.setVisibility(View.VISIBLE);
                                                loading_places.setVisibility(View.GONE);
                                                break;
                                        }
                                    }
                                    break;
                            }
                        }catch (JSONException e){}
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void listAttendence(){
        AndroidNetworking.get(API.URL_DEV+"Modules/Attendance_Filter.php")
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                JSONArray jsonArray = response.getJSONArray("attendance");
                                editor.putString("list", jsonArray.toString());
                                editor.commit();
                                break;
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    API.ErrorSever(activity, anError.getErrorCode());
                }
            });
    }

    public String getCity(LatLng latLng){
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            return addresses.get(0).getLocality();
        } catch (IOException e) {
            return null;
        }
    }
}
