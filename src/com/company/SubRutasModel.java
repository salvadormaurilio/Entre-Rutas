package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sati on 18/10/2014.
 */
public class SubRutasModel {

    private List<List<List<Double>>> listList;


    public SubRutasModel() {
        this.listList = new ArrayList<List<List<Double>>>();
    }


    public void newSubRutas() {
        listList.add(new ArrayList<List<Double>>());
    }

    public void subRutas(int index, List<Double> rutas) {

        listList.get(index).add(rutas);
    }

    public List<List<List<Double>>> getListList() {
        return listList;
    }
}
