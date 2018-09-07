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
import com.simples.acesso.Adapters.Adapter_Places;
import com.simples.acesso.Adapters.Adapter_SearchPlace;
import com.simples.acesso.Models.Places_Model;
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
    ViewStub loading_places;

    public Service_Location(Activity activity){
        this.activity = activity;
        this.editor = activity.getSharedPreferences("attendance", Context.MODE_PRIVATE).edit();
        this.loading_places = activity.findViewById(R.id.loading_places);
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

    public void getPlaceAdress (String text, final List<SearchPlace_Model> list, final RecyclerView recyclerView, final ProgressBar progress_search){
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
                                    progress_search.setVisibility(View.GONE);
                                }else{
                                    progress_search.setVisibility(View.VISIBLE);
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

    public void getPlaces (final double lat, double lng, final String type, final RecyclerView recyclerView){
        loading_places.setVisibility(View.VISIBLE);
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=15000&type="+type+"&key=AIzaSyAfR29suxxy_ZWoiwkfpFNb2qGTCwWMIiE")
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
                                        List<Places_Model> list = new ArrayList<Places_Model>();
                                        list.clear();
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            Places_Model places_model = new Places_Model(jsonObject);
                                            list.add(places_model);
                                        }
                                        Adapter_Places adapter_places = new Adapter_Places(activity, list);
                                        recyclerView.setAdapter(adapter_places);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        loading_places.setVisibility(View.GONE);
                                    }else{
                                        recyclerView.setVisibility(View.GONE);
                                        loading_places.setVisibility(View.VISIBLE);
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
}
