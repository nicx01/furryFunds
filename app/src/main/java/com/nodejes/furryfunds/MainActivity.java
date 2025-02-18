package com.nodejes.furryfunds;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private boolean isMuted = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_vista);

        Intent musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);
        stopService(new Intent(this, MusicFondoService.class));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaRegistro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnMute = findViewById(R.id.btnMute);
        btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMuted) {
                    stopService(new Intent(MainActivity.this, MusicService.class));
                    isMuted = true;
                    btnMute.setText("Unmute Music");
                    Log.d("MainActivity", "Música silenciada");
                } else {
                    startService(new Intent(MainActivity.this, MusicService.class));
                    isMuted = false;
                    btnMute.setText("Mute Music");
                    Log.d("MainActivity", "Música reactivada");
                }
            }
        });
    }

    public void registrar(View view) {
        TextView editTextPassword = findViewById(R.id.editTextPassword);
        TextView editTextNombre = findViewById(R.id.editTextNombre);
        boolean errorNombre = false;
        boolean errorPassword = false;
        if (editTextPassword.getText().length() != 0 && !editTextPassword.getText().toString().equals("")) {
            errorPassword = true;
        } else {
            editTextPassword.setError("Contraseña inválida");
            errorPassword = false;
        }
        if (editTextNombre.getText().length() != 0 && !editTextNombre.getText().toString().equals("")) {
            errorNombre = true;
        } else {
            editTextNombre.setError("Nombre inválido");
            errorNombre = false;
        }
        if (errorNombre && errorPassword) {
            FireBase fireBase = new FireBase();
            fireBase.addUser(editTextNombre.getText().toString(), editTextPassword.getText().toString(), new FirebaseCallback() {
                @Override
                public void onCheckComplete(boolean exists) {
                    if (exists) {
                        Log.d("TAG", "El usuario existe y se ha autenticado correctamente.");
                        Intent intent = new Intent(getApplicationContext(), VistaInicio.class);
                        startActivity(intent);
                    } else {
                        Log.w("TAG", "El usuario no existe o las credenciales son incorrectas.");
                        editTextNombre.setError("ERROR");
                    }
                }
            });
        }
    }

    public void iniciarSesion(View view) {
        TextView editTextPassword = findViewById(R.id.editTextPassword);
        TextView editTextNombre = findViewById(R.id.editTextNombre);
        boolean errorNombre = false;
        boolean errorPassword = false;

        if (editTextPassword.getText().length() != 0 && !editTextPassword.getText().toString().equals("")) {
            errorPassword = true;
        } else {
            editTextPassword.setError("Contraseña inválida");
            errorPassword = false;
        }

        if (editTextNombre.getText().length() != 0 && !editTextNombre.getText().toString().equals("")) {
            errorNombre = true;
        } else {
            editTextNombre.setError("Nombre inválido");
            errorNombre = false;
        }

        if (errorNombre && errorPassword) {
            FireBase fireBase = new FireBase();
            fireBase.signInUser(editTextNombre.getText().toString(), editTextPassword.getText().toString(), new FirebaseCallback() {
                @Override
                public void onCheckComplete(boolean exists) {
                    if (exists) {
                        Log.d("TAG", "El usuario existe y se ha autenticado correctamente.");
                        Intent intent = new Intent(getApplicationContext(), VistaInicio.class);
                        startActivity(intent);
                    } else {
                        Log.w("TAG", "El usuario no existe o las credenciales son incorrectas.");
                        editTextNombre.setError("Nombre incorrecto");
                        editTextPassword.setError("Contraseña incorrecta");
                    }
                }
            });
        }
    }
}