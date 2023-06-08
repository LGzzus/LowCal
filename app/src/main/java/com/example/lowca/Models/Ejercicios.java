package com.example.lowca.Models;

public class Ejercicios {
    String ejercicio;
    String duracion;
    String calorias;
    String fechaRegistro;

    public String getEjercicio() {
        return ejercicio;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getCalorias() {
        return calorias;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setEjercicio(String ejercicio){
        this.ejercicio = ejercicio;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
