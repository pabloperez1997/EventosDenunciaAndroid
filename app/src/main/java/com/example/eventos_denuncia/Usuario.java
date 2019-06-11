package com.example.eventos_denuncia;

public class Usuario {
    private String ci;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String fnac;


    public Usuario(String ci, String nombre, String apellido, String email, String telefono, String fechan) {
        this.ci = ci;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fnac = fechan;
    }

    public String getCedula() {
        return ci;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }


    public String getTelefono() {
        return telefono;
    }

    public String getFechan() {
        return fnac;
    }
}
