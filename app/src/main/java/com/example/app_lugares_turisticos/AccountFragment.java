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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tour_atracciones_lista, container, false);

        recyclerView = view.findViewById(R.id.tourattr_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sitiosList = new ArrayList<>();
        adapter = new AdminAdapter(sitiosList, sitio -> {
            String key = sitio.getKey();
            String nombre = sitio.nombreSitio;
            String descripcion = sitio.descripcionSitio;
            Intent intent = new Intent(new Intent(getActivity(), MunicipioDetalles.class));
            intent.putExtra("id", key);
            intent.putExtra("nombre", nombre);
            intent.putExtra("descripcion", descripcion);
            startActivity(intent);
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
        return view;
    }

    public void eliminar(String sitioId) {
        if (getActivity() == null || getContext() == null) {
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación de eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este elemento?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Eliminar el elemento de la base de datos
                        DatabaseReference sitioRef = FirebaseDatabase.getInstance().getReference().child("sitios").child(sitioId);
                        sitioRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (getActivity() != null) {
                                    Toast.makeText(getActivity(), "Elemento eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (getActivity() != null) {
                                    Toast.makeText(getActivity(), "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cerrar el diálogo sin hacer nada
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}