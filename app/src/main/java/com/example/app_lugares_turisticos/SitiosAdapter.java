package com.example.app_lugares_turisticos;

import static android.app.PendingIntent.getActivity;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SitiosAdapter extends RecyclerView.Adapter<SitiosAdapter.SitiosViewHolder> {
    private List<Sitios> sitiosList;
    private OnItemClickListener listener;

    private DatabaseReference Database;

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
        holder.itemView.setOnClickListener(v -> listener.onItemClick(sitio));

        Picasso.get().load(sitio.getURLimagen()).into(holder.imgSitio, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return sitiosList.size();
    }

    public static class SitiosViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;
        public TextView descripcionTextView;

        private ImageView imgSitio;

        public SitiosViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombre_departamento);
            descripcionTextView = itemView.findViewById(R.id.nombre_municipio);
            imgSitio = itemView.findViewById(R.id.image_departamento);
        }
    }

    public void filterList(List<Sitios> filteredList) {
        sitiosList = filteredList;
        notifyDataSetChanged();
    }
}
