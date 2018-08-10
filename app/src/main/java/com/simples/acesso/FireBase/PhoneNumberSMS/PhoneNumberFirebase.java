package com.simples.acesso.FireBase.PhoneNumberSMS;

import android.app.Activity;
import android.view.View;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumberFirebase {

    public static void send(Activity activity, String number, View view){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+55"+number,
                60,
                TimeUnit.SECONDS,
                activity,
                new CallBackPhoneNumberValid(view));
    }
}
