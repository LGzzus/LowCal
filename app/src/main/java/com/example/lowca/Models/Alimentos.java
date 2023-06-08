package com.example.lowca.Models;

public class Alimentos {
    private String id;
    private String alimento;
    private String caloria;
    private String cantidad;
    private String calorias;
    private String fechaRegistro;

    public String getAlimento() {
        return alimento;
    }

    public void setAlimento(String alimento) {
        this.alimento = alimento;
    }

    public String getCaloria() {
        return caloria;
    }

    public void setCaloria(String categoria) {
        this.caloria = categoria;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }


    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Alimentos{" +
                "alimento='" + alimento + '\'' +
                ", categoria='" + caloria + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return  id;
    }
}
