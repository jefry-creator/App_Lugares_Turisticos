package com.example.app_lugares_turisticos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_lugares_turisticos.databinding.ActivityMainBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ChipNavigationBar bottomNav;

    RecyclerView touristdestinations, touristattractions;
    ArrayList<TuristaLugares_Model> touristDestinations_models;
    ArrayList<TuristaAtracciones_Model> touristAttractions_models;
    TuristaLugares_Adapter touristDestinations_adapter;
    TuristaAtracciones_Adapter touristAttractions_adapter;
    LinearLayoutManager manager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        touristdestinations = findViewById(R.id.touristdestinations_recyclerview);

        touristDestinations_models = new ArrayList<>();
        touristDestinations_models.add(new TuristaLugares_Model(R.drawable.delhi, "New Delhi", "Delhi"));
        touristDestinations_models.add(new TuristaLugares_Model(R.drawable.jaipur, "Jaipur", "Rajasthan"));
        touristDestinations_models.add(new TuristaLugares_Model(R.drawable.darjeeling, "Darjeeling", "West Bengal"));
        touristDestinations_models.add(new TuristaLugares_Model(R.drawable.varanasi, "Varanasi", "Uttar Pradesh"));

        touristDestinations_adapter = new TuristaLugares_Adapter(this, touristDestinations_models);
        manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        touristattractions = findViewById(R.id.touristattractions_recyclerview);

        touristAttractions_models = new ArrayList<>();
        touristAttractions_models.add(new TuristaAtracciones_Model(R.drawable.agra, "Taj Mahal", "Agra, Uttar Pradesh"));
        touristAttractions_models.add(new TuristaAtracciones_Model(R.drawable.amritsar, "Golden Temple", "Amritsar, Punjab"));
        touristAttractions_models.add(new TuristaAtracciones_Model(R.drawable.udaipur, "Lake Pichola", "Udaipur, Rajasthan"));
        touristAttractions_models.add(new TuristaAtracciones_Model(R.drawable.mysore, "Amba Vilas Palace", "Mysuru, Karnataka"));

        touristAttractions_adapter = new TuristaAtracciones_Adapter(this, touristAttractions_models);
        manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        touristattractions.setAdapter(touristAttractions_adapter);
        touristattractions.setLayoutManager(manager);


        bottomNav = findViewById(R.id.bottomAppBar);

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.home) {
                    replaceFragment(new HomeFragment());
                } else if (i == R.id.home) {
                    replaceFragment(new DiscoverFragment());
                }else if (i == R.id.discover) {
                    replaceFragment(new DiscoverFragment());
                }
                else if (i == R.id.account) {
                    replaceFragment(new AccountFragment());
                }
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
