package com.example.eventos_denuncia.secciones;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.SharedPrefManager;
import com.example.eventos_denuncia.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VerPerfil extends Fragment {

    private TextView nombre;
    private TextView cedula;
    private TextView email;
    private TextView telefono;
    private TextView fechan;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.ver_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nombre = view.findViewById(R.id.nombre);
        cedula = view.findViewById(R.id.cedula);
        email = view.findViewById(R.id.email);
        telefono = view.findViewById(R.id.telefono);
        fechan = view.findViewById(R.id.fechan);

        Usuario usuario = SharedPrefManager.getInstance(getActivity()).getUsuario();
        nombre.setText("Bienvenido " + usuario.getNombre() + "," +usuario.getApellido() );
        cedula.setText(usuario.getCedula());
        email.setText(usuario.getEmail());
        telefono.setText(usuario.getTelefono());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(usuario.getFechan());
            format.applyPattern("dd-MM-yyyy");
            String fecha = format.format(date);
            fechan.setText(fecha);
        }
        catch(ParseException e){

        }


    }
}
