package com.example.cafeteria.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cafeteria.R;
import com.example.cafeteria.utils.Constantes;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView txtPrincipal = findViewById(R.id.txt_principal);
        txtPrincipal.setText("¡Hola! " + Constantes.usuario.getNombre() + "... ¿Que deseas hacer hoy?");
    }
}