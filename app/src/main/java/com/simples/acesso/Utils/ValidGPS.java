package com.simples.acesso.Utils;

import android.app.Activity;
import android.location.LocationManager;

import static android.content.Context.LOCATION_SERVICE;

public class ValidGPS {

    public static boolean check(final Activity activity){
        LocationManager service = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            return false;
        }else{
            return true;
        }
    }
}
