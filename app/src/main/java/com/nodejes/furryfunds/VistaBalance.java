package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VistaBalance extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ver_balance_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaBalance), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void InicioViewBalance(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void PerfilViewBalance(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }
}
