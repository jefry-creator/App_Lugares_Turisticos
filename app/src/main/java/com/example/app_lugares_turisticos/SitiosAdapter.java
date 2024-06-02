package com.example.app_lugares_turisticos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SitiosAdapter extends RecyclerView.Adapter<SitiosAdapter.SitiosViewHolder> {
    private List<Sitios> sitiosList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Sitios sitio);
    }

    public SitiosAdapter(List<Sitios> sitiosList, OnItemClickListener listener) {
        this.sitiosList = sitiosList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SitiosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycleview_layout, parent, false);
        return new SitiosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SitiosViewHolder holder, int position) {
        Sitios sitio = sitiosList.get(position);
        holder.nombreTextView.setText(sitio.getNombreSitio());
        holder.descripcionTextView.setText(sitio.getDescripcionSitio());
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(sitio);
        });
    }

    @Override
    public int getItemCount() {
        return sitiosList.size();
    }

    public static class SitiosViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;
        public TextView descripcionTextView;

        public SitiosViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombre_departamento);
            descripcionTextView = itemView.findViewById(R.id.nombre_municipio);
        }
    }
}
