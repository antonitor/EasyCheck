package net.marcarni.easycheck.Utils;

/**
 * Created by Carlos on 28/11/2017.
 */

/**
 * @author Carlos Alberto Castro Cañabate
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.marcarni.easycheck.model.Reserva;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class DescargaReserva {

    private static final String BASE_URL = "localhost";
    //    private static final String BASE_URL = "easycheck.hopto.org";
    private static final int PORT = 8080;
    private static Gson gson = new Gson();

    /**
     * Mètode per obtenir les reserves del servidor
     * @return llista de reserves del server
     */
    public static List<Reserva> obtenirReservesDelServer() {
        String json = "";
        URL url = NetUtils.buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", null);
        json = NetUtils.doGetRequest(url);
        java.lang.reflect.Type tipusLlistaDeReserves = new TypeToken<List<Reserva>>() {
        }.getType();
        ArrayList<Reserva> llistaDeReserves = gson.fromJson(json, tipusLlistaDeReserves);
        return llistaDeReserves;
    }

    /**
     * Mètode per obtenir la llista de reserves d'un servei
     * @param servei a filtrar
     * @return llista de reserves d'un servei
     */
    public static ArrayList<Reserva> getReservesServei(int servei) {
        URL url = NetUtils.buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "servei=" + servei);
        String json = NetUtils.doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>() {
        }.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        return llista;
    }
}
