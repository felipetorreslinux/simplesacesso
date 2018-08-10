package com.simples.acesso.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simples.acesso.Models.SlidesIntro;
import com.simples.acesso.R;
import com.simples.acesso.Views.Slides_Intro;

import java.util.List;

public class SlidesIntroAdapter extends PagerAdapter {

    List<SlidesIntro> list_slide_intro;
    View view;
    Activity activity;

    public SlidesIntroAdapter(final Activity activity, final List<SlidesIntro> list_slide_intro){
        this.list_slide_intro = list_slide_intro;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list_slide_intro != null ? list_slide_intro.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        view = LayoutInflater.from(activity).inflate(R.layout.item_slide, container, false);
        SlidesIntro slide = list_slide_intro.get(position);

        ImageView imageview_slide_intro = view.findViewById(R.id.image_slide);
        TextView titulo_slide = view.findViewById(R.id.titulo_slide);
        TextView texto_slide = view.findViewById(R.id.texto_slide);

        imageview_slide_intro.setImageResource(slide.getImage());
        titulo_slide.setText(slide.getTitulo());
        texto_slide.setText(slide.getTexto());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }
}
