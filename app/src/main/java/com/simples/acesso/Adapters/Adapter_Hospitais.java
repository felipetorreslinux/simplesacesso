package com.simples.acesso.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simples.acesso.Models.Hospitais_Model;
import com.simples.acesso.R;

import java.util.List;

public class Adapter_Hospitais extends RecyclerView.Adapter<Adapter_Hospitais.Hospitas>{

    Activity activity;
    List<Hospitais_Model> list;

    public Adapter_Hospitais(Activity activity, List<Hospitais_Model> list){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Hospitais.Hospitas onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_places, viewGroup, false);
        return new Adapter_Hospitais.Hospitas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Hospitais.Hospitas hospitas, int i) {
        final Hospitais_Model hospitaisModel = list.get(i);

        hospitas.name.setText(hospitaisModel.getName().toUpperCase());
        hospitas.local.setText(hospitaisModel.getLocal());
        hospitas.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("google.navigation:q="+hospitaisModel.getLat()+","+hospitaisModel.getLng()+"");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class Hospitas extends RecyclerView.ViewHolder {

        TextView name;
        TextView local;
        ImageView image;

        public Hospitas(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            local = itemView.findViewById(R.id.local);
            image = itemView.findViewById(R.id.image);
        }
    }
}
