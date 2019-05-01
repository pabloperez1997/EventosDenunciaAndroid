package com.example.eventos_denuncia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.api.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmail, editTextPassword, editTextName, editTextCedula, editTextTelefono, editTextFechaN, editTextApellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCedula = findViewById(R.id.editTextCedula);
        editTextName = findViewById(R.id.editTextName);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextFechaN = findViewById(R.id.editTextFechaN);


        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);


    }
    private void userSignUp(){
        String cedula = editTextCedula.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
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
                s = response.body().string();
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
        /* Do user registration using the api call*/

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
