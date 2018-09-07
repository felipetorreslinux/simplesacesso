package com.simples.acesso.Views;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.simples.acesso.R;
import com.simples.acesso.Services.Service_NewAccount;
import com.simples.acesso.Utils.Keyboard;
import com.simples.acesso.Utils.MaskCPF;
import com.simples.acesso.Utils.MaskCellPhone;

import java.security.Key;

public class Minhas_Informacoes extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    EditText document_profile;
    EditText name_profile;
    EditText email_profile;
    EditText cellphone_profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_minhas_informacoes);
        createToolbar(toolbar);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        editor = getSharedPreferences("profile", MODE_PRIVATE).edit();

        document_profile = findViewById(R.id.document_profile);
        document_profile.addTextChangedListener(MaskCPF.insert(document_profile));
        name_profile = findViewById(R.id.name_profile);
        email_profile = findViewById(R.id.email_profile);
        cellphone_profile = findViewById(R.id.cellphone_profile);
        cellphone_profile.addTextChangedListener(MaskCellPhone.insert(cellphone_profile));

        String document = sharedPreferences.getString("document", "");
        String cellphone = sharedPreferences.getString("document", "");
        if(!document.isEmpty() && !cellphone.isEmpty()){
            document_profile.setEnabled(false);
            name_profile.setEnabled(false);
            cellphone_profile.setEnabled(false);
        }

        document_profile.setText(sharedPreferences.getString("document", ""));
        name_profile.setText(sharedPreferences.getString("name", ""));
        email_profile.setText(sharedPreferences.getString("email", ""));
        cellphone_profile.setText(sharedPreferences.getString("cellphone", ""));

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.toolbar_minhas_informacoes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_minhas_informacoes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_minhas_informacoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save_minhas_informacoes:
                salvarMinhasInfomacoes();
                break;
        }
        return true;
    }

    private void salvarMinhasInfomacoes() {

        String document = document_profile.getText().toString().trim();
        String name = name_profile.getText().toString().trim();
        String email = email_profile.getText().toString().trim();
        String cellphone = cellphone_profile.getText().toString().trim();

        if(document.isEmpty()){
            Keyboard.close(this, getWindow().getDecorView());
            Snackbar.make(getWindow().getDecorView(),
                    "Informe o seu CPF", Snackbar.LENGTH_SHORT).show();
        }else if(name.isEmpty()){
            Keyboard.close(this, getWindow().getDecorView());
            Snackbar.make(getWindow().getDecorView(),
                    "Informe o seu nome", Snackbar.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Keyboard.close(this, getWindow().getDecorView());
            Snackbar.make(getWindow().getDecorView(),
                    "Informe o seu email", Snackbar.LENGTH_SHORT).show();
        }else if(cellphone.isEmpty()){
            Keyboard.close(this, getWindow().getDecorView());
            Snackbar.make(getWindow().getDecorView(),
                    "Informe o seu telefone", Snackbar.LENGTH_SHORT).show();
        }else{
            editor.putString("document", document_profile.getText().toString().trim());
            editor.putString("name", name_profile.getText().toString().trim());
            editor.putString("email", email_profile.getText().toString().trim());
            editor.putString("cellphone", cellphone_profile.getText().toString().trim());
            editor.commit();
            finish();
        }


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
