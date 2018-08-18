package com.simples.acesso.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.simples.acesso.API.API;
import com.simples.acesso.R;
import com.simples.acesso.Utils.MaskCellPhone;
import com.simples.acesso.Utils.PreLoads;
import com.simples.acesso.Views.VerificationSMS;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public class Service_NewAccount {

    Activity activity;
    AlertDialog.Builder builder;

    public Service_NewAccount(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
    }

    public void register (final String cellphone, String password){
        AndroidNetworking.post(API.URL_DEV+"Modules/NewAccountApp.php")
            .addBodyParameter("cellphone", MaskCellPhone.unmask(cellphone))
            .addBodyParameter("password",password)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                PreLoads.close();
                                Intent intent = new Intent(activity, VerificationSMS.class);
                                intent.putExtra("cellphone", cellphone);
                                activity.startActivity(intent);
                                activity.finish();
                                break;

                            case 250:
                                PreLoads.close();
                                //Usuário já cadastrado
                                builder.setTitle("Ops!!!");
                                builder.setMessage("Telefone já cadastrado");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Ok", null);
                                builder.create().show();
                                break;

                            case 150:
                                PreLoads.close();
                                //Erro ao registrar
                                builder.setTitle("Ops!!!");
                                builder.setMessage("Não foi possível cadastrar seus dados no momento");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Ok", null);
                                builder.create().show();
                                break;

                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    API.ErrorSever(activity, anError.getErrorCode());
                }
            });
    }

}
