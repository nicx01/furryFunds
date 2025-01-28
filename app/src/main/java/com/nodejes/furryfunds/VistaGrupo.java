package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VistaGrupo extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vistagrupo_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaGrupo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView nombreGrupo = findViewById(R.id.textViewRetosVistaRetos);

        Intent intent = getIntent();
        String groupName = intent.getStringExtra("GROUP_NAME");
        int groupId = intent.getIntExtra("GROUP_ID", -1); // -1 es un valor por defecto

        // Mostrar el nombre del grupo en el TextView
        if (groupName != null) {
            nombreGrupo.setText(groupName);
        } else {
            nombreGrupo.setText("Grupo desconocido");
        }

        Button buttonAñadirFurro = findViewById(R.id.buttonAñadirFurroVistaGastos);
        buttonAñadirFurro.setOnClickListener(this::FurrosView);
    }

    public void EliminarGrupo(View v){
        Log.e("eeeeeeeeeeeeeeeeeeeee", "Error al guardar el ID de la imagen: ");
    }

    public void FurrosView(View v) {
        Intent intent = new Intent(this, VistaFurrosGrupo.class);
        startActivity(intent);
    }

    public void RetosView(View v) {
        Intent intent = new Intent(this, VistaRetos.class);
        startActivity(intent);
    }

    public void InicioViewGrupo(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void PerfilViewGrupo(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }
}
