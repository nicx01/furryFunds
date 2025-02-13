package com.nodejes.furryfunds;

import android.content.Intent;
import android.graphics.Color;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
                            String groupId = UUID.randomUUID().toString();
                            addGroupButton(groupName, groupId);
                            groupNames.add(groupName);
                            saveGroupToFirebase(groupId, groupName);
                            Toast.makeText(this, "Grupo creado: " + groupName, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "El grupo ya existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private void saveGroupToFirebase(String groupId, String groupName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("grupos");

            Map<String, Object> groupData = new HashMap<>();
            groupData.put("nombre", groupName);
            groupData.put("owner", user.getEmail());

            myRef.child(groupId).setValue(groupData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "Grupo guardado correctamente: " + groupName);
                        } else {
                            Log.e("Firebase", "Error al guardar el grupo: " + task.getException().getMessage());
                        }
                    });
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
        stopService(new Intent(this, MusicService.class));
        startService(new Intent(this, MusicFondoService.class)); // Inicia la música

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
            DatabaseReference gruposRef = database.getReference("grupos");

            gruposRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    for (DataSnapshot groupSnapshot : task.getResult().getChildren()) {
                        String groupId = groupSnapshot.getKey(); // Obtener el ID del grupo
                        String groupName = groupSnapshot.child("nombre").getValue(String.class); // Obtener el nombre del grupo
                        String groupOwner = groupSnapshot.child("owner").getValue(String.class); // Obtener el propietario del grupo

                        if (groupName != null && groupId != null) {
                            DatabaseReference membersRef = database.getReference("grupos/" + groupId + "/miembros");
                            membersRef.get().addOnCompleteListener(membersTask -> {
                                boolean isMemberOrOwner = false;

                                if (membersTask.isSuccessful()) {
                                    for (DataSnapshot memberSnapshot : membersTask.getResult().getChildren()) {
                                        String memberEmail = memberSnapshot.child("email").getValue(String.class);
                                        if (memberEmail != null && memberEmail.equals(user.getEmail())) {
                                            isMemberOrOwner = true;
                                            break;
                                        }
                                    }
                                }

                                if (isMemberOrOwner || (groupOwner != null && groupOwner.equals(user.getEmail()))) {
                                    groupNames.add(groupName);
                                    addGroupButton(groupName, groupId);
                                }
                            });
                        }
                    }
                } else {
                    Log.e("Firebase", "Error al cargar los grupos: " + (task.getException() != null ? task.getException().getMessage() : "No hay grupos disponibles."));
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

    private void addGroupButton(String groupName, String groupId) {
        Button button = new Button(this);
        button.setText(groupName);
        button.setAllCaps(false);
        button.setTag(groupId);

        button.setBackgroundColor(Color.parseColor("#BDEEE5"));
        button.setTextColor(Color.parseColor("#09332B"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGroupDetails(groupId, groupName);
            }
        });

        groupContainer.addView(button);
    }

    private void openGroupDetails(String groupId, String groupName) {
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