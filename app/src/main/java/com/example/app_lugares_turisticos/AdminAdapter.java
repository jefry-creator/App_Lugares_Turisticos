package com.example.app_lugares_turisticos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.SitiosViewHolder> {
    private List<Sitios> sitiosList;
    private SitiosAdapter.OnItemClickListener listener;
    private AccountFragment accountFragment;

    public AdminAdapter(List<Sitios> sitiosList, SitiosAdapter.OnItemClickListener listener, AccountFragment accountFragment) {
        this.sitiosList = sitiosList;
        this.listener = listener;
        this.accountFragment = accountFragment;
    }

    @NonNull
    @Override
    public SitiosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_atracciones_lista_layout, parent, false);
        return new SitiosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SitiosViewHolder holder, int position) {
        Sitios sitio = sitiosList.get(position);
        holder.nombreTextView.setText(sitio.getNombreSitio());
        holder.descripcionTextView.setText(sitio.getDescripcionSitio());
        holder.btnBorrar.setOnClickListener(v -> accountFragment.eliminar(sitio.getKey()));
    }

    @Override
    public int getItemCount() {
        return sitiosList.size();
    }

    public static class SitiosViewHolder extends RecyclerView.ViewHolder {
        ImageView image, btnBorrar, btnEditar;
        public TextView nombreTextView;
        public TextView descripcionTextView;

        public SitiosViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.touratr_image);
            nombreTextView = itemView.findViewById(R.id.touratr_municipio);
            descripcionTextView = itemView.findViewById(R.id.touratr_departamento);
            btnBorrar = itemView.findViewById(R.id.BtnBorrar);
            btnEditar = itemView.findViewById(R.id.BtnEditar);
        }
    }
}