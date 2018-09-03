package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.simples.acesso.R;

public class Loading_Service extends AppCompatActivity implements View.OnClickListener{

    TextView text_service_send;
    LottieAnimationView lottieAnimationView;
    Button button_send_service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_loading_service);

        lottieAnimationView = findViewById(R.id.animation_view);
        text_service_send = findViewById(R.id.text_service_send);

        button_send_service = findViewById(R.id.button_send_service);
        button_send_service.setVisibility(View.GONE);
        button_send_service.setOnClickListener(this);

        countTime();

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_send_service:
                countTime();
                break;
        }
    }

    private void countTime(){
        new CountDownTimer(60000, 1000){
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                text_service_send.setText(getIntent().getExtras().getString("service_send")+"\n\n"+String.valueOf(time));
                button_send_service.setVisibility(View.GONE);
                lottieAnimationView.playAnimation();
            }
            @Override
            public void onFinish() {
                button_send_service.setText("Tente novamente");
                button_send_service.setVisibility(View.VISIBLE);
                text_service_send.setText(getIntent().getExtras().getString("service_send"));
                lottieAnimationView.pauseAnimation();
            }
        }.start();
    }

}
