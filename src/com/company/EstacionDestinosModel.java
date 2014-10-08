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
//    private List<Double> puntosIntermedios;

    public EstacionDestinosModel(int destino, double distancia, List<Integer> rutasDeDestino) {
        this.destino = destino;
        this.distancia = distancia;
        this.rutasDeDestino = rutasDeDestino;
//        this.puntosIntermedios = puntosIntermedios;
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

//    public List<Double> getPuntosIntermedios() {
//        return puntosIntermedios;
//    }
}
