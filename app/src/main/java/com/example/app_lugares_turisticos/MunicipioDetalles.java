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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MunicipioDetalles extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private  ShapeableImageView imgSitio;

    private Button btnObtenerUbicacion;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mPostRef;
    private TextView mLikeCountTextView, titulo, txtdesc, txttarifa, txthorarios, txtactividades;
    private ImageButton mLikeButton, btnRegre;
    private String mPostId;
    private String mCurrentUserId, latitud, longitud;

    boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipio_detalles);

        titulo = findViewById(R.id.citydetails_name);
        txtdesc = findViewById(R.id.descripciontxt);
        imgSitio = findViewById(R.id.citydetails_image);
        txttarifa = findViewById(R.id.tarifatxt);
        txthorarios = findViewById(R.id.horariostxt);
        txtactividades = findViewById(R.id.actividadestxt);
        btnRegre = findViewById(R.id.btnRegresar);

        btnRegre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MunicipioDetalles.this, MainActivity.class);
                startActivity(loginIntent);
            }
        });

        Intent intent = getIntent();
        String key = intent.getStringExtra("id");
        String nombre = intent.getStringExtra("nombre");
        String desc = intent.getStringExtra("descripcion");
        String url = intent.getStringExtra("url");
        latitud = intent.getStringExtra("latitud");
        longitud = intent.getStringExtra("longitud");
        String tarifa = intent.getStringExtra("tarifa");
        String HoraA = intent.getStringExtra("horaApertura");
        String HoraC = intent.getStringExtra("horaCierre");
        String act = intent.getStringExtra("actividades");

        titulo.setText(nombre);
        txtdesc.setText(desc);
        txttarifa.setText("$"+tarifa);
        txthorarios.setText(HoraA+" a "+HoraC);
        txtactividades.setText(act);

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
                    mLikeButton.setImageResource(R.drawable.baseline_favorite_24);
                    isLiked = true; // actualizar el estado del like
                } else {
                    mLikeButton.setColorFilter(Color.parseColor("#8A8A8A"), PorterDuff.Mode.SRC_IN); // "Not liked" color
                    mLikeButton.setImageResource(R.drawable.baseline_favorite_border_24);
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
            mLikeButton.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            mLikeButton.setColorFilter(Color.parseColor("#8A8A8A"), PorterDuff.Mode.SRC_IN); // color cuando no está "liked"
            mLikeButton.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        // Configurar el listener para el botón de like
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    // Si ya dio like, entonces quitar el like
                    unlikePost(mPostId, mCurrentUserId);
                    mLikeButton.setColorFilter(Color.parseColor("#8A8A8A"), PorterDuff.Mode.SRC_IN); // cambiar el color a no "liked"
                    mLikeButton.setImageResource(R.drawable.baseline_favorite_border_24);

                } else {
                    // Si no ha dado like, entonces dar like
                    likePost(mPostId, mCurrentUserId);
                    mLikeButton.setColorFilter(Color.parseColor("#EA3030"), PorterDuff.Mode.SRC_IN); // cambiar el color a "liked"
                    mLikeButton.setImageResource(R.drawable.baseline_favorite_24);
                }
                isLiked = !isLiked; // actualizar el estado del like
            }
        });

        //METODO DE OBTENER UBICACIÓN
        btnObtenerUbicacion = findViewById(R.id.citydetails_getlocationbtn);

        btnObtenerUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MunicipioDetalles.this,UbicacionActivity.class);
                intent.putExtra("latitud", latitud);
                intent.putExtra("longitud", longitud);
                startActivity(intent);
            }
        });


        Picasso.get().load(url).fit().into(imgSitio, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

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