package com.example.eventos_denuncia.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventos_denuncia.LoginResponse;
import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.SharedPrefManager;
import com.example.eventos_denuncia.Usuario;
import com.example.eventos_denuncia.api.RetrofitClient;
import com.example.eventos_denuncia.secciones.CambiarPass;
import com.example.eventos_denuncia.secciones.EditarPerfil;
import com.example.eventos_denuncia.secciones.Inicio;
import com.example.eventos_denuncia.secciones.VerPerfil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextName, editTextTelefono, editTextFechaN, editTextApellido;
    private EditText cambiarPass;
    private EditText cambiarPass1;
    private DrawerLayout dl;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);



        dl = (DrawerLayout) findViewById(R.id.dl);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                int id = item.getItemId();

                if(id==R.id.inicio)
                {fragment = new Inicio();

                }

                if(id==R.id.perfil)
                {fragment = new VerPerfil(); }

                if(id==R.id.modificar_contraseña)
                {fragment = new CambiarPass();}

                if(id==R.id.desactivar)
                {desactivarUsuario();}

                if(id==R.id.cerrar_sesion)
                {logout();}

                if(id==R.id.salir){
                  finish();
                  System.exit(0);}

                if(fragment!=null)
                {mostrarSeccion(fragment);
                    dl.closeDrawers();}

                return true;
            }
        });


       // Intent intent = new Intent(this,MapsActivity.class);
        //startActivity(intent);
        mostrarSeccion(new Inicio());
    }

    private void mostrarSeccion(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void logout(){
        SharedPrefManager.getInstance(this).clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

   public void cambiarpass(View view) {

       cambiarPass = findViewById(R.id.cambiarPass);
       cambiarPass1 = findViewById(R.id.cambiarPass1);

       String currentpassword = cambiarPass.getText().toString().trim();
       String newpassword = cambiarPass1.getText().toString().trim();

       if (currentpassword.isEmpty()) {
           cambiarPass.setError("Contraseña requerida");
           cambiarPass.requestFocus();
           return;

       }

       if (newpassword.isEmpty()) {
           cambiarPass1.setError("Nueva contraseña requerida");
           cambiarPass1.requestFocus();
           return;
       }

       if (newpassword.length() < 6) {
           cambiarPass1.setError("La contraseña debe tener al menos 6 caracteres");
           cambiarPass1.requestFocus();
           return;
       }


       Usuario usuario = SharedPrefManager.getInstance(this).getUsuario();

       Call<ResponseBody> call = RetrofitClient.getInstance().getApi().updatePassword(currentpassword, newpassword, usuario.getEmail());

       call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               String s = null;

               try {
                   if (response.code() == 200) {
                       s = response.body().string();
                   } else {
                       s = response.errorBody().string();
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }

               if (s != null) {
                   try {
                       JSONObject jsonObject = new JSONObject(s);
                       Toast.makeText(PerfilActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {

           }
       });
   }


    public void editarPefil(View view)
    {
         mostrarSeccion(new EditarPerfil());
    }

    public void desactivarUsuario()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        desactivar();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Está apunto de desactivar su Cuenta.\nDesea Continuar?").setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void desactivar(){
        Usuario usuario = SharedPrefManager.getInstance(this).getUsuario();
        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .desactivarUsuario(
                        usuario.getCedula(),
                        usuario.getNombre()
                );

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if(!loginResponse.isError()){
                    Toast.makeText(PerfilActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    logout();

                }
                else{
                    Toast.makeText(PerfilActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void GuardarCambios(View view)
    {

        editTextName = findViewById(R.id.nombre1);
        editTextApellido = findViewById(R.id.apellido);
        editTextEmail = findViewById(R.id.email);
        editTextTelefono = findViewById(R.id.telefono);
        editTextFechaN = findViewById(R.id.fechan);

        String nombre = editTextName.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String fechaN = editTextFechaN.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();


        if(nombre.isEmpty()){
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

        Usuario usuario = SharedPrefManager.getInstance(this).getUsuario();
        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateUser(
                        usuario.getCedula(),
                        nombre,
                        apellido,
                        email,
                        telefono,
                        fechaN
                );

        call.enqueue(new Callback<LoginResponse>() {
          @Override
          public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
              LoginResponse loginResponse = response.body();

              if(!loginResponse.isError()){
                  Toast.makeText(PerfilActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                  SharedPrefManager.getInstance(PerfilActivity.this).guardarUsuario(response.body().getUser());

              }
              else{
                  Toast.makeText(PerfilActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
              }
          }

          @Override
          public void onFailure(Call<LoginResponse> call, Throwable t) {

          }
      });

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
