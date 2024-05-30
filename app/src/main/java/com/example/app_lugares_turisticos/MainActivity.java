package com.example.app_lugares_turisticos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.app_lugares_turisticos.databinding.ActivityMainBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            binding.bottomNavMenu.setItemSelected(R.id.home, true);
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavMenu.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch (id) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.discover:
                        fragment = new DiscoverFragment();
                        break;
                    case R.id.account:
                        fragment = new AccountFragment();
                        break;
                }
                if (fragment != null) {
                    replaceFragment(fragment);
                } else {
                    Log.e(TAG, "Error creating fragment");
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
