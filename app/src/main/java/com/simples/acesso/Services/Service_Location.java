package com.simples.acesso.Services;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Service_Location {

    Activity activity;

    public Service_Location(Activity activity){
        this.activity = activity;
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
}
