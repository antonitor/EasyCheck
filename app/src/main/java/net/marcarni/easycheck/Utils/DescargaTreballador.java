package net.marcarni.easycheck.Utils;

/**
 * Created by Carlos on 28/11/2017.
 */

/**
 *
 * @author Carlos Alberto Castro Cañabate
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.marcarni.easycheck.LoginActivity;
import net.marcarni.easycheck.model.Treballador;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class DescargaTreballador {


    public static ArrayList<Treballador> treballadors;


    /**
     * Mètode per obtenir els treballadors del server.
     * @param URL url
     * @return  llista de traballadors.
     */
    public static  List<Treballador> obtenirTreballadorsDelServer(String URL) {
        String json = "";
        URL url = NetUtils.buildUrl(URL, LoginActivity.PORT, "/easycheckapi/treballador", null);
        json = NetUtils.doGetRequest(url);
        Gson gson = new Gson();
        java.lang.reflect.Type tipusLlistaDeTreballadors = new TypeToken<List<Treballador>>() {
        }.getType();
        ArrayList<Treballador> llistaDeTreballadors = gson.fromJson(json, tipusLlistaDeTreballadors);

        return llistaDeTreballadors;
    }
    /**
     * Mètode per obtenir els treballadors del server. sense Parametre
     * @return  llista de traballadors.
     */
    public static List<Treballador> obtenirTreballadorsDelServer() {
        String json = "";
        URL url = NetUtils.buildUrl(LoginActivity.IP, LoginActivity.PORT, "/easycheckapi/treballador", null);
        json = NetUtils.doGetRequest(url);
        Gson gson = new Gson();
        java.lang.reflect.Type tipusLlistaDeTreballadors = new TypeToken<List<Treballador>>() {
        }.getType();
        ArrayList<Treballador> llistaDeTreballadors = gson.fromJson(json, tipusLlistaDeTreballadors);

        return llistaDeTreballadors;
    }
}
