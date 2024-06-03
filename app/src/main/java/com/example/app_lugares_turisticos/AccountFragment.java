package com.example.app_lugares_turisticos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AccountFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdminAdapter adapter;
    ArrayList<Sitios> sitiosList;
    private DatabaseReference mDatabase;
    private FloatingActionButton deleteButton;
    private FloatingActionButton addButton;
    private Button selectButton;

    private boolean isSelectionActive = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tour_atracciones_lista, container, false);

        recyclerView = view.findViewById(R.id.tourattr_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        selectButton = view.findViewById(R.id.btnSelected);
        selectButton.setOnClickListener(v -> toggleSelection());

        sitiosList = new ArrayList<>();
        adapter = new AdminAdapter(sitiosList, sitio -> {
            Intent intent = new Intent(getActivity(), Registro_Activity.class);
            intent.putExtra("id", sitio.getKey());
            intent.putExtra("nombre", sitio.getNombreSitio());
            intent.putExtra("descripcion", sitio.getDescripcionSitio());
            intent.putExtra("latitud", String.valueOf(sitio.getLatitud()));
            intent.putExtra("longitud", String.valueOf(sitio.getLongitud()));
            intent.putExtra("url", sitio.getURLimagen());
            intent.putExtra("tarifa", sitio.getTarifaSitio());
            intent.putExtra("actividades", sitio.getActividadesSitio());
            intent.putExtra("direccion", sitio.getDireccionSitio());
            intent.putExtra("horaApertura", sitio.getHoraApertura());
            intent.putExtra("horaCierre", sitio.getHoraCierre());
            intent.putExtra("editar", true); // Indica que es una edición
            getActivity().startActivity(intent);
        }, this);


        recyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("sitios");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sitiosList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sitios sitio = snapshot.getValue(Sitios.class);
                    if (sitio != null) {
                        sitio.setKey(snapshot.getKey());  // Asigna la clave
                        sitiosList.add(sitio);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Error al cargar datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> deleteSelectedItems());

        addButton = view.findViewById(R.id.BtnRegistrar);
        addButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), Registro_Activity.class)));
        return view;
    }

    private void deleteSelectedItems() {

        if (getActivity() == null || getContext() == null) {
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación de eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este elemento?")
                .setPositiveButton("Sí", (dialog, which) -> {

                    if (getActivity() != null) {
                        List<Sitios> selectedItems = adapter.getSelectedItems();
                            for (Sitios sitio : selectedItems) {
                                mDatabase.child(sitio.getKey()).removeValue().addOnSuccessListener(aVoid -> {
                                    sitiosList.remove(sitio);
                                    adapter.notifyDataSetChanged();
                                }).addOnFailureListener(e -> {
                                    if (getActivity() != null) {
                                        Toast.makeText(getActivity(), "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            Toast.makeText(getActivity(), "Elementos eliminados exitosamente", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    public void eliminar(String sitioId) {
        if (getActivity() == null || getContext() == null) {
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación de eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este elemento?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Eliminar el elemento de la base de datos
                    DatabaseReference sitioRef = FirebaseDatabase.getInstance().getReference().child("sitios").child(sitioId);
                    sitioRef.removeValue().addOnSuccessListener(unused -> {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Elemento eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void toggleSelection() {
        isSelectionActive = !isSelectionActive;
        if(isSelectionActive){
            selectButton.setText("Cancelar");
        }else{
            selectButton.setText("Seleccionar");
            adapter.clearSelections();
        }
        updateUI();
    }

    private void updateUI() {
        // Actualizar visibilidad del botón de eliminar
        deleteButton.setVisibility(isSelectionActive ? View.VISIBLE : View.GONE);

        // Actualizar visibilidad de los checkbox en el adaptador
        adapter.setSelectionActive(isSelectionActive);
        adapter.notifyDataSetChanged();
    }
}
