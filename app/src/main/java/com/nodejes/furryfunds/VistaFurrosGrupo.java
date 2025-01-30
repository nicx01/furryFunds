package com.nodejes.furryfunds;

import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class VistaFurrosGrupo extends AppCompatActivity {
    private ArrayList<String> emailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vistafurrosgrupo);

        // Inicializar la lista de correos
        emailList = new ArrayList<>();

        // Ajustar insets para vista
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaFurrosGrupo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cargarFurros();
    }
    public void cargarFurros() {
        // Obtener el ScrollView del diseño
        ScrollView scrollView = findViewById(R.id.scrollViewFurrosGrupo);

        // Verificar que el ScrollView no sea nulo
        if (scrollView != null) {
            // Crear un contenedor LinearLayout dentro del ScrollView
            LinearLayout container = new LinearLayout(this);
            container.setOrientation(LinearLayout.VERTICAL);

            // Limpiar cualquier vista previa en el LinearLayout si ya existe
            container.removeAllViews();

            // Iterar sobre la lista de correos (emailList) y agregar botones dinámicamente
            for (String item : emailList) {
                Button button = new Button(this);
                button.setText(item);
                button.setTextSize(16);
                button.setPadding(8, 8, 8, 8);

                // Establecer un listener para eliminar el correo cuando se presione el botón
                button.setOnClickListener(v -> mostrarConfirmacionEliminacion(item));

                // Agregar el botón al contenedor
                container.addView(button);
            }

            // Agregar el LinearLayout como único hijo del ScrollView
            scrollView.removeAllViews();  // Eliminar cualquier vista anterior del ScrollView
            scrollView.addView(container); // Agregar el LinearLayout al ScrollView
        }
    }



    public void addFurros(View v) {
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
                    if (isValidEmail(email) || email.equals("prueba")) {
                        emailList.add(email);
                        mostrarPopUp("Correo agregado: " + email, false); // Mensaje de éxito
                        cargarFurros();
                    } else {
                        mostrarPopUp("Correo inválido. Por favor, inténtalo de nuevo.", true); // Mensaje de error
                    }
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        dialog.show();

    }

    public void borrarFurros(View v) {
        EditText emailInput = new EditText(this);
        emailInput.setHint("Ingresa el correo a eliminar");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Eliminar correo")
                .setMessage("Introduce el correo que deseas eliminar del grupo:")
                .setView(emailInput)
                .setPositiveButton("Buscar", (dialogInterface, i) -> {
                    String email = emailInput.getText().toString();

                    // Verificar si el correo existe en la lista
                    if (emailList.contains(email)) {
                        mostrarConfirmacionEliminacion(email.trim());

                    } else {
                        mostrarPopUp("El correo no está en la lista.", true); // Mensaje de error
                    }
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        // Mostrar el diálogo
        dialog.show();

    }

    private void mostrarConfirmacionEliminacion(String email) {
        new AlertDialog.Builder(VistaFurrosGrupo.this)

                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar el correo: " + email + "?")
                .setPositiveButton("Sí, eliminar", (dialogInterface, i) -> {
                    // Eliminar espacios en blanco y buscar el correo en la lista
                    boolean removed = emailList.remove(email.trim());

                    if (removed) {
                        mostrarPopUp("Correo eliminado: " + email, false);
                        cargarFurros(); // Actualizar la vista después de eliminar
                    } else {
                        mostrarPopUp("Error: El correo no se encontró en la lista.", true);
                    }
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss())
                .create()
                .show();
    }


    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void mostrarPopUp(String mensaje, boolean isError) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Configurar el título y el color en función del tipo de mensaje
        builder.setTitle(isError ? "Error" : "Éxito");
        builder.setMessage(mensaje);

        // Cambiar el color del título a rojo si es un error
        AlertDialog dialog = builder
                .setPositiveButton("Aceptar", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        // Aplicar el estilo personalizado
        dialog.setOnShowListener(dialogInterface -> {
            if (isError) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            }
        });

        dialog.show();
    }


    public void PerfilViewGrupo(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }
    public void InicioViewGrupo(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }
}