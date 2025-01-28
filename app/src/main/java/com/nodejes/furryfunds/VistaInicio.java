package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VistaInicio extends ComponentActivity {

    private int groupIdCounter = 1; // Contador para identificar cada grupo
    private ArrayList<String> groupNames; // Lista para almacenar nombres de grupos
    private LinearLayout groupContainer; // Contenedor de botones de grupos
    private FloatingActionButton btnCreateGroup; // Botón para crear grupos

    private final ActivityResultLauncher<Intent> createGroupLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String groupName = result.getData().getStringExtra("GROUP_NAME");
                    if (groupName != null && !groupName.isEmpty()) {
                        if (!groupNames.contains(groupName)) { // Validar nombres duplicados
                            addGroupButton(groupName, groupIdCounter++);
                            groupNames.add(groupName);
                            saveGroupToFirebase(groupName); // Guardar el grupo en Firebase
                            Toast.makeText(this, "Grupo creado: " + groupName, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "El grupo ya existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private void saveGroupToFirebase(String groupName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("usuarios/" + user.getUid() + "/grupos");

            String groupId = myRef.push().getKey();
            if (groupId != null) {
                myRef.child(groupId).setValue(groupName)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("Firebase", "Grupo guardado correctamente: " + groupName);
                            } else {
                                Log.e("Firebase", "Error al guardar el grupo: " + task.getException().getMessage());
                            }
                        });
            } else {
                Log.e("Firebase", "Error al generar el ID del grupo.");
            }
        } else {
            Log.e("Firebase", "No hay usuario autenticado.");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.inicio_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaInicio), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        groupContainer = findViewById(R.id.LinearLayoutScrollViewPrincipal);
        btnCreateGroup = findViewById(R.id.addGrupoButton);

        groupNames = new ArrayList<>();

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearGrupoNuevo(v);
            }
        });

        cargarGrupos();
    }

    private void cargarGrupos() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("usuarios/" + user.getUid() + "/grupos");

            myRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String groupName = snapshot.getValue(String.class);
                        if (groupName != null) {
                            groupNames.add(groupName);
                            addGroupButton(groupName, groupIdCounter++);
                        }
                    }
                } else {
                    Log.e("Firebase", "Error al cargar los grupos: " + task.getException().getMessage());
                }
            });
        } else {
            Log.e("Firebase", "No hay usuario autenticado.");
        }
    }


    public void CrearGrupoNuevo(View view){
        Intent intent = new Intent(this, VistaCrearGrupo.class);
        createGroupLauncher.launch(intent);
    }

    private void addGroupButton(String groupName, int groupId) {
        // Crear un nuevo botón
        Button button = new Button(this);
        button.setText(groupName);
        button.setAllCaps(false); // Desactivar texto en mayúsculas (opcional)
        button.setTag(groupId); // Guardar el ID del grupo como tag

        // Configurar el evento de clic
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGroupDetails(groupId);
            }
        });

        // Añadir el botón al contenedor de grupos
        groupContainer.addView(button);
    }

    private void openGroupDetails(int groupId) {
        // Abre la actividad con los detalles del grupo correspondiente
        String groupName = groupNames.get(groupId - 1);

        Intent intent = new Intent(VistaInicio.this, VistaGrupo.class);
        intent.putExtra("GROUP_ID", groupId); // Pasar el ID del grupo
        intent.putExtra("GROUP_NAME", groupName); // Pasar el nombre del grupo
        startActivity(intent);
    }

    public void PerfilButton(View view){
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }
}