package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.PhoneAuthProvider;
import com.simples.acesso.FireBase.PhoneNumberSMS.CallBackPhoneNumberValid;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.LoadingView;

import java.util.concurrent.TimeUnit;

public class VerificationSMS extends AppCompatActivity implements View.OnClickListener {

    AlertDialog.Builder builder;
    Toolbar toolbar;
    String cellphone;
    TextView info_valid_sms;

    TextInputLayout layout_code_sms;
    EditText code_sms;

    FloatingActionButton button_sms;
    MenuItem count;

    Service_Login serviceLogin;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_sms);
        serviceLogin = new Service_Login(this);
        builder = new AlertDialog.Builder(this);
        cellphone = getIntent().getExtras().getString("cellphone");
        createToolbar(toolbar);

        info_valid_sms = findViewById(R.id.info_valid_sms);
        info_valid_sms.setText("Acabamos de enviar um código SMS para o número "+cellphone+".\n\n" +
                "Informe o código recebido abaixo para validarmos seu acesso.");

        layout_code_sms = findViewById(R.id.layout_code_sms);

        code_sms = findViewById(R.id.code_sms);

        button_sms = findViewById(R.id.button_sms);
        button_sms.setVisibility(View.GONE);
        button_sms.setOnClickListener(this);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+55"+cellphone, 60, TimeUnit.SECONDS, VerificationSMS.this,
                new CallBackPhoneNumberValid(VerificationSMS.this, code_sms, button_sms));

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_purple);
        toolbar = (Toolbar) findViewById(R.id.toolbar_verification_sms);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Código SMS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sms, menu);

        count = menu.findItem(R.id.count_sms);
        count();
        return true;
    }

    private void count(){
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                count.setTitle(""+millisUntilFinished / 1000);
            }
            public void onFinish() {
                count.setIcon(getResources().getDrawable(R.drawable.ic_refresh));
                count.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+55"+cellphone, 60, TimeUnit.SECONDS, VerificationSMS.this,
                                new CallBackPhoneNumberValid(VerificationSMS.this, code_sms, button_sms));
                        count();
                        return false;
                    }
                });

            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_sms:
                String code = code_sms.getText().toString().trim();
                if(code.isEmpty()){
                    layout_code_sms.setErrorEnabled(true);
                    layout_code_sms.setError("informe o código");
                    code_sms.requestFocus();
                }else if(code_sms.getText().length() < 6) {
                    layout_code_sms.setErrorEnabled(true);
                    layout_code_sms.setError("Código inválido");
                    code_sms.requestFocus();
                }else{
                    layout_code_sms.setErrorEnabled(false);
                    layout_code_sms.setError(null);
                    LoadingView.open(this, "Validando");
                    new CountDownTimer(4000, 1000) {
                        public void onTick(long millisUntilFinished) {}
                        public void onFinish() {
                            Intent intent = new Intent(VerificationSMS.this, Principal.class);
                            intent.putExtra("cellphone", cellphone);
                            startActivity(intent);
                            finishAffinity();
                            LoadingView.close();
                        }
                    }.start();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
