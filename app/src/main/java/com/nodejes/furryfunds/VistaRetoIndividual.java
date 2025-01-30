package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VistaRetoIndividual extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.retos_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaCrearReto), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText etTitulo = findViewById(R.id.plainTextTituloReto);
        EditText etDescripcion = findViewById(R.id.plaintextDescripcionReto);
        Button btnCrear = findViewById(R.id.buttonCrearReto);


        btnCrear.setOnClickListener(v -> {
            String titulo = etTitulo.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            if (titulo.isEmpty()) {
                etTitulo.setError("Falta el título");
                return;
            }
            if (descripcion.isEmpty()) {
                etDescripcion.setError("Falta la descripción");
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("RETO_TITULO", titulo);
            resultIntent.putExtra("RETO_DESCRIPCION", descripcion);
            setResult(RESULT_OK, resultIntent);
            finish(); // Vuelve a la vista anterior
        });
    }

    public void InicioViewRetoIndividual(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void PerfilViewRetoIndividual(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }
}
