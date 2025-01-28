package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vistagrupo_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaGrupo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
