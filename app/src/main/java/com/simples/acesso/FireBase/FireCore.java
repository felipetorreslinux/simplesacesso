package com.simples.acesso.FireBase;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class FireCore extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
