package com.example.eventos_denuncia;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_preff";
    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx){
        this.mCtx= mCtx;

    }

    public static  synchronized SharedPrefManager getInstance(Context mCtx)
    {
        if(mInstance==null){
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void guardarUsuario(Usuario usuario){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("ci",usuario.getCedula());
        editor.putString("nombre",usuario.getNombre());
        editor.putString("apellido",usuario.getApellido());
        editor.putString("email",usuario.getEmail());
        editor.putString("telefono",usuario.getTelefono());
        editor.putString("fechan",usuario.getFechan());

        editor.apply();

    }

    public boolean estaLogueado(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences.getString("ci", "UsuarioDefecto") != "UsuarioDefecto")
            return true;

        return false;
    }

    public Usuario getUsuario(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        Usuario usuario = new Usuario(
                sharedPreferences.getString("ci", "UsuarioDefecto"),
                sharedPreferences.getString("nombre", null),
                sharedPreferences.getString("apellido", null),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("telefono", null),
                sharedPreferences.getString("fechan", "2000-01-01")
                );

        return usuario;

    }

    public void clear(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}
