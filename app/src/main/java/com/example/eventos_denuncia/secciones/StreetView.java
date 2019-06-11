package com.example.eventos_denuncia.secciones;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventos_denuncia.R;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class StreetView extends Fragment implements OnStreetViewPanoramaReadyCallback {

    private double latitud, longitud;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_street_view, container, false);



        return view;


    }

    public void setLatitudLong (double lat, double lonng){

        this.latitud= lat;
        this.longitud=lonng;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StreetViewPanoramaFragment streetViewFragment =
                (StreetViewPanoramaFragment) getActivity().getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewFragment.getStreetViewPanoramaAsync(this);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama)
    {
        streetViewPanorama.setPosition(new LatLng(latitud,longitud));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        StreetViewPanoramaFragment streetViewFragment =
                (StreetViewPanoramaFragment) getActivity().getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        if (streetViewFragment != null)
            getActivity().getFragmentManager().beginTransaction().remove(streetViewFragment).commit();
    }
}
