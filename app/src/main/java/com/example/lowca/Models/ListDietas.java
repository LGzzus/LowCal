package com.example.lowca.Models;

public class ListDietas {
   private  String dieta,tipoDieta;
   private String calorias,id_dieta;
   private  String [] infoDieta;
   private int color;


    public ListDietas(String dieta, String calorias,String [] infoDieta,String tipoDieta,String id_dieta) {
        this.dieta = dieta;
        this.calorias = calorias;
        this.infoDieta=infoDieta;
        this.tipoDieta=tipoDieta;
        this.id_dieta=id_dieta;


    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public String getId_dieta() {
        return id_dieta;
    }

    public String getTipoDieta() {
        return tipoDieta;
    }


    public void setTipoDieta(String tipoDieta) {
        this.tipoDieta = tipoDieta;
    }

    /*public String getInfoDieta() {
        return infoDieta;
    }

    public void setInfoDieta(String infoDieta) {
        this.infoDieta = infoDieta;
    }*/

    public String[] getInfoDieta() {
        return infoDieta;
    }

    public void setInfoDieta(String[] infoDieta) {
        this.infoDieta = infoDieta;
    }

    public String getDieta() {
        return dieta;
    }

    public void setDieta(String dieta) {
        this.dieta = dieta;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }


}
