
package com.nodejes.furryfunds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;

public class CambiarIconoVista extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cambiar_icono_vista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cambiarIconoVista), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });
        Log.i("Hola","Hola");
        ponerImagenes();
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

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(iconosId[i]);
            imageView.setPadding(10, 10, 10, 10);
            gridLayout.addView(imageView);
        }
        }


}


