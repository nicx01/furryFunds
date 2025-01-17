package com.nodejes.furryfunds;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PerfilVista extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.perfil_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaPerfil), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView email=findViewById(R.id.correoEjemploTextView);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email.setText(user.getEmail());
        Button eliminarCuenta=findViewById(R.id.eliminarCuentaButton);
        eliminarCuenta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeleteAccount();
            }

        });
    }

    public void CompartirView(View v) {
        Intent intent = new Intent(this, CompartirAnimal.class);
        startActivity(intent);
    }

    public void CambiarIconoView(View v) {
        Intent intent = new Intent(this, CambiarIconoVista.class);
        startActivity(intent);
    }

    public void InicioView(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void ModificarPerfil(View v){
        Intent intent = new Intent(this, PerfilVista.class);
    }

    public void DeleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("TAG", "Error al eliminar la cuenta: " + task.getException().getMessage());
                        }
                    });
        } else {
            Log.e("TAG", "No hay usuario autenticado.");
        }
    }


    /*public void ModificarPerfil(View v){
        Intent intent = new Intent(this, )
    }*/
}
