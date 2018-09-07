package com.simples.acesso.Adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simples.acesso.Models.Locais;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Location;

import org.json.JSONException;
import org.json.JSONObject;


public class Adapter_InfoWindow implements GoogleMap.InfoWindowAdapter {

    Activity activity;
    View myContentsView;
    Locais locais;

    public Adapter_InfoWindow(Activity activity, Locais locais){
        this.activity = activity;
        myContentsView = activity.getLayoutInflater().inflate(R.layout.info_map_window, null);
        this.locais = locais;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        TextView distance_info = myContentsView.findViewById(R.id.distance_info);
        TextView textView = myContentsView.findViewById(R.id.text_info);
        distance_info.setText(String.valueOf(locais.getDistance())+"\n km");
        textView.setText(locais.getLocal());
        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
