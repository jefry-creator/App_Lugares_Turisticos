package com.example.app_lugares_turisticos.ListadeAtracciones;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_lugares_turisticos.MunicipioDetalles;
import com.example.app_lugares_turisticos.R;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class TourAtraccionesLista_Adapter extends RecyclerView.Adapter<TourAtraccionesLista_Adapter.ViewHolder>{

    Context context;
    ArrayList<TourAtraccionesLista_Model> arrayList;

    public TourAtraccionesLista_Adapter(Context context, ArrayList<TourAtraccionesLista_Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public TourAtraccionesLista_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View showview = LayoutInflater.from(context).inflate(R.layout.tour_atracciones_lista_layout, null, true);
        return new TourAtraccionesLista_Adapter.ViewHolder(showview);
    }

    @Override
    public void onBindViewHolder(@NonNull TourAtraccionesLista_Adapter.ViewHolder holder, int position) {

        TourAtraccionesLista_Model model = arrayList.get(position);

        holder.municipio.setText(model.municipio);
        holder.departamento.setText(model.departamento);
        holder.picture.setImageResource(model.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MunicipioDetalles.class);

                intent.putExtra("putextra_city", model.municipio);

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView municipio, departamento;
        ShapeableImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            municipio = itemView.findViewById(R.id.touratr_municipio);
            departamento = itemView.findViewById(R.id.touratr_departamento);
            picture = itemView.findViewById(R.id.touratr_image);

        }
    }
}
