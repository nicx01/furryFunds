/*
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



    GridView gridIcono = findViewById(gridLayout);

    public void cargarIconos(){
        String[] iconosid = {"E:\\FurryFo\\furryFunds\\app\\src\\main\\res\\mipmap-anydpi-v26\\iconogoku.xml" };
        for(String icono : iconosid){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(Integer.parseInt(icono));
            imageView.setPadding(10, 10, 10, 10);

        }

    }





    ImageView imagenperfil = findViewById(imageView3);
    if (imagenperfil.)






}





    int[] iconosId = {
            R.mipmap.iconogoku,
            R.mipmap.iconohormiga


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_icono_vista);




        GridView gridIcono = findViewById(gridLayout);

        ImageAdapter adapter = new ImageAdapter(this, iconosId);
        gridIcono.setAdapter(adapter);








        /*
        gridIcono.setAdapter(new BaseAdapter(gridLayout) {

            public int getCount() {
                return iconosId.length;}

            public Object getItem(int position) {
                return iconosId[position];
            }
            public long getItemId(int position) {
                return position; // Devuelve la posición
            }
            public ImageView getView(int position, View convertView, android.view.ViewGroup parent) {

                ImageView imageView;
                    imageView = new ImageView(CambiarIconoVista.this);
                    imageView.setPadding(10, 10, 10, 10);
                    imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setImageResource(iconosId[position]);
                return imageView;
            }
        });
        }


}
*/
package com.nodejes.furryfunds;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;

public class CambiarIconoVista extends AppCompatActivity {
    int[] iconosId = {
            R.mipmap.iconogoku,
            R.mipmap.iconohormiga
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_icono_vista);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();
        int numColumns = 3;
        for (int i = 0; i < iconosId.length; i++) {
            // Crear un nuevo ImageView para cada imagen
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(iconosId[i]);
            imageView.setLayoutParams(new GridLayout.LayoutParams());
            imageView.setPadding(10, 10, 10, 10);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) imageView.getLayoutParams();
            params.width = 200;  // Establecer el ancho de la celda
            params.height = 200;  // Establecer la altura de la celda
            imageView.setLayoutParams(params);
            gridLayout.addView(imageView);
        }

    }
}

