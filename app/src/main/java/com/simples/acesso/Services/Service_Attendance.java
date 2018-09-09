package com.simples.acesso.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simples.acesso.Views.Loading_Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.simples.acesso.Adapters.Adapter_Attendance.lista_sintomas;

public class Service_Attendance {

    Activity activity;
    SharedPreferences sharedPreferences;
    SharedPreferences sintomas;

    public Service_Attendance(Activity activity){
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences("profile", Context.MODE_PRIVATE);
        this.sintomas = activity.getSharedPreferences("sintomas", Context.MODE_PRIVATE);
    }

    public void register (final JSONObject jsonObject) throws JSONException{
        long id_attendance = new Date().getTime();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Attendance").child(String.valueOf(id_attendance));
        Map<String, Object> map = new HashMap<>();
        map.put("id_user", sharedPreferences.getInt("id", 0));
        map.put("document_user", sharedPreferences.getString("document", null));
        map.put("name_user", sharedPreferences.getString("name", null));
        map.put("email_user", sharedPreferences.getString("email", null));
        map.put("cellphone_user", sharedPreferences.getString("cellphone", null));
        map.put("name_service", jsonObject.getString("name_service"));
        map.put("local_service", jsonObject.getString("local_service").replace("\n", ""));
        map.put("latitude", Double.parseDouble(jsonObject.getString("latitude")));
        map.put("longitude", Double.parseDouble(jsonObject.getString("longitude")));
        map.put("is_attendance", false);
        map.put("created_at", id_attendance);
        map.put("messages", "");
        map.put("sintomas", lista_sintomas);
        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    try{
                        Intent intent = new Intent(activity, Loading_Service.class);
                        intent.putExtra("service_send", "Chamando\n"+jsonObject.getString("name_service"));
                        activity.startActivity(intent);
                    }catch (JSONException e){}
                }else{

                }
            }
        });
    }
}
