package net.marcarni.easycheck.Utils;

/**
 * Created by Carlos on 28/11/2017.
 */

/**
 * @author Carlos Alberto Castro Cañabate
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.marcarni.easycheck.LoginActivity;
import net.marcarni.easycheck.model.Servei;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DescargaServei {

    //    private static final String BASE_URL = "easycheck.hopto.org";

    public static ArrayList<Servei> serveis;
    private static Gson gson = new Gson();

    /**
     * Mètode per obtenir els serveis del servidor
     * @return llista de serveis del server
     */
    public static List<Servei> obtenirServeisDelServer() {
        String json = "";
        URL url = NetUtils.buildUrl(LoginActivity.IP, LoginActivity.PORT, "/easycheckapi/servei", null);
        json = NetUtils.doGetRequest(url);

        java.lang.reflect.Type tipusLlistaDeServeis = new TypeToken<List<Servei>>() {
        }.getType();
        ArrayList<Servei> llistaDeServeis = gson.fromJson(json, tipusLlistaDeServeis);
        return llistaDeServeis;
    }

    /**
     * Mètode per obtenir els serveis d'un treballador del servidor
     * @param treballador a filtrar
     * @return llista de serveis d'un treballador del server
     */
    public static ArrayList<Servei> getServeisTreballador(int treballador) {
        URL url = NetUtils.buildUrl(LoginActivity.IP, LoginActivity.PORT, "/easycheckapi/servei", "treballador=" + treballador);
        System.out.println(url);
        String json = NetUtils.doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>() {
        }.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        return llista;
    }
    public static List<Servei> obtenirServeisDelServer(String localhost) {
        String json = "";
        URL url = NetUtils.buildUrl(localhost, LoginActivity.PORT, "/easycheckapi/servei", null);
        json = NetUtils.doGetRequest(url);

        java.lang.reflect.Type tipusLlistaDeServeis = new TypeToken<List<Servei>>() {
        }.getType();
        ArrayList<Servei> llistaDeServeis = gson.fromJson(json, tipusLlistaDeServeis);
        return llistaDeServeis;
    }

}
