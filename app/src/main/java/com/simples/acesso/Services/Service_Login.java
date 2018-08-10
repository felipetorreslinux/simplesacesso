package com.simples.acesso.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.simples.acesso.API.API;
import com.simples.acesso.Models.SlidesIntro;
import com.simples.acesso.R;
import com.simples.acesso.Utils.MaskCellPhone;
import com.simples.acesso.Utils.PreLoads;
import com.simples.acesso.Views.Slides_Intro;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Login {

    Activity activity;
    AlertDialog.Builder builder;

    public Service_Login(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
    }

    public void login (String cellphone, String password){
        AndroidNetworking.post(API.URL_DEV+"/Modules/Login.php")
        .addBodyParameter("cellphone", MaskCellPhone.unmask(cellphone))
        .addBodyParameter("password", password)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            // Success
                            PreLoads.close();
                            final int id = response.getJSONObject("profile").getInt("id");
//                            Intent intent = new Intent(activity, Slides_Intro.class);
//                            intent.putExtra("id", id);
//                            activity.startActivity(intent);
                            builder.setTitle(R.string.app_name);
                            builder.setMessage("Você logou com sucesso");
                            builder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PreLoads.open(activity, null, "Saindo", false);
                                    logout(id);
                                }
                            });
                            builder.create().show();
                            break;
                        case 100:
                            // User is online
                            PreLoads.close();
                            final int user = response.getInt("id");
                            builder.setTitle("Ops!!!");
                            builder.setMessage("Você já está logado em outro aparelho");
                            builder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PreLoads.open(activity, null, "Saindo", false);
                                    logout(user);
                                }
                            });
                            builder.create().show();
                            break;

                        case 500:
                            //Not found User
                            PreLoads.close();
                            builder.setTitle("Ops!!!");
                            builder.setMessage("Telefone e senha inválidos");
                            builder.setPositiveButton("Ok", null);
                            builder.create().show();
                            break;
                    }
                }catch (JSONException e){}
            }

            @Override
            public void onError(ANError anError) {
                PreLoads.close();
                API.ErrorSever(activity, anError.getErrorCode());
            }
        });
    }

    public void logout (int id){
        AndroidNetworking.post(API.URL_DEV+"/Modules/Logout.php")
            .addBodyParameter("id", String.valueOf(id))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                PreLoads.close();
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Você saiu com sucesso");
                                builder.setPositiveButton("Ok",  null);
                                builder.create().show();
                                break;
                            default:
                                PreLoads.close();
                                builder.setTitle(R.string.app_name);
                                builder.setMessage(response.getString("message"));
                                builder.setPositiveButton("Ok",  null);
                                builder.create().show();
                                break;
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    PreLoads.close();
                    API.ErrorSever(activity, anError.getErrorCode());
                }
            });
    }

}
