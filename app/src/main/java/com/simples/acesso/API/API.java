package com.simples.acesso.API;

import android.app.Activity;
import android.app.AlertDialog;

public class API {

    public static String URL_DEV = "http://simplesacesso.com/";

    public static void ErrorSever (Activity activity, int code){
        AlertDialog.Builder builder  = new AlertDialog.Builder(activity);
        switch (code){
            case 0:

                break;
        }

    }
}
