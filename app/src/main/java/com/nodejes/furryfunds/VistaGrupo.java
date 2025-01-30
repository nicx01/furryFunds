package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VistaGrupo extends AppCompatActivity {

    private LinearLayout gastoContainer; // Contenedor para los gastos
    private final ActivityResultLauncher<Intent> addGastoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String gastoNombre = result.getData().getStringExtra("GASTO_NOMBRE");
                    double cantidad = result.getData().getDoubleExtra("CANTIDAD", 0.0);

                    if (gastoNombre != null && cantidad>0) {
                        String cantidadStr = String.valueOf(cantidad);
                        addGastoButton(gastoNombre, cantidadStr);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vistagrupo_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaGrupo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gastoContainer = findViewById(R.id.vistaVistaRetos);
        FloatingActionButton btnAñadirGasto = findViewById(R.id.buttonAñadirGastoVistaGrupo);

        btnAñadirGasto.setOnClickListener(v -> {
            Intent intent = new Intent(VistaGrupo.this, VistaCrearGasto.class);
            addGastoLauncher.launch(intent);
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




    private void addGastoButton(String nombreGasto, String cantidadGasto) {
        //String nombreCreador="hugo";
        Button gastoButton = new Button(this);
        gastoButton.setText(nombreGasto + " - " + cantidadGasto + "€");

        gastoButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar gasto")
                    .setMessage("¿Estás seguro de que quieres eliminar este gasto?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        gastoContainer.removeView(gastoButton); // Eliminar el botón
                        Toast.makeText(VistaGrupo.this, "Gasto eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Cerrar el diálogo sin eliminar
                    .show();
        });

        gastoContainer.addView(gastoButton); // Añadir el botón al contenedor
        Toast.makeText(this, "Gasto añadido: " + nombreGasto + " - " + cantidadGasto + "€", Toast.LENGTH_SHORT).show();

    }


    public void EliminarGrupo(View v) {
        Intent intent = getIntent();
        String groupName = intent.getStringExtra("GROUP_NAME");
        int groupId = intent.getIntExtra("GROUP_ID", -1);

        if (groupId != -1 && groupName != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar el grupo \"" + groupName + "\"? Esta acción no se puede deshacer.")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
                            DatabaseReference groupRef = database.getReference("usuarios/" + user.getUid() + "/grupos");

                            groupRef.orderByValue().equalTo(groupName).get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                        snapshot.getRef().removeValue();
                                    }
                                    Log.d("Firebase", "Grupo eliminado correctamente: " + groupName);

                                    Intent returnIntent = new Intent(this, VistaInicio.class);
                                    returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(returnIntent);
                                    finish();
                                } else {
                                    Log.e("Firebase", "Error al eliminar el grupo: " + task.getException().getMessage());
                                }
                            });
                        } else {
                            Log.e("Firebase", "No hay usuario autenticado.");
                        }
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            Log.e("EliminarGrupo", "Datos del grupo inválidos.");
        }
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
