package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.simples.acesso.FireBase.PhoneNumberSMS.CallBackPhoneNumberValid;
import com.simples.acesso.FireBase.PhoneNumberSMS.PhoneNumberFirebase;
import com.simples.acesso.Manifest;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.MaskCellPhone;
import com.simples.acesso.Utils.PreLoads;


public class Login extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    Service_Login serviceLogin;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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

        serviceLogin = new Service_Login(this);
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

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
