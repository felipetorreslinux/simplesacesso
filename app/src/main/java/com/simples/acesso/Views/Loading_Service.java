package com.simples.acesso.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.simples.acesso.R;

public class Loading_Service extends AppCompatActivity {

    TextView text_service_send;
    LottieAnimationView LottieAnimationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_loading_service);


        LottieAnimationView = findViewById(R.id.animation_view);
        text_service_send = findViewById(R.id.text_service_send);
        text_service_send.setText(getIntent().getExtras().getString("service_send"));


    }

    @Override
    public void onBackPressed() {

    }
}
