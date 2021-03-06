package com.simples.acesso.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.simples.acesso.Models.Attendance_Model;
import com.simples.acesso.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Attendance extends RecyclerView.Adapter<Adapter_Attendance.Attendance> {

    Activity activity;
    List<Attendance_Model> list;
    public static List<String> lista_sintomas = new ArrayList<String>();

    public Adapter_Attendance(Activity activity, List<Attendance_Model> list) throws JSONException{
        this.activity = activity;
        this.list = list;
        lista_sintomas.clear();
    }

    @NonNull
    @Override
    public Attendance onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_attendande, viewGroup, false);
        return new Adapter_Attendance.Attendance(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Attendance attendance, final int i) {
        final Attendance_Model attendanceModel = list.get(i);

        if(attendanceModel.isChecked()){
            attendance.image_check.setVisibility(View.VISIBLE);
        }else{
            attendance.image_check.setVisibility(View.GONE);
        }

        attendance.description.setText(attendanceModel.getDescription().toUpperCase());

        attendance.item_list_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attendanceModel.isChecked()){
                    removeSitoma(attendanceModel);
                    attendance.image_check.setVisibility(View.GONE);
                    attendanceModel.setChecked(false);
                }else{
                    addSintoma(attendanceModel);
                    attendance.image_check.setVisibility(View.VISIBLE);
                    attendanceModel.setChecked(true);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class Attendance extends RecyclerView.ViewHolder {

        LinearLayout item_list_attendance;
        ImageView image_check;
        TextView description;

        public Attendance(@NonNull View itemView) {
            super(itemView);

            item_list_attendance = itemView.findViewById(R.id.item_list_attendance);
            image_check = itemView.findViewById(R.id.image_check);
            description = itemView.findViewById(R.id.description);
        }
    }

    private void removeSitoma(Attendance_Model attendance_model){
        if(lista_sintomas.contains(String.valueOf(attendance_model.getId()))){
            lista_sintomas.remove(String.valueOf(attendance_model.getId()));
        }
    }

    private void addSintoma(Attendance_Model attendance_model){
        if(!lista_sintomas.contains(String.valueOf(attendance_model.getId()))){
            lista_sintomas.add(String.valueOf(attendance_model.getId()));
        }
    }

}
