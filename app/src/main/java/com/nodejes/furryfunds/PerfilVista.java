package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        Log.d("butooon","funciona");
        Button buttonPerfil=findViewById(R.id.InicioButton);
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InicioView(v);
                Log.d("butooon","funciona");
            }
        });

        Log.d("butooon","funciona");
        Button buttonCambiar=findViewById(R.id.cambiarButton);
        buttonCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambiarIconoView(v);
                Log.d("butooon","funciona");
            }
        });

        Log.d("butooon","funciona");
        Button buttonShare=findViewById(R.id.shareButton);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compartirView(v);
                Log.d("butooon","funciona");
            }
        });



    }

    private void compartirView(View v) {
        Intent intent = new Intent(this, CompartirAnimal.class);
        startActivity(intent);
    }

    private void CambiarIconoView(View v) {
        Intent intent = new Intent(this, CambiarIconoVista.class);
        startActivity(intent);
    }

    private void InicioView(View v) {
        Intent intent = new Intent(this, ToolbarInicio.class);
        startActivity(intent);
    }

    private void ModificarPerfil(View v){
        Intent intent = new Intent(this, )
    }
}
