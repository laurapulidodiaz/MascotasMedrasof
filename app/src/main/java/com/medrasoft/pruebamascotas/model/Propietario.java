package com.medrasoft.pruebamascotas.model;

public class Propietario {
    public int id;
    public long cedula;
    public String nombre;
    public long telefono;

    public Propietario() {
    }

    public Propietario(long cedula, String nombre, long telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
}
