package com.example.eventos_denuncia.secciones;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventos_denuncia.Evento;
import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.api.RetrofitClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarDenuncia extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double latitud, longitud;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final Button denuncia = view.findViewById(R.id.btn_denunciar);
        denuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaDenuncia(v);
            }
        });

        Toast.makeText(getActivity(),String.valueOf(this.latitud),Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener nuevaDenuncia(View v) {

        double latitud = marker.getPosition().latitude;
        double longitud= marker.getPosition().longitude;
        String nombre= "Alta prueba";
        String descripcion= "Descripcion prueba";
        String foto = "Sin foto";
        int idEstado = 1;
        int activo = 1;


        Toast.makeText(getActivity(), String.valueOf(latitud)+String.valueOf(longitud), Toast.LENGTH_LONG).show();


        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .crearEvento(nombre,descripcion,longitud,latitud,foto,idEstado,activo);

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


   return (View.OnClickListener) v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(point.latitude,
                                        point.longitude)).title("Nuevo Marcador")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            }
        });
    }
}
