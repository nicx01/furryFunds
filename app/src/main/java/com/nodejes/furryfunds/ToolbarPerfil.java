package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ToolbarPerfil extends ComponentActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.inicio_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaInicio), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    public void InicioView(View view){
        Intent intent = new Intent(this, ToolbarInicio.class);
        startActivity(intent);

    }
/*
    public void ModificarView(View view){
        Button botonGrupos = findViewById(R.id.GruposButton);
        if(botonGrupos.callOnClick()) {
            Intent intent = new Intent(this, BotonPerfilToolbar.class);
            startActivity(intent);
        }
    }
*/
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
