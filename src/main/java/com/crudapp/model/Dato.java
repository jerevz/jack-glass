package com.crudapp.model;

public class Dato {
    private int id;
    private String texto;
    private String fecha;
    private String categoria;

    public Dato() {
    }

    public Dato(String texto, String fecha, String categoria) {
        this.texto = texto;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    public Dato(int id, String trim, String text, String trim1) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return id + ": " + texto + ", " + categoria + ", " + fecha;
    }
}