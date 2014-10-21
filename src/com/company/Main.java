package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ObtainRoutesfromJson obtainRoutesfromJson = new ObtainRoutesfromJson();
        obtainRoutesfromJson.loadEstaciones();
        obtainRoutesfromJson.loadRoutes();
        obtainRoutesfromJson.obtainRoutes();
    }


}
