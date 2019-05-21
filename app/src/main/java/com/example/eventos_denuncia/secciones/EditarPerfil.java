package com.example.eventos_denuncia.secciones;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.SharedPrefManager;
import com.example.eventos_denuncia.Usuario;


public class EditarPerfil extends Fragment {


    private EditText nombre;
    private EditText apellido;
    private EditText email;
    private EditText telefono;
    private EditText fechan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.editar_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nombre = view.findViewById(R.id.nombre1);
        apellido = view.findViewById(R.id.apellido);
        email = view.findViewById(R.id.email);
        telefono = view.findViewById(R.id.telefono);
        fechan = view.findViewById(R.id.fechan);

        Usuario usuario = SharedPrefManager.getInstance(getActivity()).getUsuario();
        nombre.setText(usuario.getNombre());
        apellido.setText(usuario.getApellido());
        email.setText(usuario.getEmail());
        telefono.setText(usuario.getTelefono());
        fechan.setText(usuario.getFechan());
    }


}
