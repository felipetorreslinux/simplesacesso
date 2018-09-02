package com.simples.acesso.Utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.simples.acesso.R;

public class LoadingView {

    static AlertDialog.Builder builder;
    static AlertDialog alertDialog;

    public static void open (Activity activity, String message){
        builder = new AlertDialog.Builder(activity, R.style.CustomDialog);
        View loading = activity.getLayoutInflater().inflate(R.layout.view_loading, null);
        builder.setView(loading);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        TextView info_loading = loading.findViewById(R.id.info_loading);
        info_loading.setText(message);
    }

    public static void close (){
        alertDialog.dismiss();
    }

}
