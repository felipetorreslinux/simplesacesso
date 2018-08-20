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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Location;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.PreLoads;
import com.simples.acesso.Utils.ValidGPS;

public class Principal extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    AlertDialog.Builder builder;
    Service_Login serviceLogin;
    SharedPreferences sharedPreferences;
    ImageView image_menu;

    LinearLayout item_service_emergency;
    LinearLayout item_location_service;
    ImageView image_service;
    TextView name_service;
    TextView name_location;

    Button button_send_service;

    MapView mapView;
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

        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        serviceLogin = new Service_Login(this);
        serviceLocation = new Service_Location(this);
        builder = new AlertDialog.Builder(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        image_menu = findViewById(R.id.image_menu);
        image_menu.setOnClickListener(this);

        item_service_emergency = findViewById(R.id.item_service_emergency);
        item_service_emergency.setOnClickListener(this);

        item_location_service = findViewById(R.id.item_location_service);
        name_location = findViewById(R.id.name_location);

        image_service = findViewById(R.id.image_service);
        name_service = findViewById(R.id.name_service);

        button_send_service = findViewById(R.id.button_send_service);
        button_send_service.setVisibility(View.GONE);
        button_send_service.setOnClickListener(this);

        drawerLayout = findViewById(R.id.drawerLayout);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        validDocument();
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
                    }
                }
            });
        }
    }

    private void mapReady(final Location location){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setBuildingsEnabled(true);
                googleMap.setIndoorEnabled(true);
                googleMap.setTrafficEnabled(false);
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Principal.this, R.raw.map_style));

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                googleMap.animateCamera(cameraUpdate);
                mapView.onResume();
                watchLocation();

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
                name_location.setText(serviceLocation.getAddress(location.getLatitude(), location.getLongitude()));
            }
        };
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.perfil:
                Intent perfil = new Intent(this, Perfil.class);
                startActivityForResult(perfil, 2002);
                break;

            case R.id.calls:
                Intent calls = new Intent(this, Requests_Services.class);
                startActivity(calls);
                break;

            case R.id.exit: {
                builder.setTitle(R.string.app_name);
                builder.setMessage(R.string.info_exit_app);
                builder.setCancelable(false);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreLoads.open(Principal.this, null, "Saindo...", false);
                        serviceLogin.logout(sharedPreferences.getInt("id", 0));
                    }
                });
                builder.setNegativeButton("Não", null);
                builder.create().show();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.item_service_emergency:
                Intent intent = new Intent(this, Services_Emergency.class);
                startActivityForResult(intent, 2001);
                break;

            case R.id.button_send_service:
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
            case 2001:
                if(resultCode == Activity.RESULT_OK){

                    Animation animation = new TranslateAnimation(0,0,1000,0);
                    animation.setDuration(1000);
                    animation.setFillEnabled(true);

                    int TypeService = data.getExtras().getInt("type_service");
                    int ImageService = data.getExtras().getInt("image_service");
                    String NameService = data.getExtras().getString("name_service");
                    image_service.setImageDrawable(getResources().getDrawable(ImageService));
                    name_service.setText(NameService);
                    name_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    item_location_service.setVisibility(View.VISIBLE);
                    name_location.setText(serviceLocation.getAddress(Latitude, Longitude));

                    button_send_service.setVisibility(View.VISIBLE);
                    button_send_service.setAnimation(animation);
                    animation.start();

                    switch (TypeService){
                        case 1:
                            button_send_service.setText("Chamar Polícia");
                            break;
                        case 2:
                            button_send_service.setText("Chamar Samu");
                            break;
                        case 3:
                            button_send_service.setText("Chamar Bombeiros");
                            break;
                        default:
                            button_send_service.setText("Socorro");
                            break;
                    }


                }else{
                    image_service.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio));
                    name_service.setText(R.string.info_box_service);
                    name_service.setTextColor(getResources().getColor(R.color.colorGray));
                    item_location_service.setVisibility(View.GONE);

                    button_send_service.setVisibility(View.GONE);
                    button_send_service.setText("Socorro");
                }
                break;

            case 2002:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, R.string.info_ok_save_perfil, Toast.LENGTH_SHORT).show();
                }
                break;

            case 1010:
                builder.create().dismiss();
                localeProfile();
                break;
        }
    }
}
