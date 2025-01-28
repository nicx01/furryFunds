package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VistaFurrosGrupo extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vistafurrosgrupo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaFurrosGrupo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        añadirFurros();
    }

    public void InicioViewGrupo(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void añadirFurros() {
        // Crear un campo de texto para ingresar el correo

        EditText emailInput = new EditText(this);
        emailInput.setHint("Ingresa el correo"); // Texto guía para el usuario

        // Crear el diálogo
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Agregar correo")
                .setMessage("Introduce el correo para agregarlo al grupo:")
                .setView(emailInput) // Agregar el campo de texto
                .setPositiveButton("Agregar", (dialogInterface, i) -> {
                    // Obtener el texto ingresado por el usuario
                    String email = emailInput.getText().toString();

                    // Validar el correo
                    if (isValidEmail(email)) {
                        // Aquí puedes trabajar con el correo, por ejemplo, agregarlo a una lista o base de datos
                        Toast.makeText(this, "Correo agregado: " + email, Toast.LENGTH_SHORT).show();
                        // Implementa tu lógica aquí, como enviar el correo a un servidor o base de datos
                    } else {
                        Toast.makeText(this, "Correo inválido. Por favor, inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Cerrar el popup sin hacer nada
                    dialogInterface.dismiss();
                })
                .create();

        // Mostrar el diálogo
        dialog.show();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void PerfilViewGrupo(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }
}