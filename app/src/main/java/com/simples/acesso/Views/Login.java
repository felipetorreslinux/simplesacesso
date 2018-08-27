package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simples.acesso.FireBase.PhoneNumberSMS.CallBackPhoneNumberValid;
import com.simples.acesso.FireBase.PhoneNumberSMS.PhoneNumberFirebase;
import com.simples.acesso.Manifest;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.Cellphone;
import com.simples.acesso.Utils.Keyboard;
import com.simples.acesso.Utils.MaskCellPhone;
import com.simples.acesso.Utils.PreLoads;


public class Login extends AppCompatActivity implements View.OnClickListener {


    AlertDialog.Builder builder;
    Toolbar toolbar;

    TextInputLayout layout_cellphone_login;
    EditText cellphone_login;
    FloatingActionButton button_login;

    Service_Login serviceLogin;
    Cellphone cellphone;

    @SuppressLint({"NewApi", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        cellphone = new Cellphone(this);
        serviceLogin = new Service_Login(this);
        createToolbar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_PHONE_NUMBERS,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.CALL_PHONE
        }, 1);

        layout_cellphone_login = findViewById(R.id.layout_cellphone_login);
        cellphone_login = findViewById(R.id.cellphone_login);
        cellphone_login.addTextChangedListener(MaskCellPhone.insert(cellphone_login));

        cellphone_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 14){
                    button_login.setVisibility(View.VISIBLE);
                }else{
                    button_login.setVisibility(View.GONE);
                }
            }
        });

        button_login = findViewById(R.id.button_login);
        button_login.setVisibility(View.GONE);
        button_login.setOnClickListener(this);

        openOptionsLogin();

    }

    @SuppressLint("RestrictedApi")
    private void openOptionsLogin(){
        if(!cellphone.get().equals("") || cellphone.get() == null){
            builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_cellphone_login, null);
            builder.setMessage("Começar com");
            builder.setView(view);
            builder.setCancelable(false);
            builder.setPositiveButton("Outro número", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Keyboard.open(Login.this, cellphone_login);
                    cellphone_login.requestFocus();
                    button_login.setVisibility(View.GONE);
                }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            final TextView number_cellphone = view.findViewById(R.id.number_cellphone);
            number_cellphone.setText(cellphone.get());
            number_cellphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    cellphone_login.setText(number_cellphone.getText().toString().trim());
                    button_login.setVisibility(View.VISIBLE);
                }
            });
        }else{
            Keyboard.open(Login.this, cellphone_login);
            button_login.setVisibility(View.GONE);
            cellphone_login.requestFocus();
        }

    }


    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_purple);
        toolbar = (Toolbar) findViewById(R.id.login_topbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Entrar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
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
            case R.id.button_login:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
                View loading = getLayoutInflater().inflate(R.layout.view_loading, null);
                builder.setView(loading);
                builder.setCancelable(false);
                builder.create().show();
                TextView info_loading = loading.findViewById(R.id.info_loading);
                info_loading.setText("Verificando Telefone");
                serviceLogin.check_cellphone(MaskCellPhone.unmask(cellphone_login.getText().toString().trim()));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
