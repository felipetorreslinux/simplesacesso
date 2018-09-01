package com.simples.acesso.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.PhoneAuthProvider;
import com.simples.acesso.Adapters.SlidesIntroAdapter;
import com.simples.acesso.Models.SlidesIntro;
import com.simples.acesso.R;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class Slides_Intro extends AppCompatActivity implements View.OnClickListener{

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    UltraViewPager ultraviewpager_intro;
    List<SlidesIntro> list_slides = new ArrayList<SlidesIntro>();

    TextView button_open_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slides_intro);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        ultraviewpager_intro = (UltraViewPager) findViewById(R.id.ultraviewpager_intro);
        button_open_login = (TextView) findViewById(R.id.button_open_login);
        button_open_login.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list_slides.clear();
        slides();
    }

    private void slides (){

        SlidesIntro slide01 = new SlidesIntro(R.drawable.ic_police, "POLÍCIA","Polícia na palma da sua mão");
        SlidesIntro slide02 = new SlidesIntro(R.drawable.ic_ambulace, "SAMU", "Chame o Samu com apenas um aperto de botão");
        SlidesIntro slide03 = new SlidesIntro(R.drawable.ic_fireman, "BOMBEIROS","Os Bombeiros sempre disponível para você");

        list_slides.add(slide01);
        list_slides.add(slide02);
        list_slides.add(slide03);

        SlidesIntroAdapter adapter = new SlidesIntroAdapter(this, list_slides);

        ultraviewpager_intro.setAdapter(adapter);
        ultraviewpager_intro.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultraviewpager_intro.initIndicator();
        ultraviewpager_intro.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(getResources().getColor(R.color.colorPrimaryDark))
                .setNormalColor(getResources().getColor(R.color.colorPrimaryLight))
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        ultraviewpager_intro.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        ultraviewpager_intro.getIndicator().setMargin(0,25, 0 ,25);
        ultraviewpager_intro.getIndicator().build();
        ultraviewpager_intro.setInfiniteLoop(false);
        ultraviewpager_intro.setPageTransformer(false, new UltraScaleTransformer());
        ultraviewpager_intro.setAutoScroll(2000);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_open_login:
                Intent login = new Intent(this, Login.class);
                startActivity(login);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1000:
                if(resultCode == Activity.RESULT_OK){
                    String cellphone = data.getExtras().getString("cellphone");
                    Intent login = new Intent(this, Login.class);
                    login.putExtra("cellphone", cellphone);
                    startActivity(login);
                }
                break;
        }
    }
}
