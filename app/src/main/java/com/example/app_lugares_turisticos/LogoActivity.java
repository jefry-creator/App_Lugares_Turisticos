package com.example.app_lugares_turisticos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogoActivity extends AppCompatActivity {

    private Button logindButton;
    private Button signupdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        logindButton = findViewById(R.id.logind_button);
        signupdButton = findViewById(R.id.signupd_button);

        logindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LogoActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        signupdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LogoActivity.this, SignUpActivity.class);
                startActivity(signupIntent);
            }
        });
    }
}