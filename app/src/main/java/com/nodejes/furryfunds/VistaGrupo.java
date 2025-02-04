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

import java.util.HashMap;
import java.util.Map;

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
        cargarGastos();
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
        Intent intent = getIntent();
        String groupId=intent.getStringExtra("GROUP_ID");
        if (groupId == null || groupId.isEmpty()) {
            Log.e("Firebase", "No se puede guardar el gasto sin un ID de grupo.");
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail(); // Obtener el email del usuario
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference gastosRef = database.getReference("usuarios/" + user.getUid() + "/grupos/" + groupId + "/gastos");

            String gastoId = gastosRef.push().getKey(); // Generar un ID único para el gasto
            if (gastoId != null) {
                Map<String, Object> gastoData = new HashMap<>();
                gastoData.put("nombre", nombreGasto);
                gastoData.put("cantidad", cantidadGasto);
                gastoData.put("usuario", userEmail);

                gastosRef.child(gastoId).setValue(gastoData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("Firebase", "Gasto guardado correctamente por " + userEmail);
                            } else {
                                Log.e("Firebase", "Error al guardar gasto: " + task.getException().getMessage());
                            }
                        });

                Button gastoButton = new Button(this);
                gastoButton.setText(nombreGasto + " - " + cantidadGasto + "€");

                gastoButton.setOnClickListener(v -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Eliminar gasto")
                            .setMessage("¿Estás seguro de que quieres eliminar este gasto?")
                            .setPositiveButton("Sí", (dialog, which) -> {
                                // Eliminar de Firebase
                                gastosRef.child(gastoId).removeValue()
                                        .addOnCompleteListener(deleteTask -> {
                                            if (deleteTask.isSuccessful()) {
                                                Log.d("Firebase", "Gasto eliminado correctamente");
                                            } else {
                                                Log.e("Firebase", "Error al eliminar gasto: " + deleteTask.getException().getMessage());
                                            }
                                        });

                                gastoContainer.removeView(gastoButton);
                                Toast.makeText(VistaGrupo.this, "Gasto eliminado", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .show();
                });

                gastoContainer.addView(gastoButton);
            }
        } else {
            Log.e("Firebase", "No hay usuario autenticado.");
        }
    }


    public void EliminarGrupo(View v) {
        Intent intent = getIntent();
        String groupName = intent.getStringExtra("GROUP_NAME");
        String groupId = intent.getStringExtra("GROUP_ID");

        if (groupId != null && groupName != null) {
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
    private void cargarGastos() {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("GROUP_ID"); // Obtener el ID del grupo

        if (groupId != null && !groupId.isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference gastosRef = database.getReference("usuarios/" + user.getUid() + "/grupos/" + groupId + "/gastos");

                gastosRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            String gastoNombre = snapshot.child("nombre").getValue(String.class);
                            String cantidadGasto = snapshot.child("cantidad").getValue(String.class);
                            String usuario = snapshot.child("usuario").getValue(String.class);

                            if (gastoNombre != null && cantidadGasto != null) {
                                Button gastoButton = new Button(this);
                                gastoButton.setText(gastoNombre + " - " + cantidadGasto + "€");

                                gastoButton.setOnClickListener(v -> {
                                    new AlertDialog.Builder(this)
                                            .setTitle("Eliminar gasto")
                                            .setMessage("¿Estás seguro de que quieres eliminar este gasto?")
                                            .setPositiveButton("Sí", (dialog, which) -> {
                                                // Eliminar de Firebase y de la vista
                                                snapshot.getRef().removeValue()
                                                        .addOnCompleteListener(deleteTask -> {
                                                            if (deleteTask.isSuccessful()) {
                                                                Log.d("Firebase", "Gasto eliminado correctamente");
                                                            } else {
                                                                Log.e("Firebase", "Error al eliminar gasto: " + deleteTask.getException().getMessage());
                                                            }
                                                        });

                                                gastoContainer.removeView(gastoButton); // Eliminar el botón de la vista
                                                Toast.makeText(VistaGrupo.this, "Gasto eliminado", Toast.LENGTH_SHORT).show();
                                            })
                                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                                            .show();
                                });

                                gastoContainer.addView(gastoButton);
                            }
                        }
                    } else {
                        Log.e("Firebase", "No se encontraron gastos o error al cargar.");
                    }
                });
            } else {
                Log.e("Firebase", "No hay usuario autenticado.");
            }
        } else {
            Log.e("Firebase", "ID de grupo no válido.");
        }
    }
    }

