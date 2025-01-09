package com.nodejes.furryfunds;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class FireBase extends AppCompatActivity {
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_vista);

        mAuth = FirebaseAuth.getInstance();

        String email = "admin@admin.com";
        String password = "admin";

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("TAG", "Usuario registrado: " + (user != null ? user.getEmail() : "N/A"));
                        } else {
                            Log.w("TAG", "Error en el registro", task.getException());
                        }
                    }
                });
    }
    public void addUser(String email, String password, FirebaseCallback callback) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("TAG", "Usuario registrado: " + (user != null ? user.getEmail() : "N/A"));
                            callback.onCheckComplete(true);
                        } else {
                            Log.w("TAG", "Error en el registro", task.getException());
                            callback.onCheckComplete(false);
                        }
                    }
        });
    }
    public void signInUser(String email, String password, FirebaseCallback callback) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("TAG", "signInWithEmail:success, Usuario: " + user.getEmail());
                            callback.onCheckComplete(true);
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            callback.onCheckComplete(false);
                        }
                    }
                });
    }
}
