package com.example.eventos_denuncia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.SharedPrefManager;
import com.example.eventos_denuncia.Usuario;

public class PerfilActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        textView = findViewById(R.id.textView);

        Usuario usuario = SharedPrefManager.getInstance(this).getUsuario();
        textView.setText("Bienvenido " + usuario.getNombre());
    }

   /* @Override
    protected void onStart() {
        super.onStart();

        if(!SharedPrefManager.getInstance(this).estaLogueado()){

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        }
    }*/
}
