package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    Service_Login serviceLogin;
    Cellphone cellphone;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        builder = new AlertDialog.Builder(this);
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
        openOptionsLogin();

    }

    private void openOptionsLogin(){
        if(!cellphone.get().equals("") || cellphone.get() == null){
            View view = getLayoutInflater().inflate(R.layout.dialog_cellphone_login, null);
            builder.setMessage("Entrar com");
            builder.setView(view);
            builder.setCancelable(false);
            builder.setPositiveButton("Outro número", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Keyboard.open(Login.this, cellphone_login);
                    cellphone_login.requestFocus();
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
                }
            });
        }else{
            Keyboard.open(Login.this, cellphone_login);
            cellphone_login.requestFocus();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
