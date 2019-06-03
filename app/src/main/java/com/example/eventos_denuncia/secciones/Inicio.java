package com.example.eventos_denuncia.secciones;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventos_denuncia.Evento;
import com.example.eventos_denuncia.EventoResponse;
import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.api.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class Inicio extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Evento> eventos;
    Marker marker;

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

        final FloatingActionButton button =
                (FloatingActionButton) view.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nuevoEvento(v);
            }
        });

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

                        LatLng nuevo = new LatLng(latitud, longitud);

                        if (activo == 1){
                        if (eventos.get(i).getIdEstado()==1){
                            Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.bachered);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);

                            mMap.addMarker(new MarkerOptions().position(nuevo).title(nombre).snippet(descripcion + "\n" + "Estado:" + idEstado)
                                    .icon(bitmapDescriptor));



                        } else if (eventos.get(i).getIdEstado()==2) {
                            Bitmap img1 = BitmapFactory.decodeResource(getResources(),R.drawable.bacheyellow);
                            BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromBitmap(img1);
                            mMap.addMarker(new MarkerOptions().position(nuevo).title(nombre).snippet(descripcion + "\n" + "Estado:" + idEstado)
                                    .icon(bitmapDescriptor1));
                        }
                        else{
                            Bitmap img2 = BitmapFactory.decodeResource(getResources(),R.drawable.bachegreen);
                            BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromBitmap(img2);
                            mMap.addMarker(new MarkerOptions().position(nuevo).title(nombre).snippet(descripcion + "\n" + "Estado:" + idEstado)
                                    .icon(bitmapDescriptor2));
                        }
                    }
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                Context mContext = getActivity();

                                LinearLayout info = new LinearLayout(mContext);
                                info.setOrientation(LinearLayout.VERTICAL);


                                TextView title = new TextView(mContext);
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(mContext);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });

       }




                }
            }

            @Override
            public void onFailure(Call<EventoResponse> call, Throwable t) {


                Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_LONG).show();


            }
        });


        //Agrega el marcador en Paysandu y mueve la camara
        LatLng paysandu = new LatLng(-32.32139, -58.07556);
        mMap.addMarker(new MarkerOptions().position(paysandu).title("Marca de Prueba Paysandu"));
        float zoomLevel = 13.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paysandu, zoomLevel));


        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                if (marker != null) {
                    marker.remove();
                }
                Bitmap img3 = BitmapFactory.decodeResource(getResources(),R.drawable.bacheblue);
                BitmapDescriptor bitmapDescriptor3 = BitmapDescriptorFactory.fromBitmap(img3);

                marker = mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(point.latitude,
                                        point.longitude)).title("Nuevo Marcador")
                        .icon(bitmapDescriptor3));

            }
        });
    }

    public void nuevoEvento(View view){
       // Toast.makeText(getActivity(),"LLEGA", Toast.LENGTH_LONG).show();
        double latitud = marker.getPosition().latitude;
        double longitud= marker.getPosition().longitude;
        RegistrarDenuncia denucia = new RegistrarDenuncia();
        denucia.setLatitudLong(latitud,longitud);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.map, denucia);
        ft.addToBackStack(null);
        ft.commit();
        /*

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
*/

    }


}
