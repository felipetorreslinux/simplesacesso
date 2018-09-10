package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Attendance;
import com.simples.acesso.Services.Service_Location;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.ValidGPS;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Principal extends AppCompatActivity implements View.OnClickListener{

    static AlertDialog.Builder builder;
    static AlertDialog alertDialog;
    Service_Login serviceLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ImageView item_person_perfil;
    ImageView item_my_location;
    ImageView image_local_profile;

    static TextView location_info;
    static int alive_location;
    Snackbar snackbar;

    BottomNavigationView bottom_bar_itens_principal;

    static GoogleMap mGoogleMap;
    MapView mapView;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;

    Service_Location serviceLocation;
    double Latitude;
    double Longitude;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        editor = getSharedPreferences("profile", MODE_PRIVATE).edit();
        serviceLogin = new Service_Login(this);
        serviceLocation = new Service_Location(this);
        serviceLocation.listAttendence();

        builder = new AlertDialog.Builder(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        item_my_location = findViewById(R.id.item_my_location);
        item_my_location.setOnClickListener(this);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        location_info = findViewById(R.id.location_info);
        location_info.setOnClickListener(this);

        snackbar = Snackbar.make(getWindow().getDecorView(),
                "Procurando sua localização\nAguarde...",
                Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(progressDialog != null){
            progressDialog.dismiss();
        }
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
                        Intent intent = new Intent(Principal.this, Minhas_Informacoes.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(null, null);
                builder.create().show();
            } else {
                new Service_Attendance(this).check(progressDialog);
                localeProfile();
            }
        }
    }

    private void imageProfile() {
        item_person_perfil = findViewById(R.id.item_person_perfil);
        image_local_profile = findViewById(R.id.image_local_profile);
        item_person_perfil.setOnClickListener(this);
        if(sharedPreferences.getString("image", "").isEmpty()){
            Picasso.get()
                    .load(R.drawable.no_image)
                    .transform(new CropCircleTransformation())
                    .resize(200,200)
                    .into(item_person_perfil);
        }else{
            Picasso.get()
                    .load(Uri.parse(sharedPreferences.getString("image", "")))
                    .transform(new CropCircleTransformation())
                    .resize(200,200)
                    .into(item_person_perfil);
        }
        Picasso.get()
                .load(R.drawable.no_image)
                .transform(new CropCircleTransformation())
                .resize(50,50)
                .into(image_local_profile);
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
                    }
                }
            });
        }
    }

    private void mapReady(final Location location){
        try {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    snackbar.show();
                    alive_location = 0;
                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                    googleMap.getUiSettings().setCompassEnabled(false);
                    googleMap.setBuildingsEnabled(true);
                    googleMap.setIndoorEnabled(true);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
                    googleMap.animateCamera(cameraUpdate);
                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            location_info.setText(serviceLocation.getAddress(new LatLng(location.getLatitude(), location.getLongitude())));
                            editor.putString("lat", String.valueOf(location.getLatitude()));
                            editor.putString("lng", String.valueOf(location.getLongitude()));
                            editor.commit();
                            alive_location = 1;
                            snackbar.dismiss();
                        }
                    });
                    mapView.onResume();
                    imageProfile();
                    openAttendence();
                }
            });
        }catch (NullPointerException e) {}
    }

    private void openAttendence(){
        bottom_bar_itens_principal = findViewById(R.id.bottom_bar_itens_principal);
        bottom_bar_itens_principal.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.item_police:

                        Intent police = new Intent(Principal.this, Services_Emergency.class);
                        police.putExtra("type_service", 1);
                        police.putExtra("local_user", location_info.getText().toString().trim());
                        startActivityForResult(police,1010);

                        break;

                    case R.id.item_ambulance:

                        Intent ambulance = new Intent(Principal.this, Services_Emergency.class);
                        ambulance.putExtra("type_service", 2);
                        ambulance.putExtra("local_user", location_info.getText().toString().trim());
                        startActivityForResult(ambulance,1010);

                        break;

                    case R.id.item_fireman:

                        Intent fireman = new Intent(Principal.this, Services_Emergency.class);
                        fireman.putExtra("type_service", 3);
                        fireman.putExtra("local_user", location_info.getText().toString().trim());
                        startActivityForResult(fireman, 1010);

                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_person_perfil:
                startActivityForResult(new Intent(this, Perfil.class), 2002);
                break;
            case R.id.location_info:
                builder = new AlertDialog.Builder(this, R.style.CustomDialog);
                View view = getLayoutInflater().inflate(R.layout.dialog_search_place, null);
                builder.setView(view);
                alertDialog = builder.create();
                alertDialog.show();

                final ProgressBar progressBar = view.findViewById(R.id.progress_search);
                progressBar.setVisibility(View.GONE);

                final EditText editText = view.findViewById(R.id.text_search);

                final RecyclerView recyclerView = view.findViewById(R.id.recycler_search);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length() > 3){
                            progressBar.setVisibility(View.VISIBLE);
                            new Service_Location(Principal.this)
                                    .getPlaceAdress(editText.getText().toString().trim(), recyclerView, progressBar);
                        }else{
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                break;
            case R.id.item_my_location:
                localeProfile();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
        }
    }

    public static void localNovo(String local, double lat, double lng){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15);
        mGoogleMap.animateCamera(cameraUpdate);
        location_info.setText(local);
        alertDialog.dismiss();
    }
}
