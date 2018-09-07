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
import com.simples.acesso.Models.Police_Model;
import com.simples.acesso.R;

import java.util.List;

public class Adapter_Polices extends RecyclerView.Adapter<Adapter_Polices.Police>{

    Activity activity;
    List<Police_Model> list;

    public Adapter_Polices(Activity activity, List<Police_Model> list){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Polices.Police onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_places, viewGroup, false);
        return new Adapter_Polices.Police(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_Polices.Police police, int i) {
        final Police_Model police_model = list.get(i);

        police.name.setText(police_model.getName().toUpperCase());
        police.local.setText(police_model.getLocal());
        police.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("google.navigation:q="+police_model.getLat()+","+police_model.getLng()+"");
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

    public class Police extends RecyclerView.ViewHolder {

        TextView name;
        TextView local;
        ImageView image;

        public Police(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            local = itemView.findViewById(R.id.local);
            image = itemView.findViewById(R.id.image);
        }
    }
}
