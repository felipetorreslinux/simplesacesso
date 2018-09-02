package com.simples.acesso.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.simples.acesso.API.API;
import com.simples.acesso.R;
import com.simples.acesso.Utils.LoadingView;
import com.simples.acesso.Utils.MaskCellPhone;
import com.simples.acesso.Utils.PreLoads;
import com.simples.acesso.Views.Principal;
import com.simples.acesso.Views.VerificationSMS;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Login {

    Activity activity;
    AlertDialog.Builder builder;
    SharedPreferences.Editor editor;

    public Service_Login(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
        this.editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
    }

    public void check_cellphone (final String cellphone){
        LoadingView.open(activity, "Verificando");
        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                Intent intent = new Intent(activity, VerificationSMS.class);
                intent.putExtra("cellphone", cellphone);
                activity.startActivity(intent);
                LoadingView.close();
            }
        }.start();
    }

    public void login (final String cellphone, final String password){
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
                            int id = response.getJSONObject("profile").getInt("id");
                            String document = response.getJSONObject("profile").getString("document");
                            String name = response.getJSONObject("profile").getString("name");
                            String email = response.getJSONObject("profile").getString("email");
                            int created_at = response.getJSONObject("profile").getInt("created_at");
                            String token = response.getJSONObject("profile").getString("token");
                            System.out.println(token);
                            editor.putInt("id", id);
                            editor.putString("document", document);
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("cellphone", cellphone);
                            editor.putString("password", password);
                            editor.putInt("created_at", created_at);
                            editor.putString("token", token);
                            editor.commit();
                            if(editor.commit()){
                                Intent intent = new Intent(activity, Principal.class);
                                activity.startActivity(intent);
                                activity.finishAffinity();
                            }
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
                                editor.putInt("id", 0);
                                editor.commit();
                                activity.finish();
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
