
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

public class CambiarIconoVista extends AppCompatActivity {
    int  imagenseleccionada =0;
    ImageButton lastSelectedButton = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cambiar_icono_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cambiarIconoVista), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        ponerImagenes();
        Button cambiar = findViewById(R.id.buttonCambiarIcono);
        cambiar.setOnClickListener(v -> {

           cambiarIconoPerfil(imagenseleccionada);
        });


    }
    public void cambiarIconoPerfil(int imagenseleccionada){
        Log.w("ASDFAS","ASDF");
        ImageView imagen = findViewById(R.id.imageView3);
        if (imagenseleccionada != 0) {
            imagen.setImageResource(imagenseleccionada);
        }
    }

    public void InicioViewIcono (View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void PerfilViewIcono(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }




      public void ponerImagenes(){

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();

         int[] iconosId = {
                R.mipmap.iconogoku,
                R.mipmap.iconohormiga
        };

        for (int i = 0; i < iconosId.length; i++) {
            ImageButton imageButton = new ImageButton(this);
            imageButton.setImageResource(iconosId[i]);
            imageButton.setPadding(10,10,10,10);
            imageButton.setBackgroundColor(Color.TRANSPARENT);

            final int iconId = iconosId[i];
            imageButton.setOnClickListener(v -> {
                if (lastSelectedButton != null) {
                    lastSelectedButton.setBackgroundColor(Color.TRANSPARENT); // Fondo por defecto
                }
                imagenseleccionada = iconId;
                imageButton.setBackgroundColor(Color.rgb(0, 0, 255));
               // cambiarIconoPerfil(imagenseleccionada);
                lastSelectedButton = imageButton;
            });

            gridLayout.addView(imageButton);
        }
      }
}


