package com.simples.acesso.Views;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Location;

public class Hospitais extends AppCompatActivity{

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    RecyclerView recycler_hospitals;

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

        String lat = sharedPreferences.getString("lat", "");
        String lng = sharedPreferences.getString("lng", "");

        new Service_Location(this).getPlaces(Double.parseDouble(lat), Double.parseDouble(lng), "hospital", recycler_hospitals);
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
