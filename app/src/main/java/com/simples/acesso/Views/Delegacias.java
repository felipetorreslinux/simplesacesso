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

public class Delegacias extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    RecyclerView recycler_delegacias;

    SeekBar seek_hospital;
    TextView distancia_seek;
    ViewStub loading_places;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_delegacias);
        createToolbar(toolbar);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        recycler_delegacias = findViewById(R.id.recycler_delegacias);
        recycler_delegacias.setLayoutManager(new LinearLayoutManager(this));
        recycler_delegacias.setHasFixedSize(true);
        recycler_delegacias.setNestedScrollingEnabled(false);

        final String lat = sharedPreferences.getString("lat", "");
        final String lng = sharedPreferences.getString("lng", "");

        seek_hospital = findViewById(R.id.seek_hospital);
        seek_hospital.setMin(1000);
        seek_hospital.setMax(50000);
        distancia_seek = findViewById(R.id.distancia_seek);
        distancia_seek.setText("Distância de 1 km de onde você está");
        loading_places.setVisibility(View.VISIBLE);
        new Service_Location(this).getPlaces(Double.parseDouble(lat), Double.parseDouble(lng),"police", "1000", recycler_delegacias, loading_places);
        seek_hospital.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distancia_seek.setText(seekBar.getProgress()+" km");
                loading_places.setVisibility(View.VISIBLE);
                new Service_Location(Delegacias.this).getPlaces(Double.parseDouble(lat), Double.parseDouble(lng),"hospital", String.valueOf(seekBar.getProgress()), recycler_delegacias, loading_places);
                distancia_seek.setText("Distância de "+seek_hospital.getProgress() / 1000+" km de onde você está");
            }
        });
    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.toolbar_delegacias);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_delegacias);
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
