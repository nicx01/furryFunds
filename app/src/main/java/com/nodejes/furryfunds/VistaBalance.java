package com.nodejes.furryfunds;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        calcularDeudasYMostrar();

    }

    public void InicioViewBalance(View v) {
        Intent intent = new Intent(this, VistaInicio.class);
        startActivity(intent);
    }

    public void PerfilViewBalance(View v) {
        Intent intent = new Intent(this, PerfilVista.class);
        startActivity(intent);
    }

    private void calcularDeudasYMostrar() {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("GROUP_ID");
        LinearLayout movimientosContainer = findViewById(R.id.movimientosContainer);
        movimientosContainer.removeAllViews();

        if (groupId != null && !groupId.isEmpty()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://furryfunds-29d6b-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference grupoRef = database.getReference("grupos/" + groupId);

            grupoRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DataSnapshot snapshot = task.getResult();
                    List<String> miembros = new ArrayList<>();
                    Map<String, Double> pagos = new HashMap<>();
                    double totalGastos = 0.0;

                    // Obtener el owner
                    String owner = snapshot.child("owner").getValue(String.class);
                    if (owner != null) {
                        miembros.add(owner);
                        pagos.put(owner, 0.0);
                    }

                    // Obtener miembros
                    for (DataSnapshot miembroSnap : snapshot.child("miembros").getChildren()) {
                        String email = miembroSnap.child("email").getValue(String.class);
                        if (email != null && !miembros.contains(email)) {
                            miembros.add(email);
                            pagos.put(email, 0.0);
                        }
                    }

                    // Obtener gastos
                    for (DataSnapshot gastoSnap : snapshot.child("gastos").getChildren()) {
                        String usuario = gastoSnap.child("usuario").getValue(String.class);
                        double cantidad = Double.parseDouble(gastoSnap.child("cantidad").getValue(String.class));

                        totalGastos += cantidad;
                        if (usuario != null && pagos.containsKey(usuario)) {
                            pagos.put(usuario, pagos.get(usuario) + cantidad);
                        }
                    }

                    // Calcular cuánto debería haber pagado cada miembro
                    double cuotaPorPersona = totalGastos / miembros.size();
                    Map<String, Double> balance = new HashMap<>();

                    for (String miembro : miembros) {
                        balance.put(miembro, pagos.get(miembro) - cuotaPorPersona);
                    }

                    // Determinar deudas
                    List<String> movimientos = calcularTransferencias(balance);

                    // Mostrar los movimientos en la interfaz
                    runOnUiThread(() -> {
                        for (String movimiento : movimientos) {
                            TextView textView = new TextView(this);
                            textView.setText(movimiento);
                            textView.setTextSize(16);
                            textView.setTypeface(null, Typeface.BOLD);
                            movimientosContainer.addView(textView);
                        }
                    });
                }
            });
        }
    }

    private List<String> calcularTransferencias(Map<String, Double> balance) {
        List<String> movimientos = new ArrayList<>();
        List<Map.Entry<String, Double>> deudores = new ArrayList<>();
        List<Map.Entry<String, Double>> acreedores = new ArrayList<>();

        for (Map.Entry<String, Double> entry : balance.entrySet()) {
            if (entry.getValue() < 0) {
                deudores.add(entry);
            } else if (entry.getValue() > 0) {
                acreedores.add(entry);
            }
        }

        int i = 0, j = 0;
        while (i < deudores.size() && j < acreedores.size()) {
            Map.Entry<String, Double> deudor = deudores.get(i);
            Map.Entry<String, Double> acreedor = acreedores.get(j);

            double cantidad = Math.min(-deudor.getValue(), acreedor.getValue());
            movimientos.add(deudor.getKey() + " " + cantidad + "€ " + acreedor.getKey());

            deudor.setValue(deudor.getValue() + cantidad);
            acreedor.setValue(acreedor.getValue() - cantidad);

            if (deudor.getValue() == 0) i++;
            if (acreedor.getValue() == 0) j++;
        }

        return movimientos;
    }
}
