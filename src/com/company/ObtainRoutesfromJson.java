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

                System.out.println("Estación Destino: " + (estacionDestinosModel.getDestino() - 1) + " , " + estacionModels.get(estacionDestinosModel.getDestino() - 1).getNombre());
                System.out.println("Ruta Obtenida: " + route.toString());
            }

            System.out.println();

        }
    }

    public List<Double> caculeRoute(int start, int end, int route) {

        List<Double> routeCalculete = new ArrayList<Double>();
        List<Double> ruta = rutas.get(route).getPoints();

        int pointStart = 0;
        int pointEnd = 0;

        double latitudeStart = estacionModels.get(start).getLatitude();
        double longitudeStart = estacionModels.get(start).getLongitude();
        double latitudeEnd = estacionModels.get(end).getLatitude();
        double longitudeEnd = estacionModels.get(end).getLongitude();

        double distanceStart = calculeDistance(latitudeStart, longitudeStart, ruta.get(0), ruta.get(1));
        double distanceEnd = calculeDistance(latitudeEnd, longitudeEnd, ruta.get(0), ruta.get(1));

        double distance;

        List<Double> auxList;

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

        System.out.println(pointStart + "," + pointEnd);

        if (pointStart < pointEnd) {

            if (pointEnd - pointStart < ruta.size() - 2 - pointEnd + pointStart) {
                routeCalculete = ruta.subList(pointStart, pointEnd + 2);
            } else {
                routeCalculete.addAll(reverseList(ruta.subList(pointEnd, ruta.size())));
                routeCalculete.addAll(reverseList(ruta.subList(0, pointStart + 2)));
                int aux = pointStart;
                pointStart = pointEnd;
                pointEnd = aux;
            }
        } else {
            if (pointStart - pointEnd < ruta.size() - 2 - pointStart + pointEnd) {
                routeCalculete = reverseList(ruta.subList(pointEnd, pointStart + 2));
                int aux = pointStart;
                pointStart = pointEnd;
                pointEnd = aux;
            } else {
                routeCalculete.addAll(ruta.subList(pointStart, ruta.size()));
                routeCalculete.addAll(ruta.subList(0, pointEnd + 2));
            }

        }



        auxList = nearPoint(latitudeStart, longitudeStart, pointStart, route);

        if (calculateAngle(routeCalculete.get(0), routeCalculete.get(1),
                routeCalculete.get(2), routeCalculete.get(3), auxList.get(0), auxList.get(1))) {

            routeCalculete.remove(0);
            routeCalculete.remove(0);
        }

        routeCalculete.addAll(0, auxList);
//
//
//        auxList = nearPoint(latitudeEnd, longitudeEnd, pointEnd, route);
//
//        if (!calculateAngle(routeCalculete.get(routeCalculete.size() - 2), routeCalculete.get(routeCalculete.size() - 1),
//                routeCalculete.get(routeCalculete.size() - 4), routeCalculete.get(routeCalculete.size() - 3), auxList.get(0), auxList.get(1))) {
//
//            routeCalculete.remove(routeCalculete.size() - 1);
//            routeCalculete.remove(routeCalculete.size() - 1);
//        }
//
//        routeCalculete.addAll(routeCalculete.size(), auxList);


        return routeCalculete;
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


    public List<Double> nearPoint(double latitudeStart, double longitudeStart, int point, int route) {

        int pointNext;

        List<Double> pointObtained;
        List<Double> arrayRoute = rutas.get(route).getPoints();

        if (point == arrayRoute.size() - 2) {
            point = 0;
        }

        pointNext = point + 2;


        if (!calculateAngle(arrayRoute.get(point), arrayRoute.get(point + 1), latitudeStart, longitudeStart, arrayRoute.get(pointNext), arrayRoute.get(pointNext + 1))) {

            if (point == 0) {
                point = arrayRoute.size() - 2;
            }
            pointNext = point - 2;

        }

        pointObtained = pointInter(arrayRoute.get(point + 1), arrayRoute.get(pointNext + 1),
                arrayRoute.get(point), arrayRoute.get(pointNext), longitudeStart, latitudeStart);


        return pointObtained;
    }


    public boolean calculateAngle(double latitudeInit, double longitudeInit, double latitudeStart, double longitudeStart, double latitudeEnd, double longitudeEnd) {

        double latitudeStartNew = latitudeStart - latitudeInit;
        double longitudeStartNew = longitudeStart - longitudeInit;
        double latitudeEndNew = latitudeEnd - latitudeInit;
        double longitudeEndNew = longitudeEnd - longitudeInit;

        double numerartor = latitudeStartNew * latitudeEndNew + longitudeStartNew * longitudeEndNew;

        double latitudeSqrt = Math.sqrt(latitudeStartNew * latitudeStartNew + latitudeEndNew * latitudeEndNew);
        double longitudeSqrt = Math.sqrt(longitudeStartNew * longitudeStartNew + longitudeEndNew * longitudeEndNew);
        double denominator = latitudeSqrt * longitudeSqrt;

        double angle = Math.toDegrees(Math.acos(numerartor / denominator));

        return angle <= 90;
    }


    public List<Double> pointInter(double longitudeA1, double longitudeA2, double latitudeA1, double latitudeA2, double longitudeB1, double latitudeB1) {

        List<Double> pointObt = new ArrayList<Double>();
        double mA;
        double y;
        double x;


        try {
            mA = (latitudeA2 - latitudeA1) / (longitudeA2 - longitudeA1);
        } catch (ArithmeticException e) {
            y = latitudeB1;
            x = longitudeA1;
            pointObt.add(y);
            pointObt.add(x);
            return pointObt;
        }
        if (mA == 0) {
            y = latitudeA1;
            x = longitudeB1;
            pointObt.add(y);
            pointObt.add(x);
            return pointObt;
        }


        double mA2 = mA * mA;


        double yNumerator = latitudeB1 * mA2 + (longitudeB1 - longitudeA1) * mA + latitudeA1;
        double yDenominator = mA2 + 1;
        y = yNumerator / yDenominator;

        x = ((y - latitudeA1) / mA) - longitudeA1;

        pointObt.add(y);
        pointObt.add(x);

        return pointObt;
    }


}
