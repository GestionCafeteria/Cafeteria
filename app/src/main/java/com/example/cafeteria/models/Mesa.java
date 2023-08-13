package com.example.cafeteria.models;

public class Mesa {

    private String id;
    private int estado; // 0->Disponible, 1->Ocupada

    public Mesa(String id, int estado) {
        this.id = id;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
