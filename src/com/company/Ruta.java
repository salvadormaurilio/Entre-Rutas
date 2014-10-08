package com.company;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by UNAM Mobile on 01/09/2014.
 */
public class Ruta {

    @SerializedName("col")
    private List<Integer> colores;
    @SerializedName("surOeste")
    private List<Double> surOeste;
    @SerializedName("notEste")
    private List<Double> notEste;
    @SerializedName("polos")
    private List<Double> points;

    private int id;

    public Ruta(List<Integer> colores, List<Double> surOeste, List<Double> notEste, List<Double> points) {
        this.colores = colores;
        this.surOeste = surOeste;
        this.notEste = notEste;
        this.points = points;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getColores() {
        return colores;
    }

    public List<Double> getSurOeste() {
        return surOeste;
    }

    public List<Double> getNotEste() {
        return notEste;
    }

    public List<Double> getPoints() {
        return points;
    }

    public int getId() {
        return id;
    }
}
