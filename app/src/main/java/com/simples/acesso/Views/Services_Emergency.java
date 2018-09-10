package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.simples.acesso.Adapters.Adapter_Attendance;
import com.simples.acesso.Models.Attendance_Model;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Attendance;
import com.simples.acesso.Services.Service_Location;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Services_Emergency extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences servicos;
    SharedPreferences sharedPreferences;

    int TYPE_SERVICE;
    String NAME_SERVICE;
    String LOCAL_USER;

    Toolbar toolbar;

    TextView info_local_service;

    ImageView image_local_profile;
    MapView map_service;

    List<Attendance_Model> list = new ArrayList<Attendance_Model>();
    RecyclerView list_services;

    Button button_service_emergency;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_emergency);

        servicos = getSharedPreferences("attendance", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);

        createToolbar(toolbar);
        mapService(savedInstanceState);

        list_services = findViewById(R.id.list_services);
        list_services.setLayoutManager(new LinearLayoutManager(this));
        list_services.setHasFixedSize(false);
        list_services.setNestedScrollingEnabled(false);

        listServices();

        button_service_emergency = findViewById(R.id.button_service_emergency);
        button_service_emergency.setOnClickListener(this);

    }

    private void mapService(Bundle savedInstanceState){
        LOCAL_USER = getIntent().getExtras().getString("local_user");
        info_local_service = findViewById(R.id.info_local_service);
        info_local_service.setText(LOCAL_USER);
        image_local_profile = findViewById(R.id.image_local_profile);
        map_service = findViewById(R.id.map_service);
        map_service.onCreate(savedInstanceState);
        map_service.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                String lat = sharedPreferences.getString("lat", "");
                String lng = sharedPreferences.getString("lng", "");
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.getUiSettings().setAllGesturesEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(false);
                googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
                googleMap.setBuildingsEnabled(true);
                googleMap.setIndoorEnabled(true);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 17);
                googleMap.moveCamera(cameraUpdate);
                if(sharedPreferences.getString("image", "").isEmpty()){
                    Picasso.get()
                            .load(R.drawable.no_image)
                            .transform(new CropCircleTransformation())
                            .resize(200,200)
                            .into(image_local_profile);
                }else{
                    Picasso.get()
                            .load(Uri.parse(sharedPreferences.getString("image", "")))
                            .transform(new CropCircleTransformation())
                            .resize(200,200)
                            .into(image_local_profile);
                }
                map_service.onResume();
            }
        });
    }

    private void listServices() {
        list.clear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONArray array = new JSONArray(servicos.getString("list", ""));
                    if(array.length() > 0){
                        for(int i = 0; i < array.length(); i++){
                            JSONObject jsonObject = array.getJSONObject(i);
                            if(jsonObject.getInt("id_service") == TYPE_SERVICE) {
                                Attendance_Model attendanceModel = new Attendance_Model(
                                        jsonObject.getInt("id"),
                                        jsonObject.getInt("id_service"),
                                        jsonObject.getString("description"),
                                        false);
                                list.add(attendanceModel);
                            }
                            Adapter_Attendance adapterAttendance = new Adapter_Attendance(Services_Emergency.this, list);
                            list_services.setAdapter(adapterAttendance);
                        }
                    }else{
                        Attendance_Model attendanceModel = new Attendance_Model(
                                0,
                                0,
                                "Não há itens disponíveis",
                                false);
                        list.add(attendanceModel);
                    }

                }catch (JSONException e){}
            }
        }, 30);
    }

    private void createToolbar(Toolbar toolbar) {

        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.toolbar_service_emergency);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        TYPE_SERVICE = getIntent().getExtras().getInt("type_service");
        switch (TYPE_SERVICE){
            case 1:
                getSupportActionBar().setTitle(R.string.send_police);
                NAME_SERVICE = "Polícia";
                break;
            case 2:
                getSupportActionBar().setTitle(R.string.send_samu);
                NAME_SERVICE = "Samu";
                break;
            case 3:
                getSupportActionBar().setTitle(R.string.send_fireman);
                NAME_SERVICE = "Bombeiros";
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button_service_emergency:
                try {
                    registerAttendance();
                } catch (JSONException e) {}
                break;

        }
    }

    private void registerAttendance() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type_service", TYPE_SERVICE);
        jsonObject.put("name_service", NAME_SERVICE);
        jsonObject.put("local_service", LOCAL_USER);
        new Service_Attendance(this).register(jsonObject);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1000:
                if(resultCode == Activity.RESULT_OK){

                }else{
                    createToolbar(toolbar);
                    listServices();
                }
                break;
        }
    }
}
