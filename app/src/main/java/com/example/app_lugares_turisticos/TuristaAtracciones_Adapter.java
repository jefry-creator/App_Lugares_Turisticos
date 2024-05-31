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

public class TuristaAtracciones_Adapter extends RecyclerView.Adapter<TuristaAtracciones_Adapter.ViewHolder> {

    Context mycontext;
    ArrayList<TuristaAtracciones_Model> model;

    public TuristaAtracciones_Adapter(Context mycontext, ArrayList<TuristaAtracciones_Model> model) {
        this.mycontext = mycontext;
        this.model = model;
    }

    @NonNull
    @Override
    public TuristaAtracciones_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(mycontext).inflate(R.layout.home_recycleview_layout,null, true);
        return new TuristaAtracciones_Adapter.ViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull TuristaAtracciones_Adapter.ViewHolder holder, int position) {
        TuristaAtracciones_Model model2 = model.get(position);
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
