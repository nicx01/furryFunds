package com.nodejes.furryfunds;

import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        loadGroupMembers();
    }
    public void cargarFurros() {
        ScrollView scrollView = findViewById(R.id.scrollViewFurrosGrupo);

        if (scrollView != null) {
            LinearLayout container = new LinearLayout(this);
            container.setOrientation(LinearLayout.VERTICAL);
            container.removeAllViews();

            for (String email : emailList) {
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setPadding(16, 16, 16, 16);
                itemLayout.setBackgroundColor(Color.parseColor("#BDEEE5")); // Fondo verde claro

                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100, 100); // ancho y alto en píxeles
                imageView.setLayoutParams(imageParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //imageView.setImageResource(R.drawable.default_avatar); // Imagen por defecto (debe existir en tus recursos)

                TextView textView = new TextView(this);
                textView.setText(email);
                textView.setTextSize(16);
                textView.setTextColor(Color.parseColor("#09332B"));
                textView.setPadding(16, 0, 0, 0);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                itemLayout.addView(imageView);
                itemLayout.addView(textView);

                itemLayout.setOnClickListener(v -> mostrarConfirmacionEliminacion(email));

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
                Query userQuery = database.getReference("usuarios").orderByChild("email").equalTo(email);
                userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Integer imagenSeleccionada = ds.child("imagenSeleccionada").getValue(Integer.class);
                                if (imagenSeleccionada != null) {
                                    imageView.setImageResource(imagenSeleccionada);
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("Firebase", "Error al cargar la imagen para " + email, error.toException());
                    }
                });

                container.addView(itemLayout);
            }
            scrollView.removeAllViews();
            scrollView.addView(container);
        }
    }


    public void addFurros(View v) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            mostrarPopUp("No hay usuario autenticado.", true);
            return;
        }

        String currentUserEmail = currentUser.getEmail(); // Obtener el correo del usuario autenticado

        EditText emailInput = new EditText(this);
        emailInput.setHint("Ingresa el correo"); // Texto guía para el usuario

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Agregar correo")
                .setMessage("Introduce el correo para agregarlo al grupo:")
                .setView(emailInput)
                .setPositiveButton("Agregar", (dialogInterface, i) -> {
                    String email = emailInput.getText().toString().trim();

                    if (email.isEmpty()) {
                        mostrarPopUp("El correo no puede estar vacío.", true);
                        return;
                    }

                    if (email.equals(currentUserEmail)) {
                        mostrarPopUp("No puedes agregarte a ti mismo.", true);
                        return;
                    }

                    verificarEmailEnBaseDeDatos(email, new FirebaseCallback() {
                        @Override
                        public void onCheckComplete(boolean exists) {
                            if (exists) {
                                addGroupMember(email);
                                cargarFurros();
                            } else {
                                mostrarPopUp("El correo no está registrado en la aplicación.", true);
                            }
                        }
                    });
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        dialog.show();
    }

    private void verificarEmailEnBaseDeDatos(String email, FirebaseCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference usersRef = database.getReference("usuarios");

        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                boolean exists = false;
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    String storedEmail = userSnapshot.child("email").getValue(String.class); // Ahora busca en la base de datos
                    if (email.equalsIgnoreCase(storedEmail)) {
                        exists = true;
                        break;
                    }
                }
                callback.onCheckComplete(exists);
            } else {
                Log.e("Firebase", "Error al verificar el correo en la base de datos", task.getException());
                callback.onCheckComplete(false);
            }
        });
    }

    interface FirebaseCallback {
        void onCheckComplete(boolean exists);
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
                    boolean exists = emailList.contains(email.trim());

                    if (exists) {
                        eliminarMiembro(email);
                        cargarFurros(); // Actualizar la vista después de eliminar
                    } else {
                        mostrarPopUp("Error: El correo no se encontró en la lista.", true);
                    }
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss())
                .create()
                .show();
    }
    private void eliminarMiembro(String email) {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("GROUP_ID");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return; // Verificar que el usuario esté autenticado

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference groupOwnerRef = database.getReference("grupos/" + groupId + "/owner");

        // Verificar si el usuario es el propietario del grupo
        groupOwnerRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String groupOwner = task.getResult().getValue(String.class);

                if (groupOwner != null && groupOwner.equals(user.getEmail())) {
                    // Si el usuario es el propietario, proceder a eliminar el miembro
                    DatabaseReference membersRef = database.getReference("grupos/" + groupId + "/miembros");

                    membersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String storedEmail = snapshot.child("email").getValue(String.class);
                                if (storedEmail != null && storedEmail.equals(email)) {
                                    snapshot.getRef().removeValue()
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Log.d("Firebase", "Miembro eliminado con éxito.");
                                                    mostrarPopUp("Miembro eliminado con éxito.", false);
                                                } else {
                                                    Log.e("Firebase", "Error al eliminar el miembro: " + task.getException().getMessage());
                                                    mostrarPopUp("Error al eliminar el miembro.", true);
                                                }
                                            });
                                    break; // Salir después de encontrar y eliminar el miembro
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", "Error al eliminar miembro: " + databaseError.getMessage());
                        }
                    });
                } else {
                    // Si no eres el propietario, no puedes eliminar miembros
                    Log.e("Firebase", "Solo el propietario del grupo puede eliminar miembros.");
                    mostrarPopUp("No eres el propietario del grupo, no puedes eliminar miembros.", true);
                }
            } else {
                Log.e("Firebase", "Error al obtener el propietario del grupo: " + task.getException().getMessage());
            }
        });
    }

    private void addGroupMember(String email) {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("GROUP_ID");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference groupOwnerRef = database.getReference("grupos/" + groupId + "/owner");

        // Verificar si el usuario es el propietario del grupo
        groupOwnerRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String groupOwner = task.getResult().getValue(String.class);

                if (groupOwner != null && groupOwner.equals(userEmail)) {
                    // Si el usuario es el propietario, agregar el miembro al grupo
                    DatabaseReference membersRef = database.getReference("grupos/" + groupId + "/miembros");

                    String memberId = membersRef.push().getKey(); // Generar un ID único para el miembro
                    if (memberId != null) {
                        Map<String, Object> memberData = new HashMap<>();
                        memberData.put("email", email);

                        membersRef.child(memberId).setValue(memberData)
                                .addOnCompleteListener(addMemberTask -> {
                                    if (addMemberTask.isSuccessful()) {
                                        Log.d("Firebase", "Miembro agregado correctamente al grupo.");
                                    } else {
                                        Log.e("Firebase", "Error al agregar miembro: " + addMemberTask.getException().getMessage());
                                    }
                                });
                    }
                } else {
                    Log.e("Firebase", "Solo el propietario del grupo puede agregar miembros.");
                    Toast.makeText(this, "No eres el propietario del grupo, no puedes agregar miembros.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Firebase", "Error al obtener el propietario del grupo: " + task.getException().getMessage());
            }
        });
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
    private void loadGroupMembers() {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("GROUP_ID");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return; // Salir si el usuario no está autenticado

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference membersRef = database.getReference("grupos/" + groupId + "/miembros");

        membersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailList.clear(); // Limpiar la lista antes de agregar nuevos datos
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String email = snapshot.child("email").getValue(String.class);
                    if (email != null) {
                        emailList.add(email);
                    }
                }
                cargarFurros(); // Actualizar la vista o hacer algo con la lista
                Log.d("Firebase", "Miembros cargados: " + emailList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error al cargar miembros: " + databaseError.getMessage());
            }
        });
    }


}