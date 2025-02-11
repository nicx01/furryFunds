package com.nodejes.furryfunds;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VistaRetos extends AppCompatActivity{

    private LinearLayout retosContainer; // Contenedor de retos

    private final ActivityResultLauncher<Intent> addRetoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String retoTitulo = result.getData().getStringExtra("RETO_TITULO");
                    String retoDescripcion = result.getData().getStringExtra("RETO_DESCRIPCION");

                    if (retoTitulo != null && retoDescripcion != null) {
                        addRetoButton(retoTitulo, retoDescripcion);
                    }
                }
            });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vistaretos_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaRetos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        retosContainer = findViewById(R.id.vistaVistaRetos);
        Button btnCrearReto = findViewById(R.id.buttonAñadirRetoVistaRetos);

        btnCrearReto.setOnClickListener(v -> {
            Intent intent = new Intent(VistaRetos.this, VistaRetoIndividual.class);
            addRetoLauncher.launch(intent);
        });
    }

    private void addRetoButton(String titulo, String descripcion) {
        LinearLayout retoLayout = new LinearLayout(this);
        retoLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button retoButton = new Button(this);
        retoButton.setText(titulo);
        retoButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        CheckBox checkBox = new CheckBox(this);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        checkBox.setButtonTintList(ColorStateList.valueOf(Color.BLACK)); // Color del check

        retoButton.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Eliminar reto")
                    .setMessage("¿Estás seguro de que quieres eliminar este reto?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        retosContainer.removeView(retoLayout);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        retoLayout.addView(retoButton);
        retoLayout.addView(checkBox);
        retosContainer.addView(retoLayout);
    }

    public void CrearReto(View v) {
        Intent intent = new Intent(this, VistaRetoIndividual.class);
        startActivity(intent);
    }

    public void InicioViewRetos(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void PerfilViewRetos(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }
}