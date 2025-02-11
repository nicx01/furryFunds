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
        EditText etCantidad = findViewById(R.id.plaintextcantidadgasto);
        Button btnCrearGasto = findViewById(R.id.buttonCrearGasto);

        btnCrearGasto.setOnClickListener(v -> {
            String gastoNombre = etGastoNombre.getText().toString().trim();
            String cantidadString = etCantidad.getText().toString().trim();

            if (gastoNombre.isEmpty()) {
                etGastoNombre.setError("Falta el titulo");
            }
            if (cantidadString.isEmpty()) {
                etCantidad.setError("Falta el gasto");
            }

            double cantidad;
            try {
                cantidad = Double.parseDouble(cantidadString);
                if (cantidad <= 0) {
                    etCantidad.setError("La cantidad debe ser mayor a 0");
                    return;
                }
            } catch (NumberFormatException e) {
                etCantidad.setError("Ingrese un número válido");
                return;
            }


            Intent resultIntent = new Intent();
            resultIntent.putExtra("GASTO_NOMBRE", gastoNombre);
            resultIntent.putExtra("CANTIDAD", cantidad);
            setResult(RESULT_OK, resultIntent);
            finish(); // Vuelve a la vista anterior
        });
    }
    public void InicioViewCrearGrupo(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void PerfilViewCrearGrupo(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }
}
