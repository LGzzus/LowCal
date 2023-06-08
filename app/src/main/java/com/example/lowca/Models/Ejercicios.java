package com.example.lowca.Models;

public class Ejercicios {
    private String id;
    private String ejercicio;
    private String minutos;
    private String repeticioes;
    private String series;
    private String caloriasQuemadas;
    private String fechaRegistro;

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getRepeticioes() {
        return repeticioes;
    }

    public void setRepeticioes(String repeticioes) {
        this.repeticioes = repeticioes;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getCaloriasQuemadas() {
        return caloriasQuemadas;
    }

    public void setCaloriasQuemadas(String caloriasQuemadas) {
        this.caloriasQuemadas = caloriasQuemadas;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMinutos() {
        return minutos;
    }

    public void setMinutos(String minutos) {
        this.minutos = minutos;
    }
}
