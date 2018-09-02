package com.simples.acesso.Views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Perfil;
import com.simples.acesso.Utils.MaskCPF;
import com.simples.acesso.Utils.MaskCellPhone;
import com.simples.acesso.Utils.PreLoads;
import com.simples.acesso.Utils.ValidEmail;
import com.simples.acesso.Utils.ValidaCPF;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Perfil extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;

    Toolbar toolbar;

    ImageView image_profile;

    TextInputLayout layout_document_perfil;
    TextInputLayout layout_name_perfil;
    TextInputLayout layout_email_perfil;
    TextInputLayout layout_cellphone_perfil;

    EditText document_perfil;
    EditText name_perfil;
    EditText email_perfil;
    EditText cellphone_perfil;

    Service_Perfil servicePerfil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        servicePerfil = new Service_Perfil(this);

        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);

        createToolbar(toolbar);

        image_profile = findViewById(R.id.image_profile);

        layout_document_perfil = findViewById(R.id.layout_document_perfil);
        layout_name_perfil = findViewById(R.id.layout_name_perfil);
        layout_email_perfil = findViewById(R.id.layout_email_perfil);
        layout_cellphone_perfil = findViewById(R.id.layout_cellphone_perfil);

        document_perfil = findViewById(R.id.document_perfil);
        document_perfil.addTextChangedListener(MaskCPF.insert(document_perfil));
        name_perfil = findViewById(R.id.name_perfil);
        email_perfil = findViewById(R.id.email_perfil);
        cellphone_perfil = findViewById(R.id.cellphone_perfil);
        cellphone_perfil.addTextChangedListener(MaskCellPhone.insert(cellphone_perfil));


        if(sharedPreferences.getString("image", "").isEmpty()){
            Picasso.with(this)
                    .load(R.drawable.no_image)
                    .transform(new CropCircleTransformation())
                    .resize(200,200)
                    .into(image_profile);
        }else{
            Picasso.with(this)
                    .load(sharedPreferences.getString("image", ""))
                    .transform(new CropCircleTransformation())
                    .resize(200,200)
                    .into(image_profile);
        }



        document_perfil.setText(sharedPreferences.getString("document", ""));
        name_perfil.setText(sharedPreferences.getString("name", ""));
        email_perfil.setText(sharedPreferences.getString("email", ""));
        cellphone_perfil.setText(sharedPreferences.getString("cellphone", ""));

        if(!document_perfil.getText().toString().isEmpty()){
            document_perfil.setEnabled(false);
            name_perfil.setEnabled(false);
        }

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.toolbar_perfil);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil_profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.save_dados_profile:
                savePerfil();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    private void savePerfil() {
        String document = document_perfil.getText().toString().trim();
        String name = name_perfil.getText().toString().trim();
        String email = email_perfil.getText().toString().trim();
        String cellphone = cellphone_perfil.getText().toString().trim();

        if(document.isEmpty()){

            layout_document_perfil.setErrorEnabled(true);
            layout_name_perfil.setErrorEnabled(false);
            layout_email_perfil.setErrorEnabled(false);
            layout_cellphone_perfil.setErrorEnabled(false);

            layout_document_perfil.setError("Informe seu CPF");
            document_perfil.requestFocus();

        }else if(ValidaCPF.check(MaskCPF.unmask(document)) == false){

            layout_document_perfil.setErrorEnabled(true);
            layout_name_perfil.setErrorEnabled(false);
            layout_email_perfil.setErrorEnabled(false);
            layout_cellphone_perfil.setErrorEnabled(false);

            layout_document_perfil.setError("CPF inválido");
            document_perfil.requestFocus();

        }else if(name.isEmpty()){

            layout_document_perfil.setErrorEnabled(false);
            layout_name_perfil.setErrorEnabled(true);
            layout_email_perfil.setErrorEnabled(false);
            layout_cellphone_perfil.setErrorEnabled(false);

            layout_name_perfil.setError("Informe seu nome");
            name_perfil.requestFocus();

        }else if(email.isEmpty()){

            layout_document_perfil.setErrorEnabled(false);
            layout_name_perfil.setErrorEnabled(false);
            layout_email_perfil.setErrorEnabled(true);
            layout_cellphone_perfil.setErrorEnabled(false);

            layout_email_perfil.setError("Informe seu email");
            email_perfil.requestFocus();

        }else if(ValidEmail.check(email) == false){

            layout_document_perfil.setErrorEnabled(false);
            layout_name_perfil.setErrorEnabled(false);
            layout_email_perfil.setErrorEnabled(true);
            layout_cellphone_perfil.setErrorEnabled(false);

            layout_email_perfil.setError("Email inválido");
            email_perfil.requestFocus();

        }else if(cellphone.isEmpty()){

            layout_document_perfil.setErrorEnabled(false);
            layout_name_perfil.setErrorEnabled(false);
            layout_email_perfil.setErrorEnabled(false);
            layout_cellphone_perfil.setErrorEnabled(true);

            layout_cellphone_perfil.setError("Informe seu telefone");
            cellphone_perfil.requestFocus();

        }else if(cellphone.length() < 13){

            layout_document_perfil.setErrorEnabled(false);
            layout_name_perfil.setErrorEnabled(false);
            layout_email_perfil.setErrorEnabled(false);
            layout_cellphone_perfil.setErrorEnabled(true);

            layout_cellphone_perfil.setError("Telefone inválido");
            cellphone_perfil.requestFocus();

        }else {

            layout_document_perfil.setErrorEnabled(false);
            layout_name_perfil.setErrorEnabled(false);
            layout_email_perfil.setErrorEnabled(false);
            layout_cellphone_perfil.setErrorEnabled(false);

            PreLoads.open(this, null, "Atualizando informações", false);

            servicePerfil.change(cellphone, document, email, name);

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
