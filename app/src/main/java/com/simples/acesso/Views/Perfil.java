package com.simples.acesso.Views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Perfil;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Perfil extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Toolbar toolbar;

    ImageView image_profile;
    TextView name_profile;
    TextView cellphone_profile;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri filePath;

    Service_Perfil servicePerfil;

    String cpf;

    LinearLayout item_edit_profile;
    LinearLayout item_hospital_profile;
    LinearLayout item_delegacia_profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        servicePerfil = new Service_Perfil(this);

        editor = getSharedPreferences("profile", MODE_PRIVATE).edit();
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        createToolbar(toolbar);

        image_profile = findViewById(R.id.image_profile);
        image_profile.setOnClickListener(this);

        cpf = sharedPreferences.getString("document", "");
        name_profile = findViewById(R.id.name_profile);
        name_profile.setText(sharedPreferences.getString("name", "Felipe Torres"));
        cellphone_profile = findViewById(R.id.cellphone_profile);
        cellphone_profile.setText(sharedPreferences.getString("cellphone", "(81) 99605-02089"));

        if(sharedPreferences.getString("image", "").isEmpty()){
            Picasso.get()
                    .load(R.drawable.no_image)
                    .transform(new CropCircleTransformation())
                    .resize(200,200)
                    .into(image_profile);
        }else{
            Picasso.get()
                    .load(Uri.parse(sharedPreferences.getString("image", "")))
                    .transform(new CropCircleTransformation())
                    .resize(200,200)
                    .into(image_profile);
        }

        item_edit_profile = findViewById(R.id.item_edit_profile);
        item_hospital_profile = findViewById(R.id.item_hospital_profile);
        item_delegacia_profile = findViewById(R.id.item_delegacia_profile);
        item_edit_profile.setOnClickListener(this);
        item_hospital_profile.setOnClickListener(this);
        item_delegacia_profile.setOnClickListener(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_profile:
                chooseImage();
                break;
            case R.id.item_hospital_profile:
                startActivity(new Intent(this, Hospitais.class));
                break;

            case R.id.item_delegacia_profile:
                startActivity(new Intent(this, Delegacias.class));
                break;

            case R.id.item_edit_profile:
                startActivity(new Intent(this, Minhas_Informacoes.class));
                break;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 72 && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            filePath = data.getData();
            CropImage.activity(filePath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(250,250)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                filePath = result.getUri();
                uploadImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        };
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 72);
    }

    private void uploadImage() {

        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Carregando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+cpf+".jpg");
            ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(getWindow().getDecorView(),
                                "Foto atualizada com sucesso", Snackbar.LENGTH_SHORT).show();

                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get()
                                        .load(uri)
                                        .transform(new CropCircleTransformation())
                                        .resize(200,200)
                                        .into(image_profile);
                                editor.putString("image", uri.toString());
                                editor.commit();
                                progressDialog.dismiss();
                            }
                        });

                        editor.putString("image", ref.getDownloadUrl().toString());
                        editor.commit();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Snackbar.make(getWindow().getDecorView(),
                                ""+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        editor.putString("image", "");
                        editor.commit();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setProgress((int) progress);
                    }
                });
        }
    }

}
