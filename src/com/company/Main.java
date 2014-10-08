package com.company;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        ObtainRoutesfromJson obtainRoutesfromJson = new ObtainRoutesfromJson();
        obtainRoutesfromJson.loadEstaciones();
        obtainRoutesfromJson.loadRoutes();
        obtainRoutesfromJson.obtainRoutes();
    }


}
