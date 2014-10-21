package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salva_000 on 23/09/2014.
 */
public class ObtainRoutesfromJson {

    private List<EstacionModel> estacionModels;
    private List<Ruta> rutas;


    public void loadEstaciones() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("estaciones.json"));
        Gson gson = new Gson();
        this.estacionModels = gson.fromJson(bufferedReader, new TypeToken<List<EstacionModel>>() {
        }.getType());

        bufferedReader.close();
    }

    public void loadRoutes() throws IOException {
        Gson gson = new Gson();
        BufferedReader bufferedReader;
        Ruta ruta;
        rutas = new ArrayList<Ruta>();

        for (int i = 0; i < 13; i++) {
            bufferedReader = new BufferedReader(new FileReader("ruta_" + (i + 1) + ".json"));
            ruta = gson.fromJson(bufferedReader, Ruta.class);
            rutas.add(ruta);

            bufferedReader.close();
        }
    }

    public void obtainRoutes() throws IOException {

        List<EstacionDestinosModel> estacionDestinosModelList;
        List<Double> route;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter("estaciones_2.json"));
        BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter("sub_rutas.json"));

        SubRutasModel subRutasModel = new SubRutasModel();


        for (int i = 0; i < estacionModels.size(); i++) {

            subRutasModel.newSubRutas();
            EstacionModel estacionModel = estacionModels.get(i);
            estacionDestinosModelList = estacionModel.getDestinos();

            System.out.println(estacionModel.getNombre());


            for (int j = 0; j < estacionDestinosModelList.size(); j++) {

                EstacionDestinosModel estacionDestinosModel = estacionDestinosModelList.get(j);
                route = caculeRoute(i, estacionDestinosModel.getDestino() - 1, estacionDestinosModel.getRutasDeDestino().get(0) - 1);

                System.out.println("Estación Destino: " + (estacionDestinosModel.getDestino() - 1) + " , " + estacionModels.get(estacionDestinosModel.getDestino() - 1).getNombre());
                System.out.println("Ruta Obtenida: " + route.toString());

                estacionModels.get(i).getDestinos().get(j).setRuta(route);
                subRutasModel.subRutas(i, route);

            }
            System.out.println();

        }

        bufferedWriter1.write(gson.toJson(estacionModels));
        bufferedWriter1.close();

        bufferedWriter2.write(gson.toJson(subRutasModel.getListList()));
        bufferedWriter2.close();

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

        for (int i = 2; i < ruta.size(); i += 2) {

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
                routeCalculete = subList(route, pointStart, pointEnd + 2);


            } else {
                routeCalculete.addAll(reverseList(subList(route, pointEnd, ruta.size())));
                routeCalculete.addAll(reverseList(subList(route, 0, pointStart + 2)));
                int aux = pointStart;
                pointStart = pointEnd;
                pointEnd = aux;
            }
        } else {
            if (pointStart - pointEnd < ruta.size() - 2 - pointStart + pointEnd) {
                routeCalculete = reverseList(subList(route, pointEnd, pointStart + 2));
                int aux = pointStart;
                pointStart = pointEnd;
                pointEnd = aux;
            } else {
                routeCalculete.addAll(subList(route, pointStart, ruta.size()));
                routeCalculete.addAll(subList(route, 0, pointEnd + 2));
            }

        }

//        System.out.println("route 1:" + routeCalculete.toString());


        auxList = nearPoint(latitudeStart, longitudeStart, pointStart, route);
//        System.out.println("Aux 1:" + auxList.toString());

        if (routeCalculete.size() > 2 && calculateAngle(routeCalculete.get(0), routeCalculete.get(1),
                routeCalculete.get(2), routeCalculete.get(3), auxList.get(0), auxList.get(1))) {

            routeCalculete.remove(0);
            routeCalculete.remove(0);
        }

        routeCalculete.addAll(0, auxList);


//        System.out.println("route 2:" + routeCalculete.toString());

        auxList = nearPoint(latitudeEnd, longitudeEnd, pointEnd, route);
//        System.out.println("Aux 2:" + auxList.toString());

        if (calculateAngle(routeCalculete.get(routeCalculete.size() - 2), routeCalculete.get(routeCalculete.size() - 1),
                routeCalculete.get(routeCalculete.size() - 4), routeCalculete.get(routeCalculete.size() - 3), auxList.get(0), auxList.get(1))) {

            routeCalculete.remove(routeCalculete.size() - 1);
            routeCalculete.remove(routeCalculete.size() - 1);
        }

        routeCalculete.addAll(routeCalculete.size(), auxList);


        return routeCalculete;
    }

    public List<Double> subList(int route, int start, int end) {

        List<Double> subList = new ArrayList<Double>();
        List<Double> ruta = rutas.get(route).getPoints();

        double aux;

        for (int i = start; i < end; i += 2) {

            aux = ruta.get(start);
            subList.add(aux);
            aux = ruta.get(start + 1);
            subList.add(aux);
        }

        return subList;

    }


    private double calculeDistance(double latitudeStart, double longitudeStart, double latitudeEnd, double longitudeEnd) {

        return Math.sqrt(Math.pow(latitudeEnd - latitudeStart, 2) + Math.pow(longitudeEnd - longitudeStart, 2));
    }

    private List<Double> reverseList(List<Double> list) {
        List<Double> newList = new ArrayList<Double>();

        double aux;

        for (int i = list.size() - 1; i > 0; i -= 2) {

            aux = list.get(i - 1);
            newList.add(aux);
            aux = list.get(i);
            newList.add(aux);
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


//        System.out.println(arrayRoute.get(point)+","+arrayRoute.get(point + 1)+","+latitudeStart+","+longitudeStart+","+ arrayRoute.get(pointNext)+","+arrayRoute.get(pointNext + 1));

        if (!calculateAngle(arrayRoute.get(point), arrayRoute.get(point + 1), latitudeStart, longitudeStart, arrayRoute.get(pointNext), arrayRoute.get(pointNext + 1))) {

            if (point == 0) {
                point = arrayRoute.size() - 2;
            }
            pointNext = point - 2;

        }

//        System.out.println(arrayRoute.get(point + 1)+","+ arrayRoute.get(pointNext + 1)+","+
//                arrayRoute.get(point)+","+ arrayRoute.get(pointNext)+","+ longitudeStart +","+ latitudeStart);

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

        double startSqrt = Math.sqrt(latitudeStartNew * latitudeStartNew + longitudeStartNew * longitudeStartNew);
        double endSqrt = Math.sqrt(latitudeEndNew * latitudeEndNew + longitudeEndNew * longitudeEndNew);
        double denominator = startSqrt * endSqrt;

        double angle = Math.toDegrees(Math.acos(numerartor / denominator));

//        System.out.println("Angles: " +angle);

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

        x = ((y - latitudeA1) / mA) + longitudeA1;

        pointObt.add(y);
        pointObt.add(x);

        return pointObt;
    }


}
