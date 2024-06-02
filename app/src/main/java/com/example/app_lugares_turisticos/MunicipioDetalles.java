package com.example.app_lugares_turisticos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_lugares_turisticos.R;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MunicipioDetalles extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mPostRef;
    private TextView mLikeCountTextView, titulo, txtdesc;
    private ImageButton mLikeButton;
    private String mPostId;
    private String mCurrentUserId;

    boolean isLiked = false; // variable para rastrear el estado del like

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipio_detalles);

        titulo = findViewById(R.id.citydetails_name);
        txtdesc = findViewById(R.id.citydetails_description);

        Intent intent = getIntent();
        String key = intent.getStringExtra("id");
        String nombre = intent.getStringExtra("nombre");
        String desc = intent.getStringExtra("descripcion");

        titulo.setText(nombre);
        txtdesc.setText(desc);

        Toast.makeText(MunicipioDetalles.this, "Key: " + key, Toast.LENGTH_SHORT).show();
// Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();


        // Inicializar Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
        mPostId = key; // Reemplaza "your_post_id" con el id de la publicación actual

        // Referencia a la publicación actual en la base de datos
        mPostRef = mDatabase.getReference().child("sitios").child(mPostId);

        // Referencias a los elementos de la interfaz de usuario
        mLikeCountTextView = findViewById(R.id.txtLikesCounts);
        mLikeButton = findViewById(R.id.btnLikes);

        // Escuchar cambios en los likes de la publicación actual
        // Asignar el color al botón basado en el estado del like desde Firebase
        mPostRef.child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Contar el número de likes
                long likeCount = dataSnapshot.getChildrenCount();
                mLikeCountTextView.setText(String.valueOf(likeCount));

                // Verificar si el usuario actual dio like a la publicación
                if (dataSnapshot.child(mCurrentUserId).exists()) {
                    mLikeButton.setColorFilter(Color.parseColor("#EA3030"), PorterDuff.Mode.SRC_IN); // "Liked" color
                    isLiked = true; // actualizar el estado del like
                } else {
                    mLikeButton.setColorFilter(Color.parseColor("#8A8A8A"), PorterDuff.Mode.SRC_IN); // "Not liked" color
                    isLiked = false; // actualizar el estado del like
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores
            }
        });

// Definir el estado inicial del botón basado en el estado del like
        if (isLiked) {
            mLikeButton.setColorFilter(Color.parseColor("#EA3030"), PorterDuff.Mode.SRC_IN); // color cuando está "liked"
        } else {
            mLikeButton.setColorFilter(Color.parseColor("#8A8A8A"), PorterDuff.Mode.SRC_IN); // color cuando no está "liked"
        }

// Configurar el listener para el botón de like
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    // Si ya dio like, entonces quitar el like
                    unlikePost(mPostId, mCurrentUserId);
                    mLikeButton.setColorFilter(Color.parseColor("#8A8A8A"), PorterDuff.Mode.SRC_IN); // cambiar el color a no "liked"
                } else {
                    // Si no ha dado like, entonces dar like
                    likePost(mPostId, mCurrentUserId);
                    mLikeButton.setColorFilter(Color.parseColor("#EA3030"), PorterDuff.Mode.SRC_IN); // cambiar el color a "liked"
                }
                isLiked = !isLiked; // actualizar el estado del like
            }
        });
    }

        // Método para dar like a una publicación
    private void likePost(String postId, String userId) {
        mPostRef.child("likes").child(userId).setValue(true);
    }

    // Método para quitar el like de una publicación
    private void unlikePost(String postId, String userId) {
        mPostRef.child("likes").child(userId).removeValue();
    }
}