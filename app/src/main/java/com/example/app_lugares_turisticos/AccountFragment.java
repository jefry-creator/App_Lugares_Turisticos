package com.example.app_lugares_turisticos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mPostRef;
    private TextView mLikeCountTextView;
    private Button mLikeButton;
    private String mPostId;
    private String mCurrentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.fragment_account, container, false);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();


        // Inicializar Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
        mPostId = "your_post_id-2"; // Reemplaza "your_post_id" con el id de la publicación actual

        // Referencia a la publicación actual en la base de datos
        mPostRef = mDatabase.getReference().child("posts").child(mPostId);

        // Referencias a los elementos de la interfaz de usuario
        mLikeCountTextView = view2.findViewById(R.id.contador2);
        mLikeButton = view2.findViewById(R.id.like_button);

        // Escuchar cambios en los likes de la publicación actual
        mPostRef.child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Contar el número de likes
                long likeCount = dataSnapshot.getChildrenCount();
                mLikeCountTextView.setText(String.valueOf(likeCount));

                // Verificar si el usuario actual dio like a la publicación
                if (dataSnapshot.child(mCurrentUserId).exists()) {
                    mLikeButton.setText("Unlike");
                } else {
                    mLikeButton.setText("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores
            }
        });

        // Configurar el OnClickListener para el botón de like
        mLikeButton.setOnClickListener(view -> {
            // Verificar si el usuario ya dio like a la publicación
            if (mLikeButton.getText().equals("Unlike")) {
                // Si ya dio like, entonces quitar el like
                unlikePost(mPostId, mCurrentUserId);
            } else {
                // Si no ha dado like, entonces dar like
                likePost(mPostId, mCurrentUserId);
            }
        });

        return view2;
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