package com.simples.acesso.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.telephony.TelephonyManager;

import static android.content.Context.TELEPHONY_SERVICE;

public class Cellphone {

    Activity activity;

    public Cellphone(Activity activity){
        this.activity = activity;
    }

    @SuppressLint("MissingPermission")
    public String get () {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
        String cellphone = null;
        if (telephonyManager != null) {
            cellphone = telephonyManager.getLine1Number().substring(3);

        }
        return cellphone;
    }
}
