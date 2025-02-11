package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VistaCrearGrupo extends ComponentActivity {

    private EditText edtGroupName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_crear_grupo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaCrearGrupo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtGroupName = findViewById(R.id.plainTextTituloGrupo);
        Button btnSaveGroup = findViewById(R.id.buttonCrearGrupo);

        // Configurar el botón de guardar grupo
        btnSaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGroup();
            }
        });
    }

    private void saveGroup() {
        // Obtener el nombre del grupo
        String groupName = edtGroupName.getText().toString().trim();

        if (!groupName.isEmpty()) {
            // Devolver el nombre del grupo a la actividad anterior
            Intent resultIntent = new Intent();
            resultIntent.putExtra("GROUP_NAME", groupName);
            setResult(RESULT_OK, resultIntent);
            finish(); // Cerrar la actividad
        } else {
            edtGroupName.setError("El nombre del grupo no puede estar vacío");
        }
    }
}
