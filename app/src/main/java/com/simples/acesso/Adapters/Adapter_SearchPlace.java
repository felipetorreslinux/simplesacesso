package com.simples.acesso.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.simples.acesso.Models.SearchPlace_Model;
import com.simples.acesso.R;
import com.simples.acesso.Services.Service_Location;
import com.simples.acesso.Views.Principal;

import java.util.List;

public class Adapter_SearchPlace extends RecyclerView.Adapter<Adapter_SearchPlace.SearchPlace> {

    Activity activity;
    List<SearchPlace_Model> list;
    SharedPreferences.Editor editor;

    public Adapter_SearchPlace(Activity activity, List<SearchPlace_Model> list){
        this.activity = activity;
        this.list = list;
        this.editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();

    }

    @NonNull
    @Override
    public SearchPlace onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_search_place, viewGroup, false);
        return new Adapter_SearchPlace.SearchPlace(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPlace searchPlace, final int i) {
        final SearchPlace_Model searchPlaceModel = list.get(i);

        searchPlace.text_name_address.setText(searchPlaceModel.getAddress());
        searchPlace.text_name_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Principal.localNovo(
                        new Service_Location(activity).getAddress(new LatLng(searchPlaceModel.getLat(), searchPlaceModel.getLng())),
                        searchPlaceModel.getLat(),
                        searchPlaceModel.getLng());
                editor.putString("lat", String.valueOf(searchPlaceModel.getLat()));
                editor.putString("lng", String.valueOf(searchPlaceModel.getLng()));
                editor.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class SearchPlace extends RecyclerView.ViewHolder {

        ImageView image_search;
        TextView text_name_address;

        public SearchPlace(@NonNull View itemView) {
            super(itemView);

            image_search = itemView.findViewById(R.id.image_search);
            text_name_address = itemView.findViewById(R.id.text_name_address);

        }
    }
}
