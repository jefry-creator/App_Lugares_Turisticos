package com.example.app_lugares_turisticos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TuristaLugares_Adapter extends RecyclerView.Adapter<TuristaLugares_Adapter.ViewHolder> {

    Context mycontext;
    ArrayList<TuristaLugares_Model> model;

    public TuristaLugares_Adapter(Context mycontext, ArrayList<TuristaLugares_Model> model) {
        this.mycontext = mycontext;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(mycontext).inflate(R.layout.home_recycleview_layout,null, true);
        return new ViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TuristaLugares_Model model2 = model.get(position);
        holder.image.setImageResource(model2.image);
        holder.nombre.setText(model2.nombre);
        holder.municipio.setText(model2.municipio);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView nombre, municipio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_departamento);
            nombre = itemView.findViewById(R.id.nombre_departamento);
            municipio = itemView.findViewById(R.id.nombre_municipio);



        }
    }
}
