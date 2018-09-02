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
import com.simples.acesso.Adapters.Adapter_InfoWindow;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Location;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.PreLoads;
import com.simples.acesso.Utils.ValidGPS;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Principal extends AppCompatActivity implements View.OnClickListener{

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Service_Login serviceLogin;
    SharedPreferences sharedPreferences;

    LinearLayout item_police_open;
    LinearLayout item_ambulance_open;
    LinearLayout item_firetruck_open;

    ImageView item_person_perfil;
    ImageView item_my_location;

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

    private void mapReady(final Location location){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(final GoogleMap googleMap) {

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                if(youPerson != null){
                    youPerson.remove();
                }

                youPerson = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .flat(false)
                        .draggable(false));

                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.setBuildingsEnabled(true);
                googleMap.setIndoorEnabled(true);
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Principal.this, R.raw.map_style));

                googleMap.setInfoWindowAdapter(new Adapter_InfoWindow(Principal.this, location.getLatitude(), location.getLongitude()));

                youPerson.showInfoWindow();

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                googleMap.animateCamera(cameraUpdate);

                googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {

                    }
                });

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                    }
                });

                mapView.onResume();
//                watchLocation();


            }
        });
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

    private void openAttendence(int type){
        builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View view = getLayoutInflater().inflate(R.layout.view_attendence, null);
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();

        TextView name_item_attendence = view.findViewById(R.id.name_item_attendence);

        RecyclerView list_filter_attendence = view.findViewById(R.id.list_filter_attendence);
        list_filter_attendence.setLayoutManager(new LinearLayoutManager(this));
        list_filter_attendence.setHasFixedSize(true);
        list_filter_attendence.setNestedScrollingEnabled(false);

        switch (type){
            case 0:
                name_item_attendence.setText(R.string.label_police_tab);
                break;
            case 1:
                name_item_attendence.setText(R.string.label_samu_tab);
                break;
            case 2:
                name_item_attendence.setText(R.string.label_fireman_tab);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 2002:
                localeProfile();
                break;

            case 1010:
                builder.create().dismiss();
                localeProfile();
                break;
        }
    }
}
