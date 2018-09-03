package com.simples.acesso.Services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.simples.acesso.API.API;
import com.simples.acesso.Adapters.Adapter_Hospitais;
import com.simples.acesso.Adapters.Adapter_SearchPlace;
import com.simples.acesso.Models.Hospitais_Model;
import com.simples.acesso.Models.SearchPlace_Model;

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

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String local = obj.getAddressLine(0);
            String[] dados = local.split(",");
            String[] dados2 = dados[1].split("-");

            local = dados[0]+", "+dados2[0]+"\n"+dados2[1]+" - "+dados[2];

            return local;
        } catch (IOException e) {
            return null;
        } catch (NullPointerException e){
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

    public void getHospital (final double lat, double lng, final RecyclerView recyclerView){
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=5000&type=hospital&key=AIzaSyAfR29suxxy_ZWoiwkfpFNb2qGTCwWMIiE")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status = response.getString("status");
                            switch (status){
                                case "OK":
                                    JSONArray array = response.getJSONArray("results");
                                    List<Hospitais_Model> list = new ArrayList<Hospitais_Model>();
                                    list.clear();
                                    if(array.length() > 0){
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            String name = jsonObject.getString("name");
                                            Hospitais_Model hospitais_model = new Hospitais_Model(name);
                                            list.add(hospitais_model);
                                        }
                                        Adapter_Hospitais adapter_hospitais = new Adapter_Hospitais(activity, list);
                                        recyclerView.setAdapter(adapter_hospitais);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }else{
                                        recyclerView.setVisibility(View.GONE);
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
