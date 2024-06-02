package com.example.app_lugares_turisticos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class HomeFragment extends Fragment {

    String correo;
    ImageButton btnMenu;
    Button asdns;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout una vez
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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

        return view;
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
