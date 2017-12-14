package net.marcarni.easycheck.Utils;
/*
 * EasyCheck - MarcarniApp
 *
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 *
 * Semestre 1 - Curs 2017 - 2018
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * @author Antoni Torres Mari
 */
public class NetUtils {

    /**
     * Aquest mètode executa una petició
     * http amb el mètode GET amb la URL que obtè per paràmetre.
     *
     * @param url URL de la petició GET
     * @author Antoni Torres Marí
     * @return cadena de caràcters amb la resposta a la petició
     */
    public static String doGetRequest(URL url) {
        String responseBody = "";
        try {
            URLConnection connection = url.openConnection();
            InputStream response = connection.getInputStream();
            Scanner scanner = new Scanner(response);
            responseBody = scanner.useDelimiter("\\A").next();
        } catch (IOException ex) {
           // Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseBody;
    }


    /**
     * Mètode que executa una petició
     * http amb el mètode POST amb la URL i el query que obtè per paràmetre
     *
     * @param url URL de la petició GET
     * @param parameters query de la petició POST
     * @author Antoni Torres Marí
     * @return cadena de caràcters amb la resposta a la petició
     */
    public static String doPostRequest(URL url, String parameters) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //Envia request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            //Rep resposta
            String responseBody = "";
            if (connection.getResponseCode() == 200) {
                InputStream response = connection.getInputStream();
                Scanner scanner = new Scanner(response);
                responseBody = scanner.useDelimiter("\\A").next();
            }
            
            System.out.println(responseBody);
            return responseBody;

        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Construeix i torna un nou objecte URL amb les dades que obtè per paràmetre.
     *
     * @param host host de l'url
     * @param port port de l'url
     * @param path path de l'url
     * @param query query de l'url
     * @return objecte URL
     */
    public static URL buildUrl(String host, int port, String path, String query) {
        try {
            return new URI("http", null, host, port, path, query, null).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
           // Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Comprova que el dispositiu té connexió a la xarxa
     *
     * @param context
     * @return true si es disposa de connexió, false en cas contrari
     */
    public static boolean comprovaXarxa(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
