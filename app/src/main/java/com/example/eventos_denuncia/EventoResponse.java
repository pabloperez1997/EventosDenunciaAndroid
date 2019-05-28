package com.example.eventos_denuncia;

import java.util.List;

public class EventoResponse {

    private boolean error;
    private List<Evento> eventos;

    public EventoResponse(boolean error, List<Evento> eventos) {
        this.error = error;
        this.eventos = eventos;
    }

    public boolean isError() {
        return error;
    }

    public List<Evento> getEventos() {
        return eventos;
    }
}
