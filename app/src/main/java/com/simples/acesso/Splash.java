package com.simples.acesso;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.simples.acesso.Views.Login;
import com.simples.acesso.Views.Slides_Intro;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        initializeApp();

    }

    private void initializeApp() {
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
