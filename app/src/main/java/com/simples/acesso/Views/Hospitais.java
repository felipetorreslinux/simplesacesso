package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.SeekBar;
import android.widget.TextView;

import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Location;

public class Hospitais extends AppCompatActivity{

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    RecyclerView recycler_hospitals;

    SeekBar seek_hospital;
    TextView distancia_seek;
    ViewStub loading_places;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_hospitais);
        createToolbar(toolbar);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        recycler_hospitals = findViewById(R.id.recycler_hospitals);
        recycler_hospitals.setLayoutManager(new LinearLayoutManager(this));
        recycler_hospitals.setHasFixedSize(true);
        recycler_hospitals.setNestedScrollingEnabled(false);

        final String lat = sharedPreferences.getString("lat", "");
        final String lng = sharedPreferences.getString("lng", "");

        loading_places = findViewById(R.id.loading_places);

        seek_hospital = findViewById(R.id.seek_hospital);
        seek_hospital.setMin(1000);
        seek_hospital.setMax(50000);
        distancia_seek = findViewById(R.id.distancia_seek);
        distancia_seek.setText("Distância de 1 km de onde você está");
        loading_places.setVisibility(View.VISIBLE);
        new Service_Location(this).getPlaces(Double.parseDouble(lat), Double.parseDouble(lng),"hospital", "1000", recycler_hospitals, loading_places);
        seek_hospital.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distancia_seek.setText(seekBar.getProgress()+" km");
                loading_places.setVisibility(View.VISIBLE);
                new Service_Location(Hospitais.this).getPlaces(Double.parseDouble(lat), Double.parseDouble(lng),"hospital", String.valueOf(seekBar.getProgress()), recycler_hospitals, loading_places);
                distancia_seek.setText("Distância de "+seek_hospital.getProgress() / 1000+" km de onde você está");
            }
        });


    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.toolbar_hospitals);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_hospitals);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
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
    public void onBackPressed() {
        finish();
    }

}
