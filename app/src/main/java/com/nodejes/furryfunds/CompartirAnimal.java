package com.nodejes.furryfunds;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CompartirAnimal extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.compartir_animal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.compartirAnimal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        startService(new Intent(this, MusicFondoService.class)); // Inicia la música
        cargarImagenPerfil();
        findViewById(R.id.shareButton2).setOnClickListener(v -> compartirImagen());

    }

    public void InicioViewCompartirAnimal(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void PerfilViewGrupoCompartirAnimal(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }
    public void cargarImagenPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("usuarios/" + user.getUid() + "/imagenSeleccionada");

            myRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Integer imagenSeleccionada = task.getResult().getValue(Integer.class);
                    if (imagenSeleccionada != null) {
                        // Si se encuentra una imagen seleccionada, actualizamos el ImageView
                        ImageView perfilImageView = findViewById(R.id.imageViewCompartir);
                        perfilImageView.setImageResource(imagenSeleccionada);
                    }
                } else {
                    Log.e("Firebase", "Error al recuperar la imagen seleccionada: " + task.getException().getMessage());
                }
            });
        } else {
            Log.e("Firebase", "No hay usuario autenticado.");
        }
    }
    private void compartirImagen() {
        ImageView imageView = findViewById(R.id.imageViewCompartir);
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();

        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "imagen_compartir.png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            Uri uri = androidx.core.content.FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    file
            );

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "¡Mira esta imagen en FurryFunds!");

            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Compartir imagen"));

        } catch (IOException e) {
            Log.e("Compartir", "Error al compartir la imagen: " + e.getMessage());
        } finally {
            imageView.setDrawingCacheEnabled(false);
        }
    }

}
