package com.company;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by UNAM Mobile on 20/08/2014.
 */
public class EstacionModel {

    @SerializedName("nom")
    private String nombre;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;
    @SerializedName("rut")
    private List<Integer> rutas;
    @SerializedName("estDes")
    private List<EstacionDestinosModel> destinos;


    public EstacionModel(String nombre, double latitude, double longitude, List<Integer> rutas, List<EstacionDestinosModel> destinos) {
        this.nombre = nombre;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rutas = rutas;
        this.destinos = destinos;
    }


    public String getNombre() {
        return nombre;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<Integer> getRutas() {
        return rutas;
    }

    public List<EstacionDestinosModel> getDestinos() {
        return destinos;
    }
}
