package com.example.eventos_denuncia.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.SharedPrefManager;
import com.example.eventos_denuncia.api.RetrofitClient;
import com.example.eventos_denuncia.secciones.Fechas;
import com.example.eventos_denuncia.secciones.Inicio;
import com.example.eventos_denuncia.utils.AccionElegirFecha;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmail, editTextPassword, editTextName, editTextCedula,
            editTextTelefono, editTextFechaN, editTextApellido, editTextRepeatPassword;
    private ImageButton elegirFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCedula = findViewById(R.id.editTextCedula);
        editTextName = findViewById(R.id.editTextName);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextFechaN = findViewById(R.id.editTextFechaN);


        elegirFecha = findViewById(R.id.elegirFecha);


        elegirFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AccionElegirFecha(editTextFechaN).setFecha(MainActivity.this);
            }
        });



        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(SharedPrefManager.getInstance(this).estaLogueado()){

            Intent intent = new Intent(this, PerfilActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        }
    }

    private void userSignUp(){
        String cedula = editTextCedula.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String paswword2 = editTextRepeatPassword.getText().toString().trim();
        String fechaN = editTextFechaN.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();


        if(cedula.isEmpty()){
            editTextCedula.setError("La cedula es obligatoria");
            editTextCedula.requestFocus();
            return;
        }

        if(name.isEmpty()){
            editTextName.setError("El nombre es obligatorio");
            editTextName.requestFocus();
            return;
        }

        if(apellido.isEmpty()){
            editTextApellido.setError("El apellido es obligatorio");
            editTextApellido.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("El Email es obligatario");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Ingrese un Email valido");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("La contraseña es obligatoria");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("La contraseña debe tener al menos 6 caracteres");
            editTextPassword.requestFocus();
            return;
        }

        if(!password.equals(paswword2)){
            editTextRepeatPassword.setError("Las contrase\u00f1as no coinciden");
            editTextRepeatPassword.requestFocus();
            return;
        }

        if(fechaN.isEmpty()){
            editTextFechaN.setError("La fecha de nacimiento es obligataria");
            editTextFechaN.requestFocus();
            return;
        }

        if(telefono.isEmpty()){
            editTextTelefono.setError("El telefono es obligatorio");
            editTextTelefono.requestFocus();
            return;
        }


        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(cedula, name, apellido, email, password, telefono, fechaN);

    call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            String s = null;

            try {
                if (response.code() == 201){
                    showCustomDialog();
                }
                else{
                    s = response.errorBody().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (s != null){
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
        }
    });


    }

    private void showCustomDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.registrarse_exito, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void arrancarInicio(View view){

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonSignUp:
                userSignUp();
                break;
            case R.id.textViewLogin:
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }
    }


}
