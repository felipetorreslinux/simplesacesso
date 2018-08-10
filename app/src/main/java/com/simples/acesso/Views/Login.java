package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.simples.acesso.FireBase.PhoneNumberSMS.CallBackPhoneNumberValid;
import com.simples.acesso.FireBase.PhoneNumberSMS.PhoneNumberFirebase;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Login;
import com.simples.acesso.Utils.MaskCellPhone;
import com.simples.acesso.Utils.PreLoads;


public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText cellphone_login;
    EditText password_login;
    TextInputLayout layout_cellphone_login;
    TextInputLayout layout_password_login;
    Button button_login;

    Service_Login serviceLogin;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        serviceLogin = new Service_Login(this);

        layout_cellphone_login = (TextInputLayout) findViewById(R.id.layout_cellphone_login);
        layout_password_login = (TextInputLayout) findViewById(R.id.layout_password_login);

        cellphone_login = (EditText) findViewById(R.id.cellphone_login);
        password_login = (EditText) findViewById(R.id.password_login);

        cellphone_login.addTextChangedListener(MaskCellPhone.insert(cellphone_login));

        button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(this);

        try {
            cellphone_login.setText(getIntent().getExtras().getString("cellphone"));
            password_login.requestFocus();
        }catch (NullPointerException e){}

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login:
               String cellphone = cellphone_login.getText().toString().trim();
               String password = password_login.getText().toString().trim();
                if(cellphone.isEmpty()){
                    layout_cellphone_login.setErrorEnabled(true);
                    layout_password_login.setErrorEnabled(false);
                    layout_cellphone_login.setError("Informe seu número de telefone");
                }else if(cellphone.length() < 14){
                    layout_cellphone_login.setErrorEnabled(true);
                    layout_password_login.setErrorEnabled(false);
                    layout_cellphone_login.setError("Número de telefone inválido");
                }else if(password.isEmpty()){
                    layout_cellphone_login.setErrorEnabled(false);
                    layout_password_login.setErrorEnabled(true);
                    layout_password_login.setError("Informe sua senha");
                }else{
                    layout_cellphone_login.setErrorEnabled(false);
                    layout_password_login.setErrorEnabled(false);
                    PreLoads.open(this, null, "Autorizando...", true);
                    serviceLogin.login(cellphone, password);

                }
                break;
        }
    }
}
