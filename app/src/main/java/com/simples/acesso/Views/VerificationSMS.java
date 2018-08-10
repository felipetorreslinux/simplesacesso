package com.simples.acesso.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.simples.acesso.R;

public class VerificationSMS extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar_verification_sms;
    String cellphone;
    TextView info_text_verification_sms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_sms);

        cellphone = getIntent().getExtras().getString("cellphone");
//        PhoneNumberFirebase.send(this, MaskCellPhone.unmask(cellphone), getWindow().getDecorView());

        toolbar_verification_sms = (Toolbar) findViewById(R.id.toolbar_verification_sms);
        toolbar_verification_sms.setNavigationIcon(R.drawable.ic_back_white);
        toolbar_verification_sms.setTitle(R.string.title_verification_sms);
        toolbar_verification_sms.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar_verification_sms.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        info_text_verification_sms = (TextView) findViewById(R.id.info_text_verification_sms);
        info_text_verification_sms.setText("Acabamos de enviar um código via SMS para o número "+cellphone+".\nInforme no campo abaixo o código recebido para finalizar seu cadastro.");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
