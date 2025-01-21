package com.nodejes.furryfunds;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CambiarIconoVista extends AppCompatActivity {
    int imagenseleccionada = 0;
    ImageButton lastSelectedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cambiar_icono_vista);

        // Establecer márgenes para las barras del sistema (por ejemplo, la barra de estado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cambiarIconoVista), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ponerImagenes();

        cargarImagenSeleccionada();

        Button cambiar = findViewById(R.id.buttonCambiarIcono);
        cambiar.setOnClickListener(v -> {
            cambiarIconoPerfil(imagenseleccionada);
        });
    }

    public void cargarImagenSeleccionada() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("usuarios/" + user.getUid() + "/imagenSeleccionada");

            myRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Integer imagenSeleccionada = task.getResult().getValue(Integer.class);
                    if (imagenSeleccionada != null) {
                        // Si se encuentra una imagen seleccionada, actualizamos el ImageView
                        ImageView imagen = findViewById(R.id.imageView3);
                        imagen.setImageResource(imagenSeleccionada);
                        imagenseleccionada = imagenSeleccionada; // Guardamos la imagen seleccionada en la variable
                    }
                } else {
                    Log.e("Firebase", "Error al recuperar la imagen seleccionada: " + task.getException().getMessage());
                }
            });
        } else {
            Log.e("Firebase", "No hay usuario autenticado.");
        }
    }

    public void cambiarIconoPerfil(int imagenseleccionada) {
        Log.w("ASDFAS", "ASDF");
        ImageView imagen = findViewById(R.id.imageView3);
        if (imagenseleccionada != 0) {
            imagen.setImageResource(imagenseleccionada);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("usuarios/" + user.getUid());

            myRef.child("imagenSeleccionada").setValue(imagenseleccionada)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "ID de imagen guardado correctamente.");
                        } else {
                            Log.e("Firebase", "Error al guardar el ID de la imagen: " + task.getException().getMessage());
                        }
                    });
        } else {
            Log.e("Firebase", "No hay usuario autenticado.");
        }
    }

    public void InicioViewIcono(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void PerfilViewIcono(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }

    public void ponerImagenes() {
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();

        int[] iconosId = {
                R.mipmap.iconogoku,
                R.mipmap.iconohormiga,
                R.mipmap.iconohomer,
                R.mipmap.iconobender,
                R.mipmap.iconofurry
        };

        for (int i = 0; i < iconosId.length; i++) {
            ImageButton imageButton = new ImageButton(this);
            imageButton.setImageResource(iconosId[i]);
            imageButton.setPadding(10, 10, 10, 10);
            imageButton.setBackgroundColor(Color.TRANSPARENT);

            final int iconId = iconosId[i];
            imageButton.setOnClickListener(v -> {
                if (lastSelectedButton != null) {
                    lastSelectedButton.setBackgroundColor(Color.TRANSPARENT); // Fondo por defecto
                }
                imagenseleccionada = iconId;
                imageButton.setBackgroundColor(Color.rgb(0, 0, 255));
                lastSelectedButton = imageButton;
            });

            gridLayout.addView(imageButton);
        }
    }
}
