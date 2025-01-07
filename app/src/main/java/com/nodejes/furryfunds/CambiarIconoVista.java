package com.nodejes.furryfunds;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    }


/*
    public void cargarIconos(){
        String[] iconosid = {"E:\\FurryFo\\furryFunds\\app\\src\\main\\res\\mipmap-anydpi-v26\\iconogoku.xml" };
        for(String icono : iconosid){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageId);
            imageView.setContentDescription("Icono");
            imageView.setPadding(10, 10, 10, 10);
        }

    }
    ImageView imagenperfil = findViewById(imageView3);
    if (imagenperfil.)


 */
}

