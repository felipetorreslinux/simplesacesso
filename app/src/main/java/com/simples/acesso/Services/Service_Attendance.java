package com.simples.acesso.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simples.acesso.API.API;
import com.simples.acesso.R;
import com.simples.acesso.Utils.MaskCellPhone;
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
    SharedPreferences profile;
    SharedPreferences sintomas;
    AlertDialog.Builder builder;

    public Service_Attendance(Activity activity){
        this.activity = activity;
        this.profile = activity.getSharedPreferences("profile", Context.MODE_PRIVATE);
        this.sintomas = activity.getSharedPreferences("sintomas", Context.MODE_PRIVATE);
        this.builder = new AlertDialog.Builder(activity);
    }

    public void check(final ProgressDialog progressDialog){
        final String[] cellphone = {MaskCellPhone.unmask(profile.getString("cellphone", ""))};
        AndroidNetworking.post(API.URL_DEV+"Modules/VerificaAtendimento.php")
                .addBodyParameter("cellphone", cellphone[0])
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int code = response.getInt("code");
                            switch (code){
                                case 100:
                                    progressDialog.setCancelable(false);
                                    progressDialog.setTitle(R.string.app_name);
                                    progressDialog.setMessage("Recuperando atendimento...");
                                    progressDialog.show();
                                    break;
                            }
                        }catch (JSONException e){}
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void register (final JSONObject jsonObject) throws JSONException{

        double lat = Double.parseDouble(profile.getString("lat", null));
        double lng = Double.parseDouble(profile.getString("lng", null));

        final String city = new Service_Location(activity).getCity(new LatLng(lat, lng));

        if(lista_sintomas.size() > 0){
            AndroidNetworking.post(API.URL_DEV+"Modules/AttendanceRegister.php")
            .addBodyParameter("id_service", String.valueOf(jsonObject.getInt("type_service")))
            .addBodyParameter("cellphone", "81996050289")
            .addBodyParameter("attendance_description_id", String.valueOf(lista_sintomas))
            .addBodyParameter("city", city != null ? city : "")
            .addBodyParameter("latitude", String.valueOf(lat))
            .addBodyParameter("longitude", String.valueOf(lng))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                Intent intent = new Intent(activity, Loading_Service.class);
                                intent.putExtra("service_send", "Chamando\n"+jsonObject.getString("name_service"));
                                intent.putExtra("id_attendance", response.getInt("id_attendance"));
                                activity.startActivity(intent);
                                break;
                            case 675:
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Sua cidade "+city+" ainda não faz parte de nossos serviços");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.finish();
                                    }
                                });
                                builder.create().show();
                                break;

                            case 655:
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Sua cidade "+city+" se encontra desativada de nossos serviços");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.finish();
                                    }
                                });
                                builder.create().show();
                                break;

                            case 3320:
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Você tem um solicitação de atendimento em aberto com "+jsonObject.getString("name_service")+"");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.finish();
                                    }
                                });
                                builder.create().show();
                                break;
                            default:
                                activity.finish();
                                break;
                        }
                    }catch (JSONException e){

                    }
                }
                @Override
                public void onError(ANError anError) {
                    API.ErrorSever(activity, anError.getErrorCode());
                }
            });
        }else{
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.info_service_emergency);
            builder.setPositiveButton("Escolher", null);
            builder.create().show();
        }
    }
}
