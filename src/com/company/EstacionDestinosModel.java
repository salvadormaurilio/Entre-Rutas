package com.company;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by UNAM Mobile on 20/08/2014.
 */
public class EstacionDestinosModel {

    @SerializedName("des")
    private int destino;
    @SerializedName("dis")
    private double distancia;
    @SerializedName("rutDes")
    private List<Integer> rutasDeDestino;
    @SerializedName("ruta")
    private List<Double> ruta;

    public EstacionDestinosModel(int destino, double distancia, List<Integer> rutasDeDestino, List<Double> ruta) {
        this.destino = destino;
        this.distancia = distancia;
        this.rutasDeDestino = rutasDeDestino;
        this.ruta = ruta;
    }

    public int getDestino() {
        return destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public List<Integer> getRutasDeDestino() {
        return rutasDeDestino;
    }

    public List<Double> getRuta() {
        return ruta;
    }

    public void setRuta(List<Double> ruta) {

        this.ruta = ruta;
    }
}
