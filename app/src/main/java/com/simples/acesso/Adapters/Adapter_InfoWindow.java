package com.simples.acesso.Adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Location;


public class Adapter_InfoWindow implements GoogleMap.InfoWindowAdapter {

    Activity activity;
    View myContentsView;
    double lat;
    double lng;

    public Adapter_InfoWindow(Activity activity, double lat, double lng){
        this.activity = activity;
        myContentsView = activity.getLayoutInflater().inflate(R.layout.info_map_window, null);
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView textView = myContentsView.findViewById(R.id.text_info);
        String local = new Service_Location(activity).getAddress(lat, lng);
        textView.setText(local);
        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
