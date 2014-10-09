package com.company;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salva_000 on 23/09/2014.
 */
public class ObtainRoutesfromJson {

    private List<EstacionModel> estacionModels;
    private List<Ruta> rutas;


    public void loadEstaciones() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("estaciones.json"));
        Gson gson = new Gson();
        this.estacionModels = gson.fromJson(bufferedReader, new TypeToken<List<EstacionModel>>() {
        }.getType());
    }

    public void loadRoutes() throws FileNotFoundException {
        Gson gson = new Gson();
        BufferedReader bufferedReader;
        Ruta ruta;
        rutas = new ArrayList<Ruta>();

        for (int i = 0; i < 13; i++) {
            bufferedReader = new BufferedReader(new FileReader("ruta_" + (i + 1) + ".json"));
            ruta = gson.fromJson(bufferedReader, Ruta.class);
            rutas.add(ruta);
        }
    }

    public void obtainRoutes() {

        List<EstacionDestinosModel> estacionDestinosModelList;
        List<Double> route;

        for (int i = 0; i < estacionModels.size(); i++) {
            EstacionModel estacionModel = estacionModels.get(i);
            estacionDestinosModelList = estacionModel.getDestinos();

            System.out.println(estacionModel.getNombre());
            for (EstacionDestinosModel estacionDestinosModel : estacionDestinosModelList) {
                route = caculeRoute(i, estacionDestinosModel.getDestino() - 1, estacionDestinosModel.getRutasDeDestino().get(0) - 1);

                System.out.println("Estación Destino: " + (estacionDestinosModel.getDestino()-1) + " , " + estacionModels.get(estacionDestinosModel.getDestino()-1).getNombre());
                System.out.println("Ruta Obtenida: " + route.toString());
            }

            System.out.println();

        }
    }

    public List<Double> caculeRoute(int start, int end, int route) {

        List<Double> routecalculete = new ArrayList<Double>();
        List<Double> ruta = rutas.get(route).getPoints();

        int pointStart = 0;
        int pointEnd = 0;

        double latitudeStart = estacionModels.get(start).getLatitude();
        double longitudeStart = estacionModels.get(start).getLongitude();
        double latitudeEnd = estacionModels.get(end).getLatitude();
        double longitudeEnd = estacionModels.get(end).getLongitude();

        double distanceStart = calculeDistance(latitudeStart, longitudeStart, ruta.get(0), ruta.get(1));
        double distanceEnd = calculeDistance(latitudeEnd, longitudeEnd, estacionModels.get(0).getLatitude(), estacionModels.get(0).getLongitude());

        double distance;

        for (int i = 0; i < ruta.size(); i += 2) {

            distance = calculeDistance(latitudeStart, longitudeStart, ruta.get(i), ruta.get(i + 1));

            if (distance < distanceStart) {
                distanceStart = distance;
                pointStart = i;
            }

            distance = calculeDistance(latitudeEnd, longitudeEnd, ruta.get(i), ruta.get(i + 1));

            if (distance < distanceEnd) {
                distanceEnd = distance;
                pointEnd = i;

            }
        }

        System.out.println(pointStart+","+pointEnd);

        if (pointStart < pointEnd) {

            if (pointEnd - pointStart < ruta.size() - 2 - pointEnd + pointStart) {
                routecalculete = ruta.subList(pointStart, pointEnd + 2);
            } else {
                routecalculete.addAll(ruta.subList(pointEnd, ruta.size()));
                routecalculete.addAll(ruta.subList(0, pointStart + 2));
            }

        } else {
            if (pointStart - pointEnd < ruta.size() - 2 - pointStart + pointEnd) {
                routecalculete = reverseList(ruta.subList(pointEnd, pointStart + 2));
            } else {
                routecalculete.addAll(ruta.subList(pointStart, ruta.size()));
                routecalculete.addAll(ruta.subList(0, pointEnd + 2));
            }

        }

        return routecalculete;
    }

    private double calculeDistance(double latitudeStart, double longitudeStart, double latitudeEnd, double longitudeEnd) {

        return Math.sqrt(Math.pow(latitudeEnd - latitudeStart, 2) + Math.pow(longitudeEnd - longitudeStart, 2));
    }

    private List<Double> reverseList(List<Double> list) {
        List<Double> newList = new ArrayList<Double>();

        for (int i = list.size() - 1; i > 0; i -= 2) {

            newList.add(list.get(i - 1));
            newList.add(list.get(i));
        }

        return newList;
    }


}
