package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;
import com.simples.acesso.Manifest;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.PreLoads;
import com.simples.acesso.Utils.ValidGPS;

public class Principal extends AppCompatActivity implements View.OnClickListener, LocationListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    AlertDialog.Builder builder;
    Service_Login serviceLogin;
    SharedPreferences sharedPreferences;

    MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        serviceLogin = new Service_Login(this);
        builder = new AlertDialog.Builder(this);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        createToolbar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        }, 1);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        localeProfile();
        validDocument();

    }

    private void validDocument(){
        if (sharedPreferences != null){
            if(sharedPreferences.getString("document", "").isEmpty()){
               builder.setTitle(R.string.app_name);
               builder.setMessage(R.string.info_update_account);
               builder.setCancelable(false);
               builder.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
               builder.create().show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void localeProfile() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    try{
                        MapsInitializer.initialize(getApplicationContext());
                        mapReady(location);
                    }catch (Exception e){}
                }else{
                    MapsInitializer.initialize(getApplicationContext());
                    mapView.onResume();
                }
            }
        });
    }

    private void createToolbar(Toolbar toolbar) {
        toolbar = (Toolbar) findViewById(R.id.actionbar_principal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_menu));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
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
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void mapReady(final Location location){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.setMyLocationEnabled(true);
                googleMap.setBuildingsEnabled(true);
                googleMap.setIndoorEnabled(true);
                googleMap.setTrafficEnabled(false);
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Principal.this, R.raw.map_style));

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                googleMap.animateCamera(cameraUpdate);
                mapView.onResume();

            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        googleMap.animateCamera(cameraUpdate);

    }
}
