package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PerfilVista extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.perfil_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaPerfil), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void compartirView(View v) {
        Intent intent = new Intent(this, CompartirAnimal.class);
        startActivity(intent);
    }

    public void CambiarIconoView(View v) {
        Intent intent = new Intent(this, CambiarIconoVista.class);
        startActivity(intent);
    }

    public void InicioView(View v) {
        Intent intent = new Intent(this, ToolbarInicio.class);
        startActivity(intent);
    }

    /*public void ModificarPerfil(View v){
        Intent intent = new Intent(this, )
    }*/
}
