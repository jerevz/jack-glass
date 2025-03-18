package com.crudapp.model;

import java.io.InputStream;

public class Archivo {
    private int id;
    private String nombre;
    private String ruta;
    private String tipo;
    private String fechaCarga;
    private InputStream contenido;
    private long tamaño;

    // Constructor completo (7 parámetros)
    public Archivo(int id, String nombre, String ruta, String tipo, String fechaCarga, InputStream contenido, long tamaño) {
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.tipo = tipo;
        this.fechaCarga = fechaCarga;
        this.contenido = contenido;
        this.tamaño = tamaño;
    }

    // Constructor sin contenido ni tamaño (5 parámetros)
    public Archivo(int id, String nombre, String ruta, String tipo, String fechaCarga) {
        this(id, nombre, ruta, tipo, fechaCarga, null, 0);
    }

    // Constructor para 6 parámetros (sin tamaño)
    public Archivo(int id, String nombre, String ruta, String tipo, String fechaCarga, InputStream contenido) {
        this(id, nombre, ruta, tipo, fechaCarga, contenido, 0);
    }

    // **NUEVO** Constructor para nombre y ruta (2 parámetros)
    public Archivo(String nombre, String ruta) {
        this(0, nombre, ruta, null, null, null, 0);
    }

    // Constructor vacío
    public Archivo() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(String fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public InputStream getContenido() {
        return contenido;
    }

    public void setContenido(InputStream contenido) {
        this.contenido = contenido;
    }

    public long getTamaño() {
        return tamaño;
    }

    public void setTamaño(long tamaño) {
        this.tamaño = tamaño;
    }

    @Override
    public String toString() {
        return "Archivo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", ruta='" + ruta + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fechaCarga='" + fechaCarga + '\'' +
                '}';
    }
}