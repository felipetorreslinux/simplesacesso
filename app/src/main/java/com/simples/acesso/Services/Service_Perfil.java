package com.simples.acesso.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.simples.acesso.API.API;
import com.simples.acesso.Utils.MaskCellPhone;
import com.simples.acesso.Utils.PreLoads;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Perfil {

    Activity activity;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    public Service_Perfil(Activity activity){
        this.activity = activity;
        this.editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
        this.sharedPreferences = activity.getSharedPreferences("profile", Context.MODE_PRIVATE);
    }

    public void change (final String cellphone, final String document, final String email, final String name){
        AndroidNetworking.post(API.URL_DEV+"Modules/ChangeAccountApp.php")
        .addBodyParameter("id", String.valueOf(sharedPreferences.getInt("id", 0)))
        .addBodyParameter("cellphone", MaskCellPhone.unmask(cellphone))
        .addBodyParameter("document", document)
        .addBodyParameter("email", email)
        .addBodyParameter("name", name)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            PreLoads.close();
                            editor.putString("document", document);
                            editor.putString("email", email);
                            editor.putString("name", name);
                            editor.putString("cellphone", cellphone);
                            editor.commit();
                            if(editor.commit()){
                                Intent intent = activity.getIntent();
                                activity.setResult(Activity.RESULT_OK, intent);
                                activity.finish();
                            }

                            break;
                        default:
                            PreLoads.close();
                            API.ErrorSever(activity, response.getInt("code"));
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
