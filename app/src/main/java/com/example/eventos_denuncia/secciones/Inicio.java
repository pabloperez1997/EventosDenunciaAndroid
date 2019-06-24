package com.example.eventos_denuncia.secciones;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

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
    private FloatingActionButton button;
    String baseURL = "http://192.168.43.118/PhpEventosDenuncia/";
    boolean posocercano= false;


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



        button = (FloatingActionButton) view.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(marker==null)
                    Toast.makeText(getActivity(),"Primero debe marcar un punto en el mapa manteniendolo pulsado",Toast.LENGTH_LONG).show();
                else if (posocercano==true)
                    Toast.makeText(getActivity(),"Poso a menos de 50 metros de alguno existente, corrija el marcador y reintente",Toast.LENGTH_LONG).show();
                else
                nuevoEvento(v);
            }
        });
        //button.show();
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
                    eventos = eventoResponse.getEventos();
                    for (int i=0; i<eventos.size(); i++) {

                        int id = eventos.get(i).getId();
                        final String nombre = eventos.get(i).getNombre();
                        String descripcion = eventos.get(i).getDescripcion();
                        double longitud = Double.parseDouble(eventos.get(i).getLongitud());
                        double latitud = Double.parseDouble(eventos.get(i).getLatitud());
                        int idEstado = eventos.get(i).getIdEstado();
                        int activo = eventos.get(i).getActivo();

                        final LatLng nuevo = new LatLng(latitud, longitud);

                        if (activo == 1){
                        if (eventos.get(i).getIdEstado()==1){
                            Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.bachered);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);

                            mMap.addMarker(new MarkerOptions().position(nuevo).title(nombre).snippet(descripcion + "\n" + "Estado:" + "En Reparación")
                                    .icon(bitmapDescriptor));



                        } else if (eventos.get(i).getIdEstado()==2) {
                            Bitmap img1 = BitmapFactory.decodeResource(getResources(),R.drawable.bacheyellow);
                            BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromBitmap(img1);
                            mMap.addMarker(new MarkerOptions().position(nuevo).title(nombre).snippet(descripcion + "\n" + "Estado:" + "En espera OSE")
                                    .icon(bitmapDescriptor1));
                        }
                        else{
                            Bitmap img2 = BitmapFactory.decodeResource(getResources(),R.drawable.bachegreen);
                            BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromBitmap(img2);
                            mMap.addMarker(new MarkerOptions().position(nuevo).title(nombre).snippet(descripcion + "\n" + "Estado:" + "Reparado")
                                    .icon(bitmapDescriptor2));
                        }
                    }


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
       // mMap.addMarker(new MarkerOptions().position(paysandu).title("Marca de Prueba Paysandu"));
        float zoomLevel = 13.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paysandu, zoomLevel));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(final Marker marker) {

                View myContentView = getLayoutInflater().inflate(
                        R.layout.info_window_layout, null);
                TextView tvTitle = myContentView
                        .findViewById(R.id.title);
                tvTitle.setText("Título: "+marker.getTitle());
                TextView tvSnippet = myContentView
                        .findViewById(R.id.distance);
                tvSnippet.setText("Descripción: "+ marker.getSnippet());

                ImageView image = myContentView
                        .findViewById(R.id.markerImage);

                String foto = null;
                for (int i=0; i<eventos.size(); i++) {
                    if(eventos.get(i).getNombre().compareTo(marker.getTitle())==0){
                        if(eventos.get(i).getFoto()!=null)
                            foto = baseURL+ eventos.get(i).getFoto();
                    }
                }

                if(foto==null){
                    Picasso.get().load(R.drawable.sinfoto).into(image);}
                else {
                    Picasso.get().load(foto).into(image);
                }

                return myContentView;
            }

            @Override
            public View getInfoContents(Marker marker) {
               return null;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                double latitud = marker.getPosition().latitude;
                double longitud= marker.getPosition().longitude;
                StreetView streetView = new StreetView();
                streetView.setLatitudLong(latitud,longitud);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, streetView)
                        .addToBackStack(null)
                        .commit();

            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                posocercano=false;

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

                LatLng markerLatLng = marker.getPosition();
                Location markerLocation = new Location("");
                markerLocation.setLatitude(markerLatLng.latitude);
                markerLocation.setLongitude(markerLatLng.longitude);

                for (int i=0; i<eventos.size(); i++) {
                    Location markerLocation1 = new Location("");
                    markerLocation1.setLatitude(Double.parseDouble(eventos.get(i).getLatitud()));
                    markerLocation1.setLongitude(Double.parseDouble(eventos.get(i).getLongitud()));

                    if(markerLocation1.distanceTo(markerLocation)<50) {
                        String distancia = String.valueOf(markerLocation1.distanceTo(markerLocation));
                        Toast.makeText(getActivity(), "Existe un poso a menos de 50 metros, \n exactamente a " + distancia+" metros", Toast.LENGTH_LONG).show();
                        posocercano=true;
                        return;
                    }
                }
            }
        });
    }


    public void nuevoEvento(View view){

        double latitud = marker.getPosition().latitude;
        double longitud= marker.getPosition().longitude;
        RegistrarDenuncia denucia = new RegistrarDenuncia();
        denucia.setLatitudLong(latitud,longitud);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, denucia)
                .addToBackStack(null)
                .commit();

    }


}
