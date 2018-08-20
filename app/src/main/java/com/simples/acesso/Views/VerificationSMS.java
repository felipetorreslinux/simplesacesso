package com.simples.acesso.Views;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.Keyboard;
import com.simples.acesso.Utils.Notifications;
import com.simples.acesso.Utils.PreLoads;

import java.util.Random;

public class VerificationSMS extends AppCompatActivity implements View.OnClickListener {

    AlertDialog.Builder builder;

    Toolbar toolbar;
    String cellphone;
    String password;
    TextView info_text_verification_sms;

    TextInputLayout layout_code_sms;

    EditText item_code_one;
    EditText item_code_two;
    EditText item_code_tree;
    EditText item_code_four;

    int codeSMS = 0;

    Service_Login serviceLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_sms);
        serviceLogin = new Service_Login(this);
        builder = new AlertDialog.Builder(this);
        cellphone = getIntent().getExtras().getString("cellphone");
        password = getIntent().getExtras().getString("password");
//        PhoneNumberFirebase.send(this, MaskCellPhone.unmask(cellphone), getWindow().getDecorView());
        createToolbar(toolbar);

        info_text_verification_sms = (TextView) findViewById(R.id.info_text_verification_sms);
        info_text_verification_sms.setText("Acabamos de enviar um código via SMS.\n" +
                "Informe no campo abaixo o código recebido para finalizar seu cadastro.");

        layout_code_sms = findViewById(R.id.layout_code_sms);

        item_code_one = findViewById(R.id.item_code_one);
        item_code_two = findViewById(R.id.item_code_two);
        item_code_tree = findViewById(R.id.item_code_tree);
        item_code_four = findViewById(R.id.item_code_four);

        codeSMSValid();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Random ran = new Random();
                codeSMS = ran.nextInt(9999);
                Notifications.create(VerificationSMS.this, 1000, "Validação Acesso Simples", "Código: " + codeSMS , R.drawable.ic_icon_splash);
            }
        }, 2500);

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.toolbar_verification_sms);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Validar "+cellphone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
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

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void codeSMSValid(){
        item_code_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(count);
                if(s.length() > 0){
                    item_code_two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        item_code_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    item_code_tree.requestFocus();
                }else{
                    item_code_one.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        item_code_tree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    item_code_four.requestFocus();
                }else{
                    item_code_two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        item_code_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    String code1 = item_code_one.getText().toString().trim();
                    String code2 = item_code_two.getText().toString().trim();
                    String code3 = item_code_tree.getText().toString().trim();
                    String code4 = item_code_four.getText().toString().trim();
                    int code = Integer.parseInt(code1+""+code2+""+code3+""+code4);

                    PreLoads.open(VerificationSMS.this, null, "Validando código", false);

                    if(code == codeSMS){

                        item_code_one.setText(null);
                        item_code_two.setText(null);
                        item_code_tree.setText(null);
                        item_code_four.setText(null);
                        Keyboard.close(VerificationSMS.this, getWindow().getDecorView());
                        serviceLogin.login(cellphone, password);

                    }else{

                        PreLoads.close();
                        layout_code_sms.setErrorEnabled(true);
                        layout_code_sms.setError("Código inválido");
                        item_code_one.setText(null);
                        item_code_two.setText(null);
                        item_code_tree.setText(null);
                        item_code_four.setText(null);
                        item_code_one.requestFocus();

                    }
                }else{
                    item_code_tree.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

}
