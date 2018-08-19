package com.simples.acesso.Views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.simples.acesso.R;

public class Services_Emergency extends AppCompatActivity implements View.OnClickListener {

    int TYPE_SERVICE;
    int IMAGE_SERVICE;
    String NAME_SERVICE;

    Toolbar toolbar;
    LinearLayout item_sercive_police;
    LinearLayout item_sercive_ambulance;
    LinearLayout item_sercive_fireman;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_emergency);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);

        TYPE_SERVICE = 0;
        IMAGE_SERVICE = 0;
        NAME_SERVICE = null;

        createToolbar(toolbar);

        item_sercive_police = findViewById(R.id.item_sercive_police);
        item_sercive_police.setOnClickListener(this);
        item_sercive_ambulance = findViewById(R.id.item_sercive_ambulance);
        item_sercive_ambulance.setOnClickListener(this);
        item_sercive_fireman = findViewById(R.id.item_sercive_fireman);
        item_sercive_fireman.setOnClickListener(this);

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.toolbar_service_emergency);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_service_emergency);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_sercive_police:
                TYPE_SERVICE = 1;
                IMAGE_SERVICE = R.drawable.ic_police_car_with_light;
                NAME_SERVICE = "POLÍCIA";
                Intent police = getIntent();
                police.putExtra("type_service", TYPE_SERVICE);
                police.putExtra("image_service", IMAGE_SERVICE);
                police.putExtra("name_service", NAME_SERVICE);
                setResult(Activity.RESULT_OK, police);
                finish();
                break;

            case R.id.item_sercive_ambulance:
                TYPE_SERVICE = 2;
                IMAGE_SERVICE = R.drawable.ic_ambulace;
                NAME_SERVICE = "SAMU";
                Intent samu = getIntent();
                samu.putExtra("type_service", TYPE_SERVICE);
                samu.putExtra("image_service", IMAGE_SERVICE);
                samu.putExtra("name_service", NAME_SERVICE);
                setResult(Activity.RESULT_OK, samu);
                finish();
                break;

            case R.id.item_sercive_fireman:
                TYPE_SERVICE = 3;
                IMAGE_SERVICE = R.drawable.ic_fire_truck;
                NAME_SERVICE = "BOMBEIROS";
                Intent fire = getIntent();
                fire.putExtra("type_service", TYPE_SERVICE);
                fire.putExtra("image_service", IMAGE_SERVICE);
                fire.putExtra("name_service", NAME_SERVICE);
                setResult(Activity.RESULT_OK, fire);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        TYPE_SERVICE = 0;
        NAME_SERVICE = null;
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
