package com.example.eventos_denuncia.secciones;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventos_denuncia.Evento;
import com.example.eventos_denuncia.EventoResponse;
import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.api.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Inicio extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Evento> eventos;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_maps, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Call<EventoResponse> call= RetrofitClient.getInstance().getApi().obtenerEventos();
        call.enqueue(new Callback<EventoResponse>() {
            @Override
            public void onResponse(Call<EventoResponse> call, Response<EventoResponse> response) {
                EventoResponse eventoResponse = response.body();
                if(!eventoResponse.isError()){
                    //Toast.makeText(getActivity(),"LLEGA", Toast.LENGTH_LONG).show();
                    eventos = eventoResponse.getEventos();
                    //Toast.makeText(getActivity(),eventos.get(0).getNombre(), Toast.LENGTH_LONG).show();
                   // LatLng paysandu1 = new LatLng(eventos.get(0).getLatitud(),eventos.get(0).getLongitud());
                    //mMap.addMarker(new MarkerOptions().position(paysandu1).title("Marca 2 de prueba"));

                    for (int i=0; i<eventos.size(); i++) {

                    int id = eventos.get(i).getId();
                    String nombre = eventos.get(i).getNombre();
                    String descripcion = eventos.get(i).getDescripcion();
                    double longitud = eventos.get(i).getLongitud();
                    double latitud = eventos.get(i).getLatitud();
                    String foto = eventos.get(i).getFoto();
                    int idEstado = eventos.get(i).getIdEstado();
                    int activo = eventos.get(i).getActivo();

                    LatLng nuevo = new LatLng(latitud,longitud);
                    mMap.addMarker(new MarkerOptions().position(nuevo).title(nombre));

       }




                }
            }

            @Override
            public void onFailure(Call<EventoResponse> call, Throwable t) {


                Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_LONG).show();


            }
        });


        // Agrega el marcador en Paysandu y mueve la camara
        LatLng paysandu = new LatLng(-32.32139, -58.07556);
        mMap.addMarker(new MarkerOptions().position(paysandu).title("Marca de Prueba Paysandu"));
        float zoomLevel = 13.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paysandu, zoomLevel));
    }

}
