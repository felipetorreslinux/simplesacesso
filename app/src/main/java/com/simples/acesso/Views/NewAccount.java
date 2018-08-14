package com.simples.acesso.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.simples.acesso.R;
import com.simples.acesso.Services.Service_NewAccount;
import com.simples.acesso.Utils.MaskCellPhone;
import com.simples.acesso.Utils.PreLoads;

public class NewAccount extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    TextInputLayout layout_cellphone_newaccount;
    TextInputLayout layout_password_newaccount;
    TextInputLayout layout_conf_password_newaccount;

    EditText cellphone_newaccount;
    EditText password_newaccount;
    EditText conf_password_newaccount;

    CheckBox check_terms_newaccount;

    Button button_send_newaccount;

    Service_NewAccount serviceNewAccount;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newaccount);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);

        serviceNewAccount = new Service_NewAccount(this);

        createToolbar(toolbar);

        layout_cellphone_newaccount = (TextInputLayout) findViewById(R.id.layout_cellphone_newaccount);
        layout_password_newaccount = (TextInputLayout) findViewById(R.id.layout_password_newaccount);
        layout_conf_password_newaccount = (TextInputLayout) findViewById(R.id.layout_conf_password_newaccount);

        cellphone_newaccount = (EditText) findViewById(R.id.cellphone_newaccount);
        password_newaccount = (EditText) findViewById(R.id.password_newaccount);
        conf_password_newaccount = (EditText) findViewById(R.id.conf_password_newaccount);

        cellphone_newaccount.addTextChangedListener(MaskCellPhone.insert(cellphone_newaccount));

        check_terms_newaccount = (CheckBox) findViewById(R.id.check_terms_newaccount);

        button_send_newaccount = (Button) findViewById(R.id.button_send_newaccount);
        button_send_newaccount.setOnClickListener(this);

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.toolbar_newaccount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_newaccount);
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
            case R.id.button_send_newaccount:
                validRegister();
                break;
        }
    }

    private void validRegister (){
        String cellphone = cellphone_newaccount.getText().toString().trim();
        String password = password_newaccount.getText().toString().trim();
        String conf_password = conf_password_newaccount.getText().toString().trim();
        boolean check_terms = check_terms_newaccount.isChecked();

        if(cellphone.isEmpty()) {
            layout_cellphone_newaccount.setErrorEnabled(true);
            layout_password_newaccount.setErrorEnabled(false);
            layout_conf_password_newaccount.setErrorEnabled(false);
            layout_cellphone_newaccount.setError("Informe seu número de telefone");
            cellphone_newaccount.requestFocus();
        }else if(cellphone.length()< 14){
            layout_cellphone_newaccount.setErrorEnabled(true);
            layout_password_newaccount.setErrorEnabled(false);
            layout_conf_password_newaccount.setErrorEnabled(false);
            layout_cellphone_newaccount.setError("Informe um número de telefone válido");
            cellphone_newaccount.requestFocus();
        }else if(password.isEmpty()){
            layout_cellphone_newaccount.setErrorEnabled(false);
            layout_password_newaccount.setErrorEnabled(true);
            layout_conf_password_newaccount.setErrorEnabled(false);
            layout_password_newaccount.setError("Crie sua senha");
            password_newaccount.requestFocus();
        }else if(password.length() < 6){
            layout_cellphone_newaccount.setErrorEnabled(false);
            layout_password_newaccount.setErrorEnabled(true);
            layout_conf_password_newaccount.setErrorEnabled(false);
            layout_password_newaccount.setError("Sua senha deve conter no mínimo 6 digitos");
            password_newaccount.requestFocus();
        }else if(conf_password.isEmpty()){
            layout_cellphone_newaccount.setErrorEnabled(false);
            layout_password_newaccount.setErrorEnabled(false);
            layout_conf_password_newaccount.setErrorEnabled(true);
            layout_conf_password_newaccount.setError("Confirme sua senha");
            conf_password_newaccount.requestFocus();
        }else if(!password.equals(conf_password)) {
            layout_cellphone_newaccount.setErrorEnabled(false);
            layout_password_newaccount.setErrorEnabled(true);
            layout_conf_password_newaccount.setErrorEnabled(true);
            layout_password_newaccount.setError("Senhas não conferem");
            layout_conf_password_newaccount.setError("Senhas não conferem");
        }else if(check_terms == false){
            Toast.makeText(this, "Você precisa aceitar os termos para prosseguir com seus cadastro", Toast.LENGTH_LONG).show();
        }else{
            PreLoads.open(this, null, "Cadastrando usuário", false);
            serviceNewAccount.register(cellphone, password);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
