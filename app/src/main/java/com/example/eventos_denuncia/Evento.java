package com.example.eventos_denuncia;

public class Evento {
    private int id;
    private String nombre;
    private String descripcion;
    private String longitud;
    private String latitud;
    private String foto;
    private int idEstado;
    private int activo;

    public Evento(int id, String nombre, String descripcion, String longitud, String latitud, String foto, int idEstado, int activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.foto = foto;
        this.idEstado = idEstado;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getFoto() {
        return foto;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public int getActivo() {
        return activo;
    }
}
