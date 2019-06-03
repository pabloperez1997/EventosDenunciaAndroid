package com.example.eventos_denuncia.secciones;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.api.RetrofitClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarDenuncia extends Fragment {
    private GoogleMap mMap;
    private double latitud, longitud;
    private EditText titulo;
    private EditText desc;
    private FloatingActionButton button;



    Marker marker;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_denuncia, container, false);
        return view;
    }
public void setLatitudLong (double lat, double lonng){

        this.latitud= lat;
        this.longitud=lonng;
}
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        final Button denuncia = view.findViewById(R.id.btn_denunciar);
        denuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaDenuncia(v);
            }
        });

        final Button cancelar = view.findViewById(R.id.btn_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button = (FloatingActionButton) v.findViewById(R.id.fab);
                button.show();
            }
        });

      //  Toast.makeText(getActivity(),String.valueOf(this.latitud),Toast.LENGTH_LONG).show();

        titulo = view.findViewById(R.id.txt_titulo);
        desc = view.findViewById(R.id.txt_descripcion);


    }

    private void nuevaDenuncia(View v) {


        String nombre = titulo.getText().toString();
        String descripcion= desc.getText().toString();

        double latitud1 = latitud;
        double longitud1 = longitud;


        String foto = "Sin foto";
        int idEstado = 1;
        int activo = 1;


        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .crearEvento(nombre,descripcion,longitud1,latitud1,foto,idEstado,activo);

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
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
