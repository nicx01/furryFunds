package com.nodejes.furryfunds;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaRegistro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void registrar(View view) {
        TextView editTextPassword = findViewById(R.id.editTextPassword);
        TextView editTextNombre = findViewById(R.id.editTextNombre);
        boolean errorNombre=false;
        boolean errorPassword=false;
        if (editTextPassword.getText().length() != 0 && editTextPassword.getText() != ""){
            errorPassword=true;
        }else{
            editTextPassword.setError("Contraseña inválida");
            errorPassword=false;
        }
        if(editTextNombre.getText().length() != 0 && editTextNombre.getText() != ""){
            errorNombre=true;
        }else{
            editTextNombre.setError("Nombre inválido");
            errorPassword=false;
        }
        if(errorNombre==true && errorPassword==true) {
            Intent intent = new Intent(this, ToolbarPerfil.class);
            startActivity(intent);
        }

    }
    public void iniciarSesion(View view) {
        TextView editTextPassword = findViewById(R.id.editTextPassword);
        TextView editTextNombre = findViewById(R.id.editTextNombre);
        boolean errorNombre=false;
        boolean errorPassword=false;
        if (editTextPassword.getText().length() != 0 && editTextPassword.getText() != ""){
            errorPassword=true;
        }else{
            editTextPassword.setError("Contraseña inválida");
            errorPassword=false;
        }
        if(editTextNombre.getText().length() != 0 && editTextNombre.getText() != ""){
            errorNombre=true;
        }else{
            editTextNombre.setError("Nombre inválido");
            errorPassword=false;
        }
        if(errorNombre==true && errorPassword==true) {
            Intent intent = new Intent(this, ToolbarPerfil.class);
            startActivity(intent);
        }
    }

}