package com.example.app_lugares_turisticos.ListadeAtracciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.app_lugares_turisticos.R;

import java.util.ArrayList;

public class TourAtraccionesLista extends AppCompatActivity {

    RecyclerView recycler;
    LinearLayoutManager manager;
    TourAtraccionesLista_Adapter adapter;
    ArrayList<TourAtraccionesLista_Model> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_atracciones_lista);

        array = new ArrayList<>();
        array.add(new TourAtraccionesLista_Model("Agra", "Uttar Pradesh", R.drawable.agra));
        array.add(new TourAtraccionesLista_Model("Amritsar", "Punjab", R.drawable.amritsar));
        array.add(new TourAtraccionesLista_Model("Darjeeling", "West Bengal", R.drawable.darjeeling));
        array.add(new TourAtraccionesLista_Model("Jaipur", "Rajasthan", R.drawable.jaipur));
        array.add(new TourAtraccionesLista_Model("Kolkata", "West Bengal", R.drawable.kolkata));
        array.add(new TourAtraccionesLista_Model("Mumbai", "Maharashtra", R.drawable.mumbai));
        array.add(new TourAtraccionesLista_Model("Mysore", "Karnataka", R.drawable.mysore));
        array.add(new TourAtraccionesLista_Model("New Delhi", "Delhi", R.drawable.delhi));
        array.add(new TourAtraccionesLista_Model("Udaipur", "Rajasthan", R.drawable.udaipur));
        array.add(new TourAtraccionesLista_Model("Varanasi", "Uttar Pradesh", R.drawable.varanasi));

        adapter = new TourAtraccionesLista_Adapter(this, array);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recycler = findViewById(R.id.tourattr_recycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(manager);

    }
}