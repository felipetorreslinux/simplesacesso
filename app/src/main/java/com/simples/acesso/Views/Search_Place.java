package com.simples.acesso.Views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.simples.acesso.R;

public class Search_Place extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    EditText text_search;
    ProgressBar progress_search;
    RecyclerView recycler_search;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_place);
        sharedPreferences = getSharedPreferences("attendance", MODE_PRIVATE);

        progress_search = findViewById(R.id.progress_search);
        progress_search.setVisibility(View.GONE);

        recycler_search = findViewById(R.id.recycler_search);
        recycler_search.setLayoutManager(new LinearLayoutManager(this));
        recycler_search.setHasFixedSize(true);
        recycler_search.setNestedScrollingEnabled(false);

        text_search = findViewById(R.id.text_search);
        text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 3){
                    progress_search.setVisibility(View.VISIBLE);
                }else{
                    progress_search.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



    }


    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
