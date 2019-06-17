package com.example.eventos_denuncia.secciones;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventos_denuncia.AdapterDenunciaUsuario;
import com.example.eventos_denuncia.Evento;
import com.example.eventos_denuncia.EventoResponse;
import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.SharedPrefManager;
import com.example.eventos_denuncia.Usuario;
import com.example.eventos_denuncia.api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisReclamos extends Fragment {

    private RecyclerView recyclerView;
    private List<Evento> listaEventos;
    private AdapterDenunciaUsuario adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.eventos_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Usuario usuario = SharedPrefManager.getInstance(getActivity()).getUsuario();
        String ci= usuario.getCedula();

        Call<EventoResponse> call= RetrofitClient.getInstance().getApi().obtenerEventosdelusuario(ci);
        call.enqueue(new Callback<EventoResponse>() {
            @Override
            public void onResponse(Call<EventoResponse> call, Response<EventoResponse> response) {

                listaEventos = response.body().getEventos();
                adapter = new AdapterDenunciaUsuario(getActivity(),listaEventos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<EventoResponse> call, Throwable t) {

            }
        });


       // Button button = (Button) view.findViewById(R.id.buttonEditarEvento);
       // button.setOnClickListener(new View.OnClickListener() {
         //   public void onClick(View v) {
           //     editarEvento(v);
            //}
      //  });
        //button.show();
    }

    public void editarEvento(View view){


    }

}
