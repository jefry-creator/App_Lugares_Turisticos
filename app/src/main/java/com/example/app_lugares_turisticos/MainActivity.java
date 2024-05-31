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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
