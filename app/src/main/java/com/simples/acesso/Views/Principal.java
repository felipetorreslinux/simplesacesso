package com.simples.acesso.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.simples.acesso.Adapters.Adapter_Attendance;
import com.simples.acesso.Adapters.Adapter_InfoWindow;
import com.simples.acesso.Models.Attendance_Model;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Location;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.LoadingView;
import com.simples.acesso.Utils.PreLoads;
import com.simples.acesso.Utils.ValidGPS;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Principal extends AppCompatActivity implements View.OnClickListener{

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Service_Login serviceLogin;
    SharedPreferences sharedPreferences;

    ImageView item_police_open;
    ImageView item_ambulance_open;
    ImageView item_firetruck_open;

    ImageView item_person_perfil;
    ImageView item_my_location;

    TextView location_info;

    MapView mapView;
    Marker youPerson;
    GoogleMap googleMap;
    LocationManager locationManager;
    LocationListener locationListener;
    FusedLocationProviderClient mFusedLocationClient;

    Service_Location serviceLocation;
    double Latitude;
    double Longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        serviceLogin = new Service_Login(this);
        serviceLocation = new Service_Location(this);
        serviceLocation.listAttendence();

        builder = new AlertDialog.Builder(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        item_person_perfil = findViewById(R.id.item_person_perfil);
        item_person_perfil.setOnClickListener(this);

        if(sharedPreferences.getString("image", "").isEmpty()){
            Picasso.with(this)
                    .load(R.drawable.no_image)
                    .transform(new CropCircleTransformation())
                    .resize(200,200)
                    .into(item_person_perfil);
        }else{
            Picasso.with(this)
                    .load(sharedPreferences.getString("image", ""))
                    .transform(new CropCircleTransformation())
                    .resize(200,200)
                    .into(item_person_perfil);
        }

        item_my_location = findViewById(R.id.item_my_location);
        item_my_location.setOnClickListener(this);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        location_info = findViewById(R.id.location_info);

        item_police_open = findViewById(R.id.item_police_open);
        item_ambulance_open = findViewById(R.id.item_ambulance_open);
        item_firetruck_open = findViewById(R.id.item_firetruck_open);

        item_ambulance_open.setOnClickListener(this);
        item_police_open.setOnClickListener(this);
        item_firetruck_open.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        validDocument();
        localeProfile();
    }

    private void validDocument() {
        if (sharedPreferences != null) {
            String document = sharedPreferences.getString("document", "");
            if (document.equals("")) {
                builder.setTitle(R.string.app_name);
                builder.setMessage(R.string.info_update_account);
                builder.setCancelable(false);
                builder.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                        Intent intent = new Intent(Principal.this, Perfil.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(null, null);
                builder.create().show();
            } else {
                localeProfile();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void localeProfile() {
        if(ValidGPS.check(this) == false){
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.info_not_gps);
            builder.setCancelable(false);
            builder.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1010);
                }
            });
            builder.create().show();
        }else{
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                    if (location != null) {
                        try {
                            MapsInitializer.initialize(getApplicationContext());
                            mapReady(location);
                        } catch (Exception e) {}
                    } else {
                        MapsInitializer.initialize(getApplicationContext());
                        mapView.onResume();
                        item_my_location.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void mapReady(final Location location){
        try {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    location_info.setText("Carregando sua\nlocalização");
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                    googleMap.setBuildingsEnabled(true);
                    googleMap.setIndoorEnabled(true);
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Principal.this, R.raw.map_style));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                    googleMap.animateCamera(cameraUpdate);

                    googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            double CameraLat = googleMap.getCameraPosition().target.latitude;
                            double CameraLong = googleMap.getCameraPosition().target.longitude;
                            if(serviceLocation.getAddress(CameraLat,CameraLong) != null){
                                location_info.setText(serviceLocation.getAddress(CameraLat,CameraLong));
                            }else{
                                location_info.setText(R.string.info_not_location);
                            }

                        }
                    });

                    googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                        @Override
                        public void onCameraMove() {
                            location_info.setText("Carregando\nlocalização");
                        }
                    });

                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            location_info.setText(serviceLocation.getAddress(location.getLatitude(), location.getLongitude()));
                        }
                    });

                    mapView.onResume();
                    watchLocation();

                }
            });
        }catch (NullPointerException e) {

        }
    }

    private void watchLocation() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                googleMap.animateCamera(cameraUpdate);
            }
        };
    }

    private void openAttendence(int type){
        switch (type){
            case 0:
                Intent police = new Intent(this, Services_Emergency.class);
                police.putExtra("type_service", 1);
                police.putExtra("local_user", serviceLocation.getAddress(Latitude, Longitude));
                startActivityForResult(police,1010);
                break;
            case 1:
                Intent ambulance = new Intent(this, Services_Emergency.class);
                ambulance.putExtra("type_service", 2);
                ambulance.putExtra("local_user", serviceLocation.getAddress(Latitude, Longitude));
                startActivityForResult(ambulance,1010);
                break;
            case 2:
                Intent fireman = new Intent(this, Services_Emergency.class);
                fireman.putExtra("type_service", 3);
                fireman.putExtra("local_user", serviceLocation.getAddress(Latitude, Longitude));
                startActivityForResult(fireman,1010);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.item_person_perfil:
                startActivityForResult(new Intent(this, Perfil.class), 2002);
                break;
            case R.id.item_my_location:
                localeProfile();
                break;
            case R.id.item_police_open:
                openAttendence(0);
                break;

            case R.id.item_ambulance_open:
                openAttendence(1);
                break;

            case R.id.item_firetruck_open:
                openAttendence(2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 2002:
                localeProfile();
                break;

            case 1010:
                localeProfile();
                break;
        }
    }
}
