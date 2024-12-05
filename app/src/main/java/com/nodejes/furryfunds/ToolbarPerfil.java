package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ToolbarPerfil extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.toolbar_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbarinicio), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void perfilView(View view){
            Intent intent = new Intent(this, BotonPerfilToolbar.class);
            startActivity(intent);

    }

    public void gruposView(View view){
        Button botonGrupos = findViewById(R.id.GruposButton);
        if(botonGrupos.callOnClick()) {
            Intent intent = new Intent(this, BotonPerfilToolbar.class);
            startActivity(intent);
        }
    }
}
