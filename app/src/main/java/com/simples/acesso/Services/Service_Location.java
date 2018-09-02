package com.simples.acesso.Services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.simples.acesso.API.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Service_Location {

    Activity activity;
    SharedPreferences.Editor editor;

    public Service_Location(Activity activity){
        this.activity = activity;
        this.editor = activity.getSharedPreferences("attendence", Context.MODE_PRIVATE).edit();
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
        }
    }

    public void listAttendence(){
        AndroidNetworking.get(API.URL_DEV+"Modules/AttendanceFilter.php")
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
