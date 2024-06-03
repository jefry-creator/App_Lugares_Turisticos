package com.example.app_lugares_turisticos;

import static android.app.PendingIntent.getActivity;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        holder.btnMenuItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupMenu(v, sitio.getKey());
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
        public ImageButton btnMenuItems;

        public SitiosViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombre_departamento);
            descripcionTextView = itemView.findViewById(R.id.nombre_municipio);
            btnMenuItems = itemView.findViewById(R.id.menu_item);
        }
    }

    private void mostrarPopupMenu(View view, String sitioId) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenu().add("Editar");
        popupMenu.getMenu().add("Eliminar");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                if (title.equals("Editar")) {
                    Toast.makeText(view.getContext(), "Editar seleccionado", Toast.LENGTH_SHORT).show();
                } else if (title.equals("Eliminar")) {
                    eliminar(view, sitioId);
                }
                return true;
            }
        });
    }

    private void eliminar(View view, String sitioId) {
        Database = FirebaseDatabase.getInstance().getReference();
        Database.child("sitios").child(sitioId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(view.getContext(), "Elemento eliminado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
