package com.example.eventos_denuncia;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterDenunciaUsuario extends
        RecyclerView.Adapter<AdapterDenunciaUsuario.DenunciaUsuarioHolder> {

    private Context mCtx;
    private List<Evento> eventosUsuario;

    public AdapterDenunciaUsuario(Context mCtx, List<Evento> eventosUsuario) {
        this.mCtx = mCtx;
        this.eventosUsuario = eventosUsuario;
    }

    @NonNull
    @Override
    public DenunciaUsuarioHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).
                inflate(R.layout.recyclerview_eventos, viewGroup,false);
        return new DenunciaUsuarioHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DenunciaUsuarioHolder denunciaUsuarioHolder, int i) {
        Evento evento = eventosUsuario.get(i);

        denunciaUsuarioHolder.textViewNombre.setText(evento.getNombre());
        denunciaUsuarioHolder.textViewDescripcion.setText(evento.getDescripcion());
//        denunciaUsuarioHolder.textViewEstado.setText(evento.getIdEstado());
        //denunciaUsuarioHolder.imageViewEvento.setImageURI(evento.getFoto());
    }

    @Override
    public int getItemCount() {
        return eventosUsuario.size();
    }

    class DenunciaUsuarioHolder extends RecyclerView.ViewHolder{

        TextView textViewNombre,textViewDescripcion,textViewEstado;
        ImageView imageViewEvento;

    public DenunciaUsuarioHolder(@NonNull View itemView) {
        super(itemView);

        textViewNombre = itemView.findViewById(R.id.textViewNombre);
        textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
      //  textViewEstado = itemView.findViewById(R.id.textViewEstado);
        //imageViewEvento = itemView.findViewById(R.id.imageViewEvento);
    }
}
}
