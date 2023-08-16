package com.example.cafeteria.models;

public class Platillo {

    private int id;
    private String nombre;
    private double precio;
    private int cantidad;
    private String menu;
    private int categoria;

    public Platillo(int id, String nombre, double precio, String menu, int categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.menu = menu;
        this.categoria = categoria;
        this.cantidad = 0;
    }

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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
}
