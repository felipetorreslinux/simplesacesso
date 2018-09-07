package com.simples.acesso.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simples.acesso.Models.Places_Model;
import com.simples.acesso.R;

import java.util.List;

public class Adapter_Places extends RecyclerView.Adapter<Adapter_Places.Hospitas>{

    Activity activity;
    List<Places_Model> list;

    public Adapter_Places(Activity activity, List<Places_Model> list){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Places.Hospitas onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_places, viewGroup, false);
        return new Adapter_Places.Hospitas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Places.Hospitas hospitas, int i) {
        Places_Model hospitaisModel = list.get(i);

        hospitas.name_hospital.setText(hospitaisModel.getName().toUpperCase());
        hospitas.local_hospital.setText(hospitaisModel.getLocal());
        hospitas.distancia_hospital.setText("Distância de " + String.valueOf(hospitaisModel.getDistance()) + "km");
        if(hospitaisModel.isOpen()){
            hospitas.aberto_hospital.setText("Aberto");
            hospitas.aberto_hospital.setTextColor(activity.getResources().getColor(R.color.colorGreen));
        }else{
            hospitas.aberto_hospital.setText("Fechado");
            hospitas.aberto_hospital.setTextColor(activity.getResources().getColor(R.color.colorRed));
        }


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class Hospitas extends RecyclerView.ViewHolder {

        TextView name_hospital;
        TextView local_hospital;
        TextView distancia_hospital;
        TextView aberto_hospital;

        public Hospitas(@NonNull View itemView) {
            super(itemView);
            name_hospital = itemView.findViewById(R.id.name_hospital);
            local_hospital = itemView.findViewById(R.id.local_hospital);
            distancia_hospital = itemView.findViewById(R.id.distancia_hospital);
            aberto_hospital = itemView.findViewById(R.id.aberto_hospital);
        }
    }
}
