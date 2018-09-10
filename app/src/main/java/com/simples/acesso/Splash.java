package com.simples.acesso;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.simples.acesso.Models.SlidesIntro;
import com.simples.acesso.Views.Login;
import com.simples.acesso.Views.Principal;
import com.simples.acesso.Views.Slides_Intro;

public class Splash extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        initializeApp();

    }

    private void initializeApp() {
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        if(sharedPreferences != null){
            if(sharedPreferences.getInt("id", 0) != 0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Splash.this, Principal.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Splash.this, Slides_Intro.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        }

    }
}
