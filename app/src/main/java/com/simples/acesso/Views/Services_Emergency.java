package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simples.acesso.Adapters.Adapter_Attendance;
import com.simples.acesso.Models.Attendance_Model;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Services_Emergency extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences servicos;
    List<Attendance_Model> list = new ArrayList<Attendance_Model>();
    int TYPE_SERVICE;
    String LOCAL_USER;

    Toolbar toolbar;
    TextView local_user_service;
    TextView edit_local_service;

    RecyclerView list_services;

    Button button_service_emergency;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_emergency);

        servicos = getSharedPreferences("attendance", MODE_PRIVATE);

//        new Service_Location(this).getPlaceAdress("Recife");

        createToolbar(toolbar);

        list_services = findViewById(R.id.list_services);
        list_services.setLayoutManager(new LinearLayoutManager(this));
        list_services.setHasFixedSize(false);
        list_services.setNestedScrollingEnabled(false);

        list.clear();
        Attendance_Model not_info = new Attendance_Model(0,0, "NÃO SEI INFORMAR", true);
        list.add(not_info);

        edit_local_service = findViewById(R.id.edit_local_service);
        edit_local_service.setOnClickListener(this);

        listServices();

        button_service_emergency = findViewById(R.id.button_service_emergency);
        button_service_emergency.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void listServices() {
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

    @SuppressLint("NewApi")
    private void createToolbar(Toolbar toolbar) {

        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.toolbar_service_emergency);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        TYPE_SERVICE = getIntent().getExtras().getInt("type_service");
        LOCAL_USER = getIntent().getExtras().getString("local_user");

        local_user_service = findViewById(R.id.local_user_service);
        local_user_service.setText(LOCAL_USER);

        switch (TYPE_SERVICE){
            case 1:
                getSupportActionBar().setTitle(R.string.send_police);
                break;
            case 2:
                getSupportActionBar().setTitle(R.string.send_samu);
                break;
            case 3:
                getSupportActionBar().setTitle(R.string.send_fireman);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_local_service:
                startActivityForResult(new Intent(this, Search_Place.class), 1000);
                break;

            case R.id.button_service_emergency:
                Intent intent = new Intent(this, Loading_Service.class);
                intent.putExtra("service_send", "Solicitando\nAguarde...");
                startActivity(intent);
                break;

        }
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
                    local_user_service.setText(data.getExtras().getString("local_user"));
                }else{
                    createToolbar(toolbar);
                    listServices();
                }
                break;
        }
    }
}
