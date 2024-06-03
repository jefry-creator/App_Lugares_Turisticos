package com.example.app_lugares_turisticos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    String correo;
    ImageButton btnMenu;
    Button asdns;
    private GoogleSignInClient mGoogleSignInClient;

    private RecyclerView recyclerView;
    private SitiosAdapter adapter;
    private List<Sitios> sitiosList;
    private DatabaseReference mDatabase;

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout una vez
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.searchViewhome);


        recyclerView = view.findViewById(R.id.touristattractions_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        sitiosList = new ArrayList<>();
        adapter = new SitiosAdapter(sitiosList, sitio -> {
                String key = sitio.getKey();
                String nombre = sitio.nombreSitio;
                String descripcion = sitio.descripcionSitio;
                double latitud = sitio.Latitud;
                double longitud = sitio.Longitud;
                String url = sitio.getURLimagen();

                Intent intent = new Intent(new Intent(getActivity(),MunicipioDetalles.class));
                intent.putExtra("id", key);
                intent.putExtra("nombre", nombre);
                intent.putExtra("descripcion", descripcion);
                intent.putExtra("latitud", latitud + "");
                intent.putExtra("longitud", longitud + "");
                intent.putExtra("url", url);

                getActivity().startActivity(intent);

        });
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
                // Manejo de errores
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        correo = user.getEmail();

        // Inicializa el botón de menú
        btnMenu = view.findViewById(R.id.imageView2);
        asdns = view.findViewById(R.id.exploretouristdestinations_button);

        // Configura el listener para el botón
        asdns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarMenu();
            }
        });

        // Configura el listener para el botón de menú
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupMenu(v);
            }
        });

        // Configura Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1055072710971-ucivc4m341fiaj15ktosuclhduouka85.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No necesitas hacer nada aquí, el filtrado es en tiempo real
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtra la lista según el texto de búsqueda
                filter(newText);
                return true;
            }
        });

        return view;
    }

    private void filter(String text) {
        List<Sitios> filteredList = new ArrayList<>();

        for (Sitios sitio : sitiosList) {
            // Aquí puedes personalizar cómo se realiza el filtrado
            if (sitio.getNombreSitio().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(sitio);
            } else if (sitio.getDescripcionSitio().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(sitio);
            }
        }

        // Actualiza la lista del adapter con los resultados filtrados
        adapter.filterList(filteredList);
    }

    private void MostrarMenu() {
        asdns.setText("Hola");
    }

    private void mostrarPopupMenu(View view) {
        PopupMenu menu = new PopupMenu(getActivity(), view);
        menu.getMenu().add(correo);
        menu.getMenu().add("Cerrar Sesion");
        menu.show();
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                if (title.equals("Cerrar Sesion")) {
                    // Lógica para cerrar sesión
                    signOut();
                } else {
                    // Lógica para el correo
                    Toast.makeText(getActivity(), "Correo: " + title, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void signOut() {
        // Cerrar sesión de Firebase
        FirebaseAuth.getInstance().signOut();

        // Cerrar sesión de Google
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), task -> {
            // Redirigir al usuario a la pantalla de inicio de sesión
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}
