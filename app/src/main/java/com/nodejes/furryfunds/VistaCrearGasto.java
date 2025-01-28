package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

public class VistaCrearGasto extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_gastos);

        EditText etGastoNombre = findViewById(R.id.plaintexttitulo);
        EditText etCreadorNombre = findViewById(R.id.plaintextfurronickgasto);
        EditText etCantidad = findViewById(R.id.plaintextcantidadgasto);
        Button btnCrearGasto = findViewById(R.id.buttonCrearGasto);

        btnCrearGasto.setOnClickListener(v -> {
            String gastoNombre = etGastoNombre.getText().toString().trim();
            String creadorNombre = etCreadorNombre.getText().toString().trim();
            String cantidad = etCantidad.getText().toString().trim();

            if (gastoNombre.isEmpty() || creadorNombre.isEmpty() || cantidad.isEmpty()) {
                Toast.makeText(VistaCrearGasto.this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("GASTO_NOMBRE", gastoNombre);
            resultIntent.putExtra("CREADOR_NOMBRE", creadorNombre);
            resultIntent.putExtra("CANTIDAD", cantidad);
            setResult(RESULT_OK, resultIntent);
            finish(); // Vuelve a la vista anterior
        });
    }
}
