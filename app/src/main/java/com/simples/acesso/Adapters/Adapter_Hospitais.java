package com.simples.acesso.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simples.acesso.Models.Hospitais_Model;
import com.simples.acesso.R;

import org.w3c.dom.Text;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hospitais, viewGroup, false);
        return new Adapter_Hospitais.Hospitas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Hospitais.Hospitas hospitas, int i) {
        Hospitais_Model hospitaisModel = list.get(i);

        hospitas.name_hospital.setText(hospitaisModel.getName().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class Hospitas extends RecyclerView.ViewHolder {

        TextView name_hospital;

        public Hospitas(@NonNull View itemView) {
            super(itemView);
            name_hospital = itemView.findViewById(R.id.name_hospital);
        }
    }
}
