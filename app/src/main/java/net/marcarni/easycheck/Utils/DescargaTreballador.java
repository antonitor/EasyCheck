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

import net.marcarni.easycheck.model.Treballador;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class DescargaTreballador {

    private static final String BASE_URL = "localhost";
    //    private static final String BASE_URL = "easycheck.hopto.org";
    private static final int PORT = 8080;
    public static ArrayList<Treballador> treballadors;
    public static final String IP="10.0.2.2";
    /**
     * Mètode principal
     * @param args
     */
   /* public static void main(String[] args) {
        DescargaTreballador test = new DescargaTreballador();
    }
    /**
     * Constructor
     */
  /*  public DescargaTreballador() {
        treballadors = (ArrayList<Treballador>) obtenirTreballadorsDelServer();
    }*/
    /**
     * Mètode per obtenir els treballadors del server.
     * @return  llista de traballadors.
     */
    public static  List<Treballador> obtenirTreballadorsDelServer(String URL) {
        String json = "";
        URL url = NetUtils.buildUrl(URL, PORT, "/easycheckapi/treballador", null);
        json = NetUtils.doGetRequest(url);
        Gson gson = new Gson();
        java.lang.reflect.Type tipusLlistaDeTreballadors = new TypeToken<List<Treballador>>() {
        }.getType();
        ArrayList<Treballador> llistaDeTreballadors = gson.fromJson(json, tipusLlistaDeTreballadors);

        return llistaDeTreballadors;
    }
    public List<Treballador> obtenirTreballadorsDelServer() {
        String json = "";
        URL url = NetUtils.buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        json = NetUtils.doGetRequest(url);
        Gson gson = new Gson();
        java.lang.reflect.Type tipusLlistaDeTreballadors = new TypeToken<List<Treballador>>() {
        }.getType();
        ArrayList<Treballador> llistaDeTreballadors = gson.fromJson(json, tipusLlistaDeTreballadors);

        return llistaDeTreballadors;
    }
}
