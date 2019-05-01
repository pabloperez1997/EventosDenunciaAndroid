package com.example.eventos_denuncia;

public class Usuario {
    private String cedula;
    private String name;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
    private String fechan;

    public Usuario(String cedula, String name, String apellido, String email, String password, String telefono, String fechan) {
        this.cedula = cedula;
        this.name = name;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.fechan = fechan;
    }

    public String getCedula() {
        return cedula;
    }

    public String getName() {
        return name;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFechan() {
        return fechan;
    }
}
